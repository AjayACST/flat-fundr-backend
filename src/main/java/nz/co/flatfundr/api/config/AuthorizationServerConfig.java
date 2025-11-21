package nz.co.flatfundr.api.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.security.KeyStore;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;

@Configuration
@EnableWebSecurity
public class AuthorizationServerConfig {

    private final JwkKeystoreProperties jwkProperties;

    public AuthorizationServerConfig(JwkKeystoreProperties jwkProperties) {
        this.jwkProperties = jwkProperties;
    }

    @Bean
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        // Provide a login redirect for unauthenticated users
        http
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(jwkProperties.getKeystoreLocation().getInputStream(),
                jwkProperties.getKeystorePassword().toCharArray());

        char[] keyPassword = jwkProperties.getKeyPassword().toCharArray();
        String alias = jwkProperties.getKeyAlias();
        var key = keyStore.getKey(alias, keyPassword);

        if (!(key instanceof RSAPrivateKey privateKey)) {
            throw new IllegalStateException("RSA private key not found in keystore alias '" + alias + "'");
        }

        var certificate = keyStore.getCertificate(alias);
        if (certificate == null || !(certificate.getPublicKey() instanceof RSAPublicKey publicKey)) {
            throw new IllegalStateException("RSA public key not found for alias '" + alias + "'");
        }

        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(alias)
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, _) -> jwkSelector.select(jwkSet);
    }

    @Bean
    public AuthorizationServerSettings providerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer("http://localhost:9000")
                .build();
    }

}
