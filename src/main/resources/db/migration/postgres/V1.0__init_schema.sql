-- add modules
CREATE
EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE
EXTENSION IF NOT EXISTS "ltree";


-- Create tables
CREATE TABLE IF NOT EXISTS graph
(
    id
    UUID
    PRIMARY
    KEY,
    name
    VARCHAR
    NOT
    NULL
);

CREATE TABLE IF NOT EXISTS node
(
    id
    UUID
    PRIMARY
    KEY,
    graph_id
    UUID
    NOT
    NULL
    REFERENCES
    graph
(
    id
),
    name VARCHAR NOT NULL,
    path LTREE NOT NULL,
    n_level INTEGER
    );


-- Create indexes
CREATE INDEX IF NOT EXISTS tree_path_idx ON node USING GIST (path);
