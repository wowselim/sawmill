CREATE
EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE sawmills
(
    id         uuid      DEFAULT uuid_generate_v4(),
    name       text,
    city       text,
    country    text,
    created_at timestamp DEFAULT current_timestamp,
    updated_at timestamp DEFAULT current_timestamp
);

CREATE
OR REPLACE FUNCTION trigger_set_timestamp()
  RETURNS TRIGGER AS
$$
BEGIN
  NEW.updated_at
= now();
RETURN NEW;
END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER set_timestamp
    BEFORE UPDATE
    ON sawmills
    FOR EACH ROW
    EXECUTE PROCEDURE trigger_set_timestamp();
