-- Create user
CREATE USER online_voting WITH PASSWORD 'Bishob#2025';

-- Grant access to the database
GRANT CONNECT ON DATABASE bishop_heber_db TO online_voting;

-- Grant usage on the schema
GRANT USAGE ON SCHEMA online_voting TO online_voting;

-- Grant all privileges on the schema (optional)
GRANT ALL PRIVILEGES ON SCHEMA online_voting TO online_voting;

-- Allow user to use all current and future tables
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA online_voting TO online_voting;

-- Optional: make it default for future tables
ALTER DEFAULT PRIVILEGES IN SCHEMA online_voting
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO online_voting;