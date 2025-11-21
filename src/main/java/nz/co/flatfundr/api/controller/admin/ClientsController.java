package nz.co.flatfundr.api.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import nz.co.flatfundr.api.core.JpaRegisteredClientRepository;
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

    private final JpaRegisteredClientRepository repo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ClientsController(JpaRegisteredClientRepository repo, PasswordEncoder passwordEncoder) {
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
    public ResponseEntity<Map<String,String>> createClient(@RequestBody Map<String, Object> payload) {
        String clientId = (String) payload.getOrDefault("client_id", UUID.randomUUID().toString());
        String clientName = (String) payload.getOrDefault("client_name", "unnamed");

        String clientSecret = generateOAuthSecret();
        RegisteredClient.Builder builder = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(clientId)
                .clientSecret(passwordEncoder.encode(clientSecret))
                .clientName(clientName)
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

        if (payload.containsKey("redirect_uris")) {
            Object r = payload.get("redirect_uris");
            if (r instanceof java.util.List) {
                for (Object uri : (java.util.List<?>) r) builder.redirectUri(uri.toString());
            }
        }
        if (payload.containsKey("scopes")) {
            Object s = payload.get("scopes");
            if (s instanceof java.util.List) {
                for (Object scope : (java.util.List<?>) s) builder.scope(scope.toString());
            }
        } else {
            builder.scope("openid");
            builder.scope("profile");
            builder.scope("email");
        }

        builder.clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build());

        RegisteredClient rc = builder.build();
        repo.save(rc);

        return ResponseEntity.ok(Map.of("client_id", clientId, "client_secret", clientSecret));
    }
}
