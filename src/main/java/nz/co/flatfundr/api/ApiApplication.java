package nz.co.flatfundr.api;

import nz.co.flatfundr.api.config.AkahuConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import nz.co.flatfundr.api.config.JwkKeystoreProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties({JwkKeystoreProperties.class, AkahuConfig.class})
@EnableScheduling
public class ApiApplication {

    static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

}
