-- ============================================================
-- 电商平台 MySQL 数据库初始化脚本
-- ============================================================

CREATE DATABASE IF NOT EXISTS ecommerce DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE ecommerce;

-- 用户表
CREATE TABLE IF NOT EXISTS t_user (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    username    VARCHAR(50)  NOT NULL COMMENT '用户名',
    password    VARCHAR(200) NOT NULL COMMENT '密码（BCrypt加密）',
    email       VARCHAR(100) COMMENT '邮箱',
    phone       VARCHAR(20)  COMMENT '手机号',
    role        VARCHAR(20)  NOT NULL DEFAULT 'USER' COMMENT '角色',
    status      INT          NOT NULL DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 商品分类表
CREATE TABLE IF NOT EXISTS t_category (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    name        VARCHAR(100) NOT NULL COMMENT '分类名称',
    parent_id   BIGINT       COMMENT '父分类ID',
    sort_order  INT          DEFAULT 0 COMMENT '排序',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

-- 商品表
CREATE TABLE IF NOT EXISTS t_product (
    id          BIGINT         NOT NULL AUTO_INCREMENT COMMENT '主键',
    name        VARCHAR(200)   NOT NULL COMMENT '商品名称',
    description TEXT           COMMENT '商品描述',
    price       DECIMAL(10,2)  NOT NULL COMMENT '价格',
    stock       INT            NOT NULL DEFAULT 0 COMMENT '库存',
    category_id BIGINT         COMMENT '分类ID',
    image_url   VARCHAR(500)   COMMENT '商品图片URL',
    status      INT            NOT NULL DEFAULT 1 COMMENT '状态：1-上架，0-下架',
    create_time DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    INDEX idx_name (name),
    INDEX idx_category_id (category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- 购物车表
CREATE TABLE IF NOT EXISTS t_cart_item (
    id          BIGINT   NOT NULL AUTO_INCREMENT COMMENT '主键',
    user_id     BIGINT   NOT NULL COMMENT '用户ID',
    product_id  BIGINT   NOT NULL COMMENT '商品ID',
    quantity    INT      NOT NULL DEFAULT 1 COMMENT '数量',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

-- 订单表
CREATE TABLE IF NOT EXISTS t_order (
    id               BIGINT         NOT NULL AUTO_INCREMENT COMMENT '主键',
    order_no         VARCHAR(32)    NOT NULL COMMENT '订单号',
    user_id          BIGINT         NOT NULL COMMENT '用户ID',
    total_amount     DECIMAL(10,2)  NOT NULL COMMENT '总金额',
    status           INT            NOT NULL DEFAULT 0 COMMENT '状态：0-待支付，1-已支付，2-已取消',
    receiver_name    VARCHAR(50)    NOT NULL COMMENT '收货人',
    receiver_phone   VARCHAR(20)    NOT NULL COMMENT '收货电话',
    receiver_address VARCHAR(500)   NOT NULL COMMENT '收货地址',
    create_time      DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time      DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_order_no (order_no),
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 订单项表
CREATE TABLE IF NOT EXISTS t_order_item (
    id            BIGINT         NOT NULL AUTO_INCREMENT COMMENT '主键',
    order_id      BIGINT         NOT NULL COMMENT '订单ID',
    product_id    BIGINT         NOT NULL COMMENT '商品ID',
    product_name  VARCHAR(200)   NOT NULL COMMENT '商品名称快照',
    product_price DECIMAL(10,2)  NOT NULL COMMENT '商品单价快照',
    quantity      INT            NOT NULL COMMENT '数量',
    total_price   DECIMAL(10,2)  NOT NULL COMMENT '小计金额',
    PRIMARY KEY (id),
    INDEX idx_order_id (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单项表';
