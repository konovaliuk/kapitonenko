INSERT INTO users
(user_role_id, username, password_hash, active, created_at)
VALUES
  (1, 'cash', '9af15b336e6a9619928537df30b2e6a2376569fcf9d7e773eccede65606529a0', TRUE, NOW()),
  (2, 'senior', '9af15b336e6a9619928537df30b2e6a2376569fcf9d7e773eccede65606529a0', TRUE, NOW()),
  (3, 'merch', '9af15b336e6a9619928537df30b2e6a2376569fcf9d7e773eccede65606529a0', TRUE, NOW());

INSERT INTO products
(unit_id, price, tax_category_id, quantity, created_at, created_by, deleted_at, deleted_by)
VALUES
  (2, 8.73, 1, 100.000, '2018-01-21 20:16:34', 3, NULL, NULL),
  (1, 23.99, 1, 90.500, '2018-01-21 20:19:59', 3, NULL, NULL),
  (1, 42.99, 1, 74.300, '2018-01-21 20:22:45', 3, NULL, NULL),
  (2, 124.00, 2, 6.000, '2018-01-21 20:32:25', 3, NULL, NULL),
  (2, 25.49, 1, 0.000, '2018-01-21 20:44:39', 3, NULL, NULL),
  (2, 18.69, 1, 113.000, '2018-01-21 20:46:25', 3, NULL, NULL),
  (2, 20.83, 1, 14.000, '2018-01-21 20:48:20', 3, NULL, NULL);

INSERT INTO product_locale
(product_id, locale_id, property_name, property_value)
VALUES
  (1, 1, 'product.name', 'Kyiv long loaf Kyivkhlib'),
  (1, 2, 'product.name', 'Батон Київський Київхліб'),
  (2, 1, 'product.name', 'Golden apple'),
  (2, 2, 'product.name', 'Яблуко Голден'),
  (3, 1, 'product.name', 'Fish herring frozen'),
  (3, 2, 'product.name', 'Оселедець свіжеморожений'),
  (4, 1, 'product.name', 'Book Tales from Moominvalley'),
  (4, 2, 'product.name', 'Книга дитяча Країна Мумі-тролів'),
  (5, 1, 'product.name', 'Milk Burenka'),
  (5, 2, 'product.name', 'Молоко Буренка'),
  (6, 1, 'product.name', 'Lyubimov Milky Chocolate'),
  (6, 2, 'product.name', 'Шоколад молочний Любімов'),
  (7, 1, 'product.name', 'Pasta vermicelli Makfa'),
  (7, 2, 'product.name', 'Макаронні вироби Макфа Вермішель');

INSERT INTO receipts (cashbox_id, payment_type_id, receipt_type_id, cancelled, created_at, created_by)
VALUES
  (2, 2, 1, 0, '2018-01-22 14:48:47', 1),
  (2, 2, 1, 0, '2018-01-22 14:50:20', 1),
  (2, 2, 1, 1, '2018-01-22 14:59:04', 1),
  (2, 2, 2, 0, '2018-01-22 15:02:48', 1),
  (2, 2, 2, 1, '2018-01-22 15:07:14', 1),
  (2, 3, 1, 0, '2018-01-22 15:07:59', 1),
  (2, 3, 1, 1, '2018-01-22 15:09:41', 1),
  (2, 1, 1, 1, '2018-01-22 15:12:38', 1),
  (3, 2, 1, 0, '2018-01-22 18:05:52', 1),
  (3, 3, 1, 0, '2018-01-22 18:06:33', 1),
  (2, 3, 1, 0, '2018-01-22 18:07:08', 1);


INSERT INTO receipt_products (receipt_id, product_id, quantity)
VALUES
  (1, 4, 1.000),
  (1, 6, 2.000),
  (2, 3, 2.220),
  (2, 1, 1.000),
  (3, 7, 2.000),
  (4, 3, 1.100),
  (5, 3, 2.220),
  (5, 1, 1.000),
  (6, 3, 7.000),
  (7, 1, 2.000),
  (8, 6, 1.000),
  (9, 1, 1.000),
  (9, 2, 2.000),
  (10, 4, 1.000),
  (11, 1, 1.000),
  (11, 2, 2.000);

INSERT INTO z_reports (cashbox_id, cash_balance, created_at, created_by)
VALUES
  (2, 218.26, '2018-01-22 16:30:42', 2),
  (3, 56.71, '2018-01-22 18:08:07', 2),
  (2, 0.00, '2018-01-22 18:08:46', 2);