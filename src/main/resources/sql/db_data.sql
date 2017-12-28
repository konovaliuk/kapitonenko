INSERT INTO user_roles
(name, bundle_name, bundle_key)
VALUES
  ('cashier', 'settingsMap', 'role.cashier'),
  ('senior cashier', 'settingsMap', 'role.seniorCashier'),
  ('merchandiser', 'settingsMap', 'role.merchandiser');

INSERT INTO cashboxes
(fn_number, zn_number, make)
VALUES
  ('default', 'default', 'default'),
  ('1010101010', 'AT2020202020', 'КРОХА'),
  ('0123456789', '12345678910', 'Datecs');

INSERT INTO locale
(name)
VALUES
  ('en_US'),
  ('uk_UA');

