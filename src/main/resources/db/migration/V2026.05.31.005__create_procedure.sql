CREATE TABLE catalog.procedures (
    id   VARCHAR(36) PRIMARY KEY,
    name VARCHAR(128) NOT NULL UNIQUE
);
