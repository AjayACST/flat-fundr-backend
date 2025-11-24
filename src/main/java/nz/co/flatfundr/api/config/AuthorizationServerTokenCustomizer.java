package nz.co.flatfundr.api.config;

import nz.co.flatfundr.api.service.OidcUserInfoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.List;

@Configuration
public class AuthorizationServerTokenCustomizer {

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer(OidcUserInfoService oidcUserInfoService) {
        return context -> {
            if (OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue())) {
                OidcUserInfo userInfo = oidcUserInfoService.loadUser(context.getPrincipal().getName());
                context.getClaims().claims(claims -> claims.putAll(userInfo.getClaims()));
            }
            if (AuthorizationGrantType.AUTHORIZATION_CODE.equals(context.getAuthorizationGrantType())
                || AuthorizationGrantType.REFRESH_TOKEN.equals(context.getAuthorizationGrantType())
                || AuthorizationGrantType.CLIENT_CREDENTIALS.equals(context.getAuthorizationGrantType())) {
                Authentication principal = context.getPrincipal();
                if (principal != null) {
                    List<String> roles = principal.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .filter(a -> a != null && a.startsWith("ROLE"))
                            .map(a -> a.replaceFirst("^ROLE_?", ""))
                            .toList();

                    if (!roles.isEmpty()) {
                        context.getClaims().claim("roles", roles);
                    }
                }
            }
        };
    }
}
