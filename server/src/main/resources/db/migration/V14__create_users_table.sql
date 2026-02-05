CREATE TABLE IF NOT EXISTS users
(
    uuid       UUID   NOT NULL DEFAULT uuid_generate_v4(),
    name       VARCHAR(255)                NOT NULL,
    email      VARCHAR(255)                NOT NULL,
    password   VARCHAR(255)                NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    deleted_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_user PRIMARY KEY (uuid)
);

ALTER TABLE users
    ADD CONSTRAINT uc_user_email UNIQUE (email);