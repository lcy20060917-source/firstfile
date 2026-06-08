-- ============================================================
-- 电商平台 SQLite 数据库初始化脚本
-- ============================================================

-- 用户表
CREATE TABLE IF NOT EXISTS t_user (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    username    VARCHAR(50)  NOT NULL UNIQUE,
    password    VARCHAR(200) NOT NULL,
    email       VARCHAR(100),
    phone       VARCHAR(20),
    role        VARCHAR(20)  NOT NULL DEFAULT 'USER',
    status      INTEGER      NOT NULL DEFAULT 1,
    create_time DATETIME     NOT NULL DEFAULT (datetime('now','localtime')),
    update_time DATETIME     NOT NULL DEFAULT (datetime('now','localtime'))
);

-- 商品分类表
CREATE TABLE IF NOT EXISTS t_category (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    name        VARCHAR(100) NOT NULL,
    parent_id   BIGINT,
    sort_order  INTEGER      DEFAULT 0,
    create_time DATETIME     NOT NULL DEFAULT (datetime('now','localtime'))
);

-- 商品表
CREATE TABLE IF NOT EXISTS t_product (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    name        VARCHAR(200)   NOT NULL,
    description TEXT,
    price       DECIMAL(10,2)  NOT NULL,
    stock       INTEGER        NOT NULL DEFAULT 0,
    category_id BIGINT,
    image_url   VARCHAR(500),
    status      INTEGER        NOT NULL DEFAULT 1,
    create_time DATETIME       NOT NULL DEFAULT (datetime('now','localtime')),
    update_time DATETIME       NOT NULL DEFAULT (datetime('now','localtime'))
);

-- 购物车表
CREATE TABLE IF NOT EXISTS t_cart_item (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id     BIGINT  NOT NULL,
    product_id  BIGINT  NOT NULL,
    quantity    INTEGER NOT NULL DEFAULT 1,
    create_time DATETIME NOT NULL DEFAULT (datetime('now','localtime')),
    update_time DATETIME NOT NULL DEFAULT (datetime('now','localtime'))
);

-- 订单表
CREATE TABLE IF NOT EXISTS t_order (
    id               INTEGER PRIMARY KEY AUTOINCREMENT,
    order_no         VARCHAR(32)    NOT NULL UNIQUE,
    user_id          BIGINT         NOT NULL,
    total_amount     DECIMAL(10,2)  NOT NULL,
    status           INTEGER        NOT NULL DEFAULT 0,
    receiver_name    VARCHAR(50)    NOT NULL,
    receiver_phone   VARCHAR(20)    NOT NULL,
    receiver_address VARCHAR(500)   NOT NULL,
    create_time      DATETIME       NOT NULL DEFAULT (datetime('now','localtime')),
    update_time      DATETIME       NOT NULL DEFAULT (datetime('now','localtime'))
);

-- 订单项表
CREATE TABLE IF NOT EXISTS t_order_item (
    id            INTEGER PRIMARY KEY AUTOINCREMENT,
    order_id      BIGINT         NOT NULL,
    product_id    BIGINT         NOT NULL,
    product_name  VARCHAR(200)   NOT NULL,
    product_price DECIMAL(10,2)  NOT NULL,
    quantity      INTEGER        NOT NULL,
    total_price   DECIMAL(10,2)  NOT NULL
);
