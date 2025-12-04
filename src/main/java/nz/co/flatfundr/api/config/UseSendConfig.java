package nz.co.flatfundr.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "usesend")
public class UseSendConfig {
    private String apikey;
    private String endpoint;
    private String fromEmail;
}
