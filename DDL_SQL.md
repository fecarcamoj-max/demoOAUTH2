# DDL Bds::::::::

CREATE DATABASE IF NOT EXISTS demoauth_db;
USE demoauth_db;

CREATE TABLE roles (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_roles_name (name)
);

CREATE TABLE users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(80) NOT NULL,
    password VARCHAR(255) NOT NULL,
    enabled BIT(1) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_users_username (username)
);

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (role_id, user_id),
    CONSTRAINT fk_user_roles_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_user_roles_role
        FOREIGN KEY (role_id) REFERENCES roles(id)
        ON DELETE CASCADE
);

INSERT INTO roles (name) VALUES ('ROLE_USER');

INSERT INTO users (username, password, enabled)
VALUES (
    'user',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    b'1'
);

INSERT INTO user_roles (user_id, role_id)
VALUES (1, 1);

USE demoauth_db;

DELETE FROM user_roles
WHERE user_id IN (SELECT id FROM users WHERE username = 'user');

DELETE FROM users
WHERE username = 'user';
