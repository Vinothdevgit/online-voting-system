-- USERS TABLE
CREATE TABLE online_voting.users (
    id UUID PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255),
    enabled BOOLEAN NOT NULL
);

-- ROLES TABLE
CREATE TABLE online_voting.roles (
    id UUID PRIMARY KEY,
    user_id UUID REFERENCES online_voting.users(id) ON DELETE CASCADE,
    role_name VARCHAR(50) NOT NULL
);

-- CANDIDATES TABLE
CREATE TABLE online_voting.candidates (
    id VARCHAR(100) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT
);

-- VOTES TABLE
CREATE TABLE online_voting.votes (
    id UUID PRIMARY KEY,
    voter_hash VARCHAR(255) UNIQUE NOT NULL,
    candidate_id VARCHAR(100) REFERENCES online_voting.candidates(id),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
