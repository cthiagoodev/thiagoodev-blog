CREATE TABLE talks
(
    uuid        UUID         NOT NULL DEFAULT uuid_generate_v4(),
    external_id UUID         NOT NULL,
    title       VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_talks PRIMARY KEY (uuid)
);

ALTER TABLE talks
    ADD CONSTRAINT uc_talks_externalid UNIQUE (external_id);