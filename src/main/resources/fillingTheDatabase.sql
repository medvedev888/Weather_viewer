CREATE TABLE IF NOT EXISTS Users
(
    ID       SERIAL PRIMARY KEY,
    Login    VARCHAR UNIQUE NOT NULL,
    Password VARCHAR        NOT NULL
);

CREATE TABLE IF NOT EXISTS Locations
(
    ID        SERIAL PRIMARY KEY,
    Name      VARCHAR NOT NULL,
    User_ID    INTEGER REFERENCES Users (ID) ON DELETE CASCADE,
    Latitude  DECIMAL NOT NULL,
    Longitude DECIMAL NOT NULL
);

CREATE TABLE IF NOT EXISTS Sessions
(
    ID        Varchar UNIQUE PRIMARY KEY,
    User_ID    INTEGER REFERENCES Users (ID) ON DELETE CASCADE,
    Expires_At TIMESTAMP NOT NULL
);