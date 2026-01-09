CREATE TABLE publications
(
    uuid        UUID NOT NULL DEFAULT uuid_generate_v4(),
    title       VARCHAR(255),
    slug        VARCHAR(255),
    description VARCHAR(255),
    text        VARCHAR(255),
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    deleted_at  TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_publications PRIMARY KEY (uuid)
);