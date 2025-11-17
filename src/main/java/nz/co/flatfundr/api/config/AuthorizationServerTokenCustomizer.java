package nz.co.flatfundr.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.List;

@Configuration
public class AuthorizationServerTokenCustomizer {

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
        return context -> {
            if (AuthorizationGrantType.AUTHORIZATION_CODE.equals(context.getAuthorizationGrantType())
                || AuthorizationGrantType.REFRESH_TOKEN.equals(context.getAuthorizationGrantType())
                || AuthorizationGrantType.CLIENT_CREDENTIALS.equals(context.getAuthorizationGrantType())) {

                Authentication principal = context.getPrincipal();
                if (principal != null) {
                    List<String> roles = principal.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .filter(a -> a!=null && (a.startsWith("ROLE_") ||a.startsWith("ROLE")))
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
