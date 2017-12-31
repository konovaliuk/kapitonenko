INSERT INTO user_roles
(name, bundle_name, bundle_key)
VALUES ('admin', 'messages', 'role.admin');

INSERT INTO users
(user_role_id, username, password_hash, active, created_at)
VALUES (1, 'admin', 'admin', TRUE, NOW());

INSERT INTO cashboxes
(fn_number, zn_number, make)
VALUES
  ('default', 'default', 'default'),
  ('1010101010', 'AT2020202020', 'КРОХА'),
  ('0123456789', '12345678910', 'Datecs');

INSERT INTO units
(name, bundle_name, bundle_key) VALUES
  ('kilogram', 'settings', 'unit.kg'),
  ('piece', 'settings', 'pc');

INSERT INTO tax_categories
(name, bundle_name, bundle_key) VALUES
  ('1', 'settings', 'tax.1'),
  ('2', 'settings', 'tax.2');

INSERT INTO localeId
(name)
VALUES
  ('en_US'),
  ('uk_UA');


