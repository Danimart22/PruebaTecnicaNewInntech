SELECT 'CREATE DATABASE votaciones'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'votaciones')\gexec

\c votaciones

CREATE TABLE IF NOT EXISTS app_users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS voters (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    has_voted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS candidates (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    party VARCHAR(255),
    votes BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS votes (
    id BIGSERIAL PRIMARY KEY,
    voter_id BIGINT NOT NULL UNIQUE REFERENCES voters (id),
    candidate_id BIGINT NOT NULL REFERENCES candidates (id)
);

CREATE INDEX IF NOT EXISTS idx_votes_candidate_id ON votes (candidate_id);