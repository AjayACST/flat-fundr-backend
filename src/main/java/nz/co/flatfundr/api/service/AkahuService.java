package nz.co.flatfundr.api.service;

import nz.co.flatfundr.api.config.AkahuConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class AkahuService {
    private final AkahuConfig config;
    private final RestClient restClient;
    @Autowired
    public AkahuService(AkahuConfig config) {
        this.config = config;
        RestClient.Builder rcBuilder = RestClient.builder().baseUrl(config.baseUrl());
        rcBuilder.defaultHeader("X-Akahu-Id", config.appToken());
        rcBuilder.defaultHeader("Authorization", config.userToken());
        this.restClient = rcBuilder.build();
    }

}
