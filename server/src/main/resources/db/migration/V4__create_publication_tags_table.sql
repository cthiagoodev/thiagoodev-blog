CREATE TABLE publication_tags
(
    publication_id UUID NOT NULL,
    tag            VARCHAR(255)
);


ALTER TABLE publication_tags
    ADD CONSTRAINT uc_f0c9948c04ba85f9bdfe00903 UNIQUE (publication_id, tag);

ALTER TABLE publication_tags
    ADD CONSTRAINT fk_publication_tags_on_publication FOREIGN KEY (publication_id) REFERENCES publications (uuid);