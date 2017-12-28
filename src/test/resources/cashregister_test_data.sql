INSERT INTO user_roles
(name, bundle_name, bundle_key)
VALUES ('admin', 'messages', 'role.admin');

INSERT INTO users
(user_role_id, username, password_hash, active, created_at)
VALUES (1, 'admin', 'admin', TRUE, NOW());


