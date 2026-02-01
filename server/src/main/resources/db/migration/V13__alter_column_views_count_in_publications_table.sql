UPDATE publications SET views_count = 0 WHERE views_count IS NULL;
ALTER TABLE publications ALTER COLUMN views_count SET DEFAULT 0;
ALTER TABLE publications ALTER COLUMN views_count SET NOT NULL;