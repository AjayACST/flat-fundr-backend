package nz.co.flatfundr.api.service;

import lombok.Getter;
import lombok.Setter;
import nz.co.flatfundr.api.config.UseSendConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class UseSendService {

    @Getter
    @Setter
    class HtmlEmailRequest {
        private String to;
        private String from;
        private String subject;
        private String html;
    }

    private final UseSendConfig config;
    private final RestClient restClient;

    @Autowired
    public UseSendService(UseSendConfig config) {
        this.config = config;
        this.restClient = RestClient.builder().baseUrl(config.getEndpoint()).build();
    }

    public void sendHtmlMail(String htmlContent, String to, String subject) {
        HtmlEmailRequest emailRequest = new HtmlEmailRequest();
        emailRequest.setFrom(config.getFromEmail());
        emailRequest.setTo(to);
        emailRequest.setSubject(subject);
        emailRequest.setHtml(htmlContent);
        restClient.post()
                .uri("/v1/emails")
                .header("Authorization", "Bearer " + config.getApikey())
                .contentType(MediaType.APPLICATION_JSON)
                .body(emailRequest)
                .retrieve()
                .toEntity(String.class);
    }
}
