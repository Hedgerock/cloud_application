CREATE SCHEMA IF NOT EXISTS develop_schema;

CREATE TABLE IF NOT EXISTS develop_schema.t_users (
  id SERIAL PRIMARY KEY,
  email VARCHAR NOT NULL UNIQUE,
  password VARCHAR,
  disk_space BIGINT NOT NULL DEFAULT 10737418240,
  used_space BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS develop_schema.t_files (
  id SERIAL PRIMARY KEY,
  name VARCHAR,
  type VARCHAR,
  access_link VARCHAR,
  size BIGINT,
  user_id BIGINT references develop_schema.t_users(id),
  parent_id BIGINT references develop_schema.t_files(id)
);

CREATE TABLE IF NOT EXISTS develop_schema.t_authorities (
    id SERIAL PRIMARY KEY,
    authority VARCHAR
);

CREATE TABLE IF NOT EXISTS develop_schema.t_user_authorities (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES develop_schema.t_users(id),
    authority_id BIGINT NOT NULL REFERENCES develop_schema.t_authorities(id),
    CONSTRAINT uk_user_authority UNIQUE (user_id, authority_id)
);

INSERT INTO develop_schema.t_users
(id, email, password)
VALUES
    (1, 'hedgerock@gmail.com', '$2a$10$zIGv/KeXIxQEUVkh5R7Zg.3J5Gg.prFrWfS4h/vC.n3rSMbLIHGvG'),
    (2, 'slayzer@gmail.com', '$2a$10$MTieVrRO3NqgbdhERviFNeAZSGufSy8uCS4yMaHWS/riMMMUWecnC'),
    (3, 'thexacki@gmail.com', '$2a$10$Z1yaYJdLu06eSDs0zarcnu50Fo7M//7Myz.uTPbpKdvJr19FsMkDC');

INSERT INTO develop_schema.t_files
(id, name, type, access_link, size, user_id, parent_id)
VALUES
    (1, 'file1', 'file', 'all', 0, 1, null),
    (2, 'file2', 'file', 'all', 0, 1, null),
    (3, 'file3', 'file', 'all', 0, 1, null);

INSERT INTO develop_schema.t_authorities
    (id, authority)
VALUES
    (1, 'USER'),
    (2, 'ADMIN');

INSERT INTO develop_schema.t_user_authorities
    (id, user_id, authority_id)
VALUES
    (1, 1, 1),
    (2, 2, 1),
    (3, 3, 1);