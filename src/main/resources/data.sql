INSERT INTO account_type (name) VALUES
  ('ROLE_STUDENT'),
  ('ROLE_TEACHER'),
  ('ROLE_ADMIN');

INSERT INTO users (user_mail, password, firstname, lastname) VALUES
  ('admin@coddlers.pl', '$2a$04$/XITvTRe.P2gLaDTXRoB5uvoG2kCt9QqmFFPkTZnYAS5fLaaBfg6q', 'admin', 'admin'),
  ('student@coddlers.pl', '$2a$04$iHfzqn2wwLtU6Dd674U8a.869oAtFWwfWZWJwkgQj7abKjKs3zCbO', 'student', 'student'),
  ('teacher@coddlers.pl', '$2a$04$bgUjd.QJD8OJF/j4N6kHeeG3CXh8X1ftjgXAE9aQ3judv.duDS4a2', 'teacher', 'teacher');

INSERT INTO user_account_type (user_id, account_type_name) VALUES
  (1, 'ROLE_ADMIN'),
  (2, 'ROLE_STUDENT'),
  (3, 'ROLE_TEACHER');