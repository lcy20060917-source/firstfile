INSERT OR IGNORE INTO t_user (id, username, password, email, phone, role, status) VALUES
(1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', 'admin@ecommerce.com', '13800138000', 'ADMIN', 1),
(2, 'test',  'e10adc3949ba59abbe56e057f20f883e', 'test@ecommerce.com',  '13800138001', 'USER', 1);

INSERT OR IGNORE INTO t_category (id, name, parent_id, sort_order) VALUES
(1, '电子产品', NULL, 1),(2, '服装鞋帽', NULL, 2),(3, '食品饮料', NULL, 3),
(4, '手机', 1, 1),(5, '电脑', 1, 2),(6, '男装', 2, 1),(7, '女装', 2, 2);

INSERT OR IGNORE INTO t_product (id, name, description, price, stock, category_id, image_url, status) VALUES
(1, 'iPhone 15', 'Apple iPhone 15 256GB', 6999, 100, 4, '', 1),
(2, '华为 Mate 60 Pro', '华为 Mate 60 Pro', 6999, 50, 4, '', 1),
(3, 'MacBook Pro 14', 'Apple MacBook Pro 14 M3', 14999, 30, 5, '', 1),
(4, 'ThinkPad X1', '联想 ThinkPad X1 Carbon', 9999, 20, 5, '', 1),
(5, 'AirPods Pro', 'Apple AirPods Pro', 1999, 200, 1, '', 1),
(6, '男士休闲夹克', '春季新款男士夹克', 599, 150, 6, '', 1),
(7, '女士连衣裙', '夏季新款连衣裙', 399, 200, 7, '', 1),
(8, '坚果大礼包', '每日坚果 750g', 99, 500, 3, '', 1),
(9, '有机绿茶', '明前龙井 250g', 299, 100, 3, '', 1),
(10, '机械键盘', 'Cherry MX RGB', 799, 80, 1, '', 1);
