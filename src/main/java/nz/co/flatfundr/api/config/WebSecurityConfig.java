package nz.co.flatfundr.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        // use Spring Security's default login page at /login (do not call loginPage())
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/**").permitAll()
                .anyRequest().permitAll()
            )
            .oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(new JwtAuthConverter())))
            .formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
            .logout(LogoutConfigurer::permitAll);
        return http.build();
    }
}
