ALTER TABLE publications
ALTER COLUMN slug SET NOT NULL;

ALTER TABLE publications
ADD CONSTRAINT uc_publications_slug UNIQUE (slug);