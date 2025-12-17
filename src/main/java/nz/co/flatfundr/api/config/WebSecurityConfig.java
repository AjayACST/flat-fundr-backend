package nz.co.flatfundr.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public Converter<Jwt, AbstractAuthenticationToken> jwtAuthConverter() {
        return new JwtAuthConverter();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain flatOwnerChain(HttpSecurity http,
                                              Converter<Jwt, AbstractAuthenticationToken> jwtAuthConverter,
                                              FlatOwnerAuthroizationManager ownerManager) throws Exception {
        http
                .securityMatcher("/api/accounts/link/**")
                .authorizeHttpRequests(auth -> auth.anyRequest().access(ownerManager))
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt->jwt.jwtAuthenticationConverter(jwtAuthConverter)));
        return http.build();
    }

    @Bean
    @Order(3)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http, Converter<Jwt, AbstractAuthenticationToken> jwtAuthConverter) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/**").authenticated()
                .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                .anyRequest().permitAll()
            )
            .oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter)))
//          Custom login page to theme it
            .formLogin(login -> login
                .loginPage("/login")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(PathPatternRequestMatcher.withDefaults().matcher("/logout"))
                .permitAll()
            );
        return http.build();
    }
}
