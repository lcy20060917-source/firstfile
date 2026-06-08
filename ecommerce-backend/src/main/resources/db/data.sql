-- ============================================================
-- 电商平台 初始测试数据
-- ============================================================

-- 测试用户（密码：123456，BCrypt加密）
INSERT OR IGNORE INTO t_user (username, password, email, phone, role, status) VALUES
('admin', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'admin@ecommerce.com', '13800138000', 'ADMIN', 1),
('test', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'test@ecommerce.com', '13800138001', 'USER', 1);

-- 商品分类
INSERT OR IGNORE INTO t_category (id, name, parent_id, sort_order) VALUES
(1, '电子产品', NULL, 1),
(2, '服装鞋帽', NULL, 2),
(3, '食品饮料', NULL, 3),
(4, '手机', 1, 1),
(5, '电脑', 1, 2),
(6, '男装', 2, 1),
(7, '女装', 2, 2);

-- 商品数据
INSERT OR IGNORE INTO t_product (id, name, description, price, stock, category_id, image_url, status) VALUES
(1, 'iPhone 15', 'Apple iPhone 15 256GB 智能手机', 6999.00, 100, 4, '', 1),
(2, '华为 Mate 60 Pro', '华为 Mate 60 Pro 旗舰手机', 6999.00, 50, 4, '', 1),
(3, 'MacBook Pro 14', 'Apple MacBook Pro 14英寸 M3芯片', 14999.00, 30, 5, '', 1),
(4, 'ThinkPad X1 Carbon', '联想 ThinkPad X1 Carbon 商务笔记本', 9999.00, 20, 5, '', 1),
(5, 'AirPods Pro', 'Apple AirPods Pro 主动降噪无线耳机', 1999.00, 200, 1, '', 1),
(6, '男士休闲夹克', '春季新款男士休闲夹克外套', 599.00, 150, 6, '', 1),
(7, '女士连衣裙', '夏季新款女士碎花连衣裙', 399.00, 200, 7, '', 1),
(8, '坚果大礼包', '每日坚果混合装 (750g)', 99.00, 500, 3, '', 1),
(9, '有机绿茶', '明前龙井绿茶礼盒装 (250g)', 299.00, 100, 3, '', 1),
(10, '机械键盘', 'Cherry MX 机械键盘 RGB背光', 799.00, 80, 1, '', 1);
