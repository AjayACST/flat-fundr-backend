package nz.co.flatfundr.api.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.ArrayList;
import java.util.Collection;

public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter scopeConverter = new JwtGrantedAuthoritiesConverter();

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = new ArrayList<>(scopeConverter.convert(jwt));

        Object rolesClaim = jwt.getClaim("roles");

        if (rolesClaim instanceof Collection) {
            for (Object r : (Collection<?>) rolesClaim) {
                if (r != null) {
                    String role = r.toString();
                    if (!role.startsWith("ROLE_")) role = "ROLE_" + role;
                    authorities.add(new SimpleGrantedAuthority(role));
                }
            }
        }

        return new JwtAuthenticationToken(jwt, authorities);
    }
}
