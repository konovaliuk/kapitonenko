USE `cashregister_test`;

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
(name, bundle_name, bundle_key, rate) VALUES
  ('1', 'settings', 'tax.1', 20),
  ('2', 'settings', 'tax.2', 0);

INSERT INTO locale
(name)
VALUES
  ('en_US'),
  ('uk_UA');

INSERT INTO `receipt_types`
(`id`, `name`, `bundle_name`, `bundle_key`)
VALUES
  (NULL, 'fiscal', 'settings', 'receipt.type.fiscal'),
  (NULL, 'return', 'settings', 'receipt.type.return');

INSERT INTO `payment_types`
(`id`, `name`, `bundle_name`, `bundle_key`)
VALUES
  (NULL, 'cash', 'settings', 'payment.type.cash');

INSERT INTO `products`
(`id`, `unit_id`, `price`, `tax_category_id`, `quantity`, `created_at`, `created_by`, `deleted_at`, `deleted_by`)
VALUES (1, 1, 9.99, 1, 999.999, NOW(), 1, NULL, NULL);

INSERT INTO `receipts`
(`id`, `cashbox_id`, `payment_type_id`, `receipt_type_id`, `cancelled`, `created_at`, `created_by`)
VALUES
  (NULL, 1, 1, 1, 0, NOW(), 1),
  (NULL, 2, 1, 2, 1, NOW(), 1);

INSERT INTO `receipt_products`
(id, receipt_id, product_id, quantity)
VALUES
  (1, 1, 1, 111.111);






