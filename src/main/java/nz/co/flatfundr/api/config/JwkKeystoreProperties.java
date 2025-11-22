package nz.co.flatfundr.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@ConfigurationProperties(prefix = "security.jwk")
public record JwkKeystoreProperties(
        Resource keystoreLocation,
        String keystorePassword,
        String keyAlias,
        String keyPassword
) {}