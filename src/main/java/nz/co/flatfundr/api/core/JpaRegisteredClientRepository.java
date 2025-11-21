package nz.co.flatfundr.api.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import nz.co.flatfundr.api.entity.RegisteredClientEntity;
import nz.co.flatfundr.api.repository.RegisteredClientEntityRepository;

import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
@Transactional
public class JpaRegisteredClientRepository implements RegisteredClientRepository {

    private final RegisteredClientEntityRepository repository;
    private final ObjectMapper objectMapper;

    @Autowired
    public JpaRegisteredClientRepository(RegisteredClientEntityRepository repository) {
        this.repository = repository;
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void save(RegisteredClient registeredClient) {
        RegisteredClientEntity entity = toEntity(registeredClient);
        repository.save(entity);
    }

    @Override
    public RegisteredClient findById(String id) {
        return repository.findById(id).map(this::toRegisteredClient).orElse(null);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        return repository.findByClientId(clientId).map(this::toRegisteredClient).orElse(null);
    }

    private RegisteredClient toRegisteredClient(RegisteredClientEntity e) {
        RegisteredClient.Builder builder = RegisteredClient.withId(e.getId())
                .clientId(e.getClientId())
                .clientSecret(e.getClientSecret())
                .clientIdIssuedAt(e.getClientIdIssuedAt())
                .clientSecretExpiresAt(e.getClientSecretExpiresAt())
                .clientName(e.getClientName());

        if (e.getRedirectUris() != null && !e.getRedirectUris().isBlank()) {
            Arrays.stream(e.getRedirectUris().split(","))
                    .map(String::trim)
                    .forEach(builder::redirectUri);
        }
        if (e.getScopes() != null && !e.getScopes().isBlank()) {
            Arrays.stream(e.getScopes().split(","))
                    .map(String::trim)
                    .forEach(builder::scope);
        }
        if (e.getGrantTypes() != null && !e.getGrantTypes().isBlank()) {
            Arrays.stream(e.getGrantTypes().split(","))
                    .map(String::trim)
                    .map(AuthorizationGrantType::new)
                    .forEach(builder::authorizationGrantType);
        }
        if (e.getClientAuthMethods() != null && !e.getClientAuthMethods().isBlank()) {
            Arrays.stream(e.getClientAuthMethods().split(","))
                    .map(String::trim)
                    .map(ClientAuthenticationMethod::new)
                    .forEach(builder::clientAuthenticationMethod);
        }

        if (e.getClientSettings() != null && !e.getClientSettings().isBlank()) {
            try {
                ClientSettings cs = objectMapper.readValue(e.getClientSettings(), ClientSettings.class);
                builder.clientSettings(cs);
            } catch (JsonProcessingException ex) {
                // ignore, use defaults
            }
        }
        builder.tokenSettings(TokenSettings.builder()
                .accessTokenTimeToLive(Duration.ofHours(12))
                .refreshTokenTimeToLive(Duration.ofDays(7))
                .build());
//        if (e.getTokenSettings() != null && !e.getTokenSettings().isBlank()) {
//            try {
//                TokenSettings ts = objectMapper.readValue(e.getTokenSettings(), TokenSettings.class);
//                builder.tokenSettings(ts);
//            } catch (JsonProcessingException _) {
//
//            }
//        }

        return builder.build();
    }

    private RegisteredClientEntity toEntity(RegisteredClient r) {
        RegisteredClientEntity e = new RegisteredClientEntity();
        e.setId(r.getId());
        e.setClientId(r.getClientId());
        e.setClientSecret(r.getClientSecret());
        e.setClientIdIssuedAt(r.getClientIdIssuedAt());
        e.setClientSecretExpiresAt(r.getClientSecretExpiresAt());
        e.setClientName(r.getClientName());
        e.setRedirectUris(String.join(",", r.getRedirectUris()));
        e.setScopes(String.join(",", r.getScopes()));
        e.setGrantTypes(r.getAuthorizationGrantTypes().stream().map(AuthorizationGrantType::getValue).collect(Collectors.joining(",")));
        e.setClientAuthMethods(r.getClientAuthenticationMethods().stream().map(ClientAuthenticationMethod::getValue).collect(Collectors.joining(",")));
        try {
            e.setClientSettings(objectMapper.writeValueAsString(r.getClientSettings()));
            e.setTokenSettings(objectMapper.writeValueAsString(r.getTokenSettings().getSettings()));
        } catch (JsonProcessingException _) {

        }
        return e;
    }
}