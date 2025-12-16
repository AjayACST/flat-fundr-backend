package nz.co.flatfundr.api.service.akahu;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nz.co.flatfundr.api.config.AkahuConfig;
import nz.co.flatfundr.api.service.akahu.model.AkahuAccount;
import nz.co.flatfundr.api.service.akahu.model.AkahuAccountResponse;
import nz.co.flatfundr.api.service.akahu.model.AkahuAccountsResponse;
import nz.co.flatfundr.api.service.akahu.model.AkahuResponseModel;
import nz.co.flatfundr.api.service.akahu.model.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Service
public class AkahuService {
    private final AkahuConfig config;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public AkahuService(AkahuConfig config, ObjectMapper objectMapper) {
        this.config = config;
        this.objectMapper = objectMapper;
        RestClient.Builder rcBuilder = RestClient.builder().baseUrl(config.baseUrl());
        rcBuilder.defaultHeader("X-Akahu-Id", config.appToken());
        rcBuilder.defaultHeader("Authorization", "Bearer " + config.userToken());
        this.restClient = rcBuilder.build();
    }

    public List<AkahuAccount> getAllAccounts() throws AkahuResponseError {
        AkahuAccountsResponse response = akahuGet("/accounts", AkahuAccountsResponse.class);
        List<AkahuAccount> accounts = response.getAccounts();

        if (accounts == null || accounts.isEmpty()) {
            throw new AkahuResponseError("Akahu did not supply any account payload", 500);
        }

        return accounts;
    }

    public AkahuAccount getAccount(String accountId) throws AkahuResponseError {
        AkahuAccountResponse response = akahuGet("/accounts/" + accountId, AkahuAccountResponse.class);
        AkahuAccount account = response.getAccount();

        if (account == null) {
            throw new AkahuResponseError("Akahu did not supply the requested account payload", 404);
        }

        return account;
    }

    private <T extends AkahuResponseModel> T akahuGet(String uri, Class<T> type) throws AkahuResponseError {
        try {
            T response = restClient.get()
                    .uri(uri)
                    .retrieve()
                    .body(type);
            if (response == null) {
                throw new AkahuResponseError("Akahu returned an empty response", 500);
            }
            if (Boolean.FALSE.equals(response.getSuccess())) {
                throw new AkahuResponseError("Akahu reported the request failed", 502);
            }
            return response;
        } catch (HttpClientErrorException e) {
            throw toAkahuResponseError(e);
        } catch (RestClientException e) {
            throw new AkahuResponseError(e.getMessage(), 500);
        }
    }

    private AkahuResponseError toAkahuResponseError(HttpClientErrorException exception) {
        ErrorResponse errorResponse = null;
        try {
            errorResponse = objectMapper.readValue(exception.getResponseBodyAsString(), ErrorResponse.class);
        } catch (JsonProcessingException ignored) {
        }

        String message = errorResponse != null && errorResponse.getErrorMessage() != null
                ? errorResponse.getErrorMessage()
                : exception.getStatusText();
        int status = errorResponse != null && errorResponse.getStatusCode() != null
                ? errorResponse.getStatusCode()
                : exception.getStatusCode().value();

        return new AkahuResponseError(message, status);
    }
}
