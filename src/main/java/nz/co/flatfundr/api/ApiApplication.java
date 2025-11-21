package nz.co.flatfundr.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import nz.co.flatfundr.api.config.JwkKeystoreProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwkKeystoreProperties.class)
public class ApiApplication {

    static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

}
