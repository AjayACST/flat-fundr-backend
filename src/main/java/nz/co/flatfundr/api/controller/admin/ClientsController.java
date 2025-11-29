package nz.co.flatfundr.api.controller.admin;

import jakarta.validation.Valid;
import nz.co.flatfundr.api.dto.admin.CreateClientRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/clients")
public class ClientsController {

    private final RegisteredClientRepository repo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ClientsController(RegisteredClientRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    private static String generateOAuthSecret() {
        SecureRandom random = new SecureRandom();
        byte[] secretBytes = new byte[32];
        random.nextBytes(secretBytes);

        return Base64.getUrlEncoder().withoutPadding().encodeToString(secretBytes);
    }

    @PostMapping
    public ResponseEntity<Map<String,String>> createClient(@Valid @RequestBody CreateClientRequest payload) {
        String clientId = UUID.randomUUID().toString();

        String clientSecret = generateOAuthSecret();
        RegisteredClient.Builder builder = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(clientId)
                .clientSecret(passwordEncoder.encode(clientSecret))
                .clientName(payload.clientName())
                .clientIdIssuedAt(Instant.now())
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofHours(12))
                        .refreshTokenTimeToLive(Duration.ofDays(7))
                        .build()
                )
                .clientSecretExpiresAt(null);

        // defaults: authorization_code with PKCE and refresh_token
        builder.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
        builder.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE);
        builder.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN);

        for (String r : payload.redirectUris()) builder.redirectUri(r);

        builder.scope("openid");
        builder.scope("profile");
        builder.scope("email");

        builder.clientSettings(ClientSettings.builder().requireAuthorizationConsent(false).build());

        RegisteredClient rc = builder.build();
        repo.save(rc);

        return ResponseEntity.ok(Map.of("client_id", clientId, "client_secret", clientSecret));
    }
}
