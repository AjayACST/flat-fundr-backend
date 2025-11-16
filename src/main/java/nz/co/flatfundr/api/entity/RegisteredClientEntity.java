package nz.co.flatfundr.api.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "registered_client", schema = "auth")
public class RegisteredClientEntity {

    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String clientId;

    private String clientSecret;

    private Instant clientIdIssuedAt;

    private Instant clientSecretExpiresAt;

    private String clientName;

    @Column(columnDefinition = "text")
    private String redirectUris; // comma separated

    @Column(columnDefinition = "text")
    private String scopes; // comma separated

    @Column(columnDefinition = "text")
    private String grantTypes; // comma separated

    @Column(columnDefinition = "text")
    private String clientAuthMethods; // comma separated

    @Column(columnDefinition = "text")
    private String clientSettings; // json

    @Column(columnDefinition = "text")
    private String tokenSettings; // json

    // getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public Instant getClientIdIssuedAt() {
        return clientIdIssuedAt;
    }

    public void setClientIdIssuedAt(Instant clientIdIssuedAt) {
        this.clientIdIssuedAt = clientIdIssuedAt;
    }

    public Instant getClientSecretExpiresAt() {
        return clientSecretExpiresAt;
    }

    public void setClientSecretExpiresAt(Instant clientSecretExpiresAt) {
        this.clientSecretExpiresAt = clientSecretExpiresAt;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getRedirectUris() {
        return redirectUris;
    }

    public void setRedirectUris(String redirectUris) {
        this.redirectUris = redirectUris;
    }

    public String getScopes() {
        return scopes;
    }

    public void setScopes(String scopes) {
        this.scopes = scopes;
    }

    public String getGrantTypes() {
        return grantTypes;
    }

    public void setGrantTypes(String grantTypes) {
        this.grantTypes = grantTypes;
    }

    public String getClientAuthMethods() {
        return clientAuthMethods;
    }

    public void setClientAuthMethods(String clientAuthMethods) {
        this.clientAuthMethods = clientAuthMethods;
    }

    public String getClientSettings() {
        return clientSettings;
    }

    public void setClientSettings(String clientSettings) {
        this.clientSettings = clientSettings;
    }

    public String getTokenSettings() {
        return tokenSettings;
    }

    public void setTokenSettings(String tokenSettings) {
        this.tokenSettings = tokenSettings;
    }
}

