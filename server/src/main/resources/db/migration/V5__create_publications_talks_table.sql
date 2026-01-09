CREATE TABLE publications_talks
(
    publication_id UUID NOT NULL,
    talk_id        UUID NOT NULL
);

ALTER TABLE publications_talks
    ADD CONSTRAINT uc_bca0971efab0def13f6bdc148 UNIQUE (publication_id, talk_id);

ALTER TABLE publications_talks
    ADD CONSTRAINT fk_pubtal_on_publication FOREIGN KEY (publication_id) REFERENCES publications (uuid);

ALTER TABLE publications_talks
    ADD CONSTRAINT fk_pubtal_on_talk FOREIGN KEY (talk_id) REFERENCES talks (uuid);