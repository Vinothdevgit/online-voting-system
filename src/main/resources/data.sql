INSERT INTO online_voting.users (id, username, password, full_name, enabled) VALUES
('11111111-1111-1111-1111-111111111111', 'admin',
'$2b$12$R5r1dre7mSweJcRJHhAI7.zWY2NkRiimFtt/1c4lDGM2z798VmnTy', 'Admin User', true);

INSERT INTO online_voting.roles (id, user_id, role_name) VALUES
(gen_random_uuid(), '11111111-1111-1111-1111-111111111111', 'ROLE_ADMIN');

INSERT INTO online_voting.candidates (id, name, description) VALUES
  ('cand1', 'Candidate A', 'Description A'),
  ('cand2', 'Candidate B', 'Description B');
