CREATE TABLE IF NOT EXISTS user_permissions (
  user_uuid UUID NOT NULL,
  permission VARCHAR(255) NOT NULL
);

ALTER TABLE user_permissions
    ADD CONSTRAINT uc_883e7549c60d339600cb6efa8 UNIQUE (user_uuid, permission);

ALTER TABLE user_permissions
    ADD CONSTRAINT fk_user_permissions_on_user FOREIGN KEY (user_uuid) REFERENCES users (uuid);