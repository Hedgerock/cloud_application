CREATE SCHEMA IF NOT EXISTS develop_schema;

CREATE TABLE IF NOT EXISTS develop_schema.t_users (
  id SERIAL PRIMARY KEY,
  email VARCHAR(50) UNIQUE,
  password VARCHAR(50),
  disk_space BIGINT DEFAULT 10737418240,
  used_space BIGINT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS develop_schema.t_files (
  id SERIAL PRIMARY KEY,
  name VARCHAR(50),
  type VARCHAR(50),
  access_link VARCHAR(255),
  size BIGINT,
  user_id INTEGER references develop_schema.t_users(id),
  parent_id INTEGER references develop_schema.t_files(id)
);

INSERT INTO develop_schema.t_users
(id, email, password)
VALUES
    (1, 'hedgerock@gmail.com', '12345'),
    (2, 'slayzer@gmail.com', '12345'),
    (3, 'thexacki@gmail.com', '12345');

INSERT INTO develop_schema.t_files
(id, name, type, access_link, size, user_id, parent_id)
VALUES
    (1, 'file1', 'file', 'all', 0, 1, null),
    (2, 'file2', 'file', 'all', 0, 1, null),
    (3, 'file3', 'file', 'all', 0, 1, null);