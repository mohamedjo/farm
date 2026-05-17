CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS farm (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    region VARCHAR(128),
    address VARCHAR(512),
    location_latitude DOUBLE PRECISION,
    location_longitude DOUBLE PRECISION,
    email VARCHAR(255),
    register_id VARCHAR(128) UNIQUE,
    phone_number VARCHAR(64),
    created_at TIMESTAMP NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_farm_register_id ON farm (register_id);
CREATE INDEX IF NOT EXISTS idx_farm_name ON farm (name);
CREATE INDEX IF NOT EXISTS idx_farm_region ON farm (region);
