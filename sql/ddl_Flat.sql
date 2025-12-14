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