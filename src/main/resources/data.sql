
INSERT INTO users (id, username, password, full_name, enabled) VALUES
  ('11111111-1111-1111-1111-111111111111', 'admin', '$2b$12$R5r1dre7mSweJcRJHhAI7.zWY2NkRiimFtt/1c4lDGM2z798VmnTy', 'Admin User', true),
  ('22222222-2222-2222-2222-222222222222', 'voter1', '$2b$12$w4t9AnTt3bG8u2t0r9M4IuuASeidjhLkgw.KvxRnP2FB0yxhH5CQC', 'Voter One', true);

INSERT INTO roles (id, user_id, role_name) VALUES
  ('aaaa1111-0000-0000-0000-000000000000', '11111111-1111-1111-1111-111111111111', 'ADMIN'),
  ('bbbb2222-0000-0000-0000-000000000000', '22222222-2222-2222-2222-222222222222', 'USER');

INSERT INTO candidates (id, name, description) VALUES
  ('cand1', 'Candidate A', 'Description A'),
  ('cand2', 'Candidate B', 'Description B');
