CREATE TABLE IF NOT EXISTS users
(
    id       SERIAL PRIMARY KEY,
    login    VARCHAR UNIQUE NOT NULL,
    password VARCHAR        NOT NULL
);

CREATE UNIQUE INDEX users_id ON users(id);
CREATE UNIQUE INDEX users_login ON users(login);

CREATE TABLE IF NOT EXISTS locations
(
    id        SERIAL PRIMARY KEY,
    name      VARCHAR NOT NULL,
    user_id    INTEGER REFERENCES users (id) ON DELETE CASCADE,
    latitude  NUMERIC NOT NULL,
    longitude NUMERIC NOT NULL
);

CREATE UNIQUE INDEX locations_id ON locations(id);
CREATE INDEX locations_name ON locations(name);
CREATE INDEX locations_user_id ON locations(user_id);
CREATE UNIQUE INDEX locations_name_user_id ON locations(name, user_id);

CREATE TABLE IF NOT EXISTS sessions
(
    id        Varchar UNIQUE PRIMARY KEY,
    user_id    INTEGER REFERENCES users (id) ON DELETE CASCADE,
    expires_at TIMESTAMP NOT NULL
);

CREATE UNIQUE INDEX sessions_id ON sessions(id);