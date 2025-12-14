package nz.co.flatfundr.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "akahu")
public record AkahuConfig(
        String appToken,
        String userToken,
        String baseUrl
) {}
