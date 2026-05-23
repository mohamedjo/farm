ALTER TABLE farm
    ADD COLUMN IF NOT EXISTS number_of_animals INT NOT NULL DEFAULT 0;

CREATE TABLE IF NOT EXISTS consumed_event (
    event_id UUID PRIMARY KEY,
    consumed_at TIMESTAMP NOT NULL
);
