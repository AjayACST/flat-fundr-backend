CREATE TABLE auth.oauth2_authorization (
                                           id varchar(100) NOT NULL,
                                           registered_client_id varchar(100) NOT NULL,
                                           principal_name varchar(200) NOT NULL,
                                           authorization_grant_type varchar(100) NOT NULL,
                                           authorized_scopes varchar(1000) DEFAULT NULL,
                                           attributes text DEFAULT NULL,
                                           state varchar(500) DEFAULT NULL,
                                           authorization_code_value text DEFAULT NULL,
                                           authorization_code_issued_at timestamptz DEFAULT NULL,
                                           authorization_code_expires_at timestamptz DEFAULT NULL,
                                           authorization_code_metadata text DEFAULT NULL,
                                           access_token_value text DEFAULT NULL,
                                           access_token_issued_at timestamptz DEFAULT NULL,
                                           access_token_expires_at timestamptz DEFAULT NULL,
                                           access_token_metadata text DEFAULT NULL,
                                           access_token_type varchar(100) DEFAULT NULL,
                                           access_token_scopes varchar(1000) DEFAULT NULL,
                                           oidc_id_token_value text DEFAULT NULL,
                                           oidc_id_token_issued_at timestamptz DEFAULT NULL,
                                           oidc_id_token_expires_at timestamptz DEFAULT NULL,
                                           oidc_id_token_metadata text DEFAULT NULL,
                                           refresh_token_value text DEFAULT NULL,
                                           refresh_token_issued_at timestamptz DEFAULT NULL,
                                           refresh_token_expires_at timestamptz DEFAULT NULL,
                                           refresh_token_metadata text DEFAULT NULL,
                                           user_code_value text DEFAULT NULL,
                                           user_code_issued_at timestamptz DEFAULT NULL,
                                           user_code_expires_at timestamptz DEFAULT NULL,
                                           user_code_metadata text DEFAULT NULL,
                                           device_code_value text DEFAULT NULL,
                                           device_code_issued_at timestamptz DEFAULT NULL,
                                           device_code_expires_at timestamptz DEFAULT NULL,
                                           device_code_metadata text DEFAULT NULL,
                                           PRIMARY KEY (id)
);

CREATE TABLE auth.oauth2_registered_client (
                                               id varchar(100) NOT NULL,
                                               client_id varchar(100) NOT NULL,
                                               client_id_issued_at timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                               client_secret varchar(200) DEFAULT NULL,
                                               client_secret_expires_at timestamptz DEFAULT NULL,
                                               client_name varchar(200) NOT NULL,
                                               client_authentication_methods varchar(1000) NOT NULL,
                                               authorization_grant_types varchar(1000) NOT NULL,
                                               redirect_uris varchar(1000) DEFAULT NULL,
                                               post_logout_redirect_uris varchar(1000) DEFAULT NULL,
                                               scopes varchar(1000) NOT NULL,
                                               client_settings varchar(2000) NOT NULL,
                                               token_settings varchar(2000) NOT NULL,
                                               PRIMARY KEY (id)
);

CREATE TABLE auth.oauth2_authorization_consent (
                                                   registered_client_id varchar(100) NOT NULL,
                                                   principal_name varchar(200) NOT NULL,
                                                   authorities varchar(1000) NOT NULL,
                                                   PRIMARY KEY (registered_client_id, principal_name)
);

-- Setup Flat table
CREATE TABLE flats.flat
(
    id        UUID         NOT NULL,
    flat_name VARCHAR(255) NOT NULL,
    owner_id  UUID         NOT NULL,
    CONSTRAINT pk_flat PRIMARY KEY (id)
);

ALTER TABLE flats.flat
    ADD CONSTRAINT uc_flat_owner UNIQUE (owner_id);

ALTER TABLE flats.flat
    ADD CONSTRAINT FK_FLAT_ON_OWNER FOREIGN KEY (owner_id) REFERENCES auth.users (id);

CREATE TABLE auth.user_roles
(
    user_id UUID NOT NULL,
    role    VARCHAR(255)
);

CREATE TABLE auth.users
(
    id              UUID         NOT NULL,
    email           VARCHAR(255) NOT NULL,
    password        VARCHAR(255) NOT NULL,
    first_name      VARCHAR(255) NOT NULL,
    last_name       VARCHAR(255) NOT NULL,
    enabled         BOOLEAN      NOT NULL,
    account_expired BOOLEAN      NOT NULL,
    account_locked  BOOLEAN      NOT NULL,
    first_time_setup BOOLEAN     NOT NULL,
    linked_flat     UUID             NULL,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT fk_users_flat FOREIGN KEY (linked_flat) REFERENCES flats.flat(id)
);

ALTER TABLE auth.users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE auth.user_roles
    ADD CONSTRAINT fk_user_roles_on_user_account FOREIGN KEY (user_id) REFERENCES auth.users (id);

-- Create the join code table
CREATE TABLE flats.flat_join
(
    id          UUID         NOT NULL,
    user_email  VARCHAR(255),
    linked_flat UUID,
    join_code   VARCHAR(255) NOT NULL,
    CONSTRAINT pk_flat_join PRIMARY KEY (id)
);

ALTER TABLE flats.flat_join
    ADD CONSTRAINT FK_FLAT FOREIGN KEY (linked_flat) REFERENCES flats.flat (id);


-- Create the tables for accounts and transactions
CREATE TABLE financial.accounts
(
    id               UUID         NOT NULL,
    flat_id          UUID         NOT NULL,
    akahu_id         VARCHAR(255),
    name             VARCHAR(255) NOT NULL,
    current_balance  DECIMAL      NOT NULL,
    institution_name VARCHAR(255) NOT NULL,
    account_logo     VARCHAR(255),
    CONSTRAINT pk_accounts PRIMARY KEY (id)
);

ALTER TABLE financial.accounts
    ADD CONSTRAINT FK_ACCOUNTS_ON_FLAT FOREIGN KEY (flat_id) REFERENCES flats.flat (id);