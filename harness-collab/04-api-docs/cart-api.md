# 购物车管理 API 文档

**关联设计文档**：[../02-design-docs/ecommerce-platform-design.md](../02-design-docs/ecommerce-platform-design.md)  
**文档版本**：v1.0  
**创建时间**：2026-06-06  
**最后更新**：2026-06-06  
**负责人**：@dev

---

## 概述

- **基础路径**：`/api/cart`
- **API 版本**：v1
- **认证方式**：需要 Bearer Token
- **内容类型**：`application/json`

---

## 接口列表

| 方法 | 路径 | 描述 | 认证 |
|------|------|------|------|
| GET | `/api/cart` | 查看购物车 | 是 |
| POST | `/api/cart/add` | 加入购物车 | 是 |
| PUT | `/api/cart/update` | 更新数量 | 是 |
| DELETE | `/api/cart/remove/{productId}` | 移除商品 | 是 |
| DELETE | `/api/cart/clear` | 清空购物车 | 是 |

---

## 接口详情

### GET /api/cart

**描述**：查看当前用户购物车。

**响应示例（200 OK）**：

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "items": [
      {
        "id": 1,
        "productId": 1,
        "productName": "iPhone 15",
        "productPrice": 6999.00,
        "quantity": 2,
        "subtotal": 13998.00
      }
    ],
    "totalAmount": 13998.00,
    "totalCount": 2
  }
}
```

### POST /api/cart/add

**请求体**：

```json
{
  "productId": 1,
  "quantity": 1
}
```

### PUT /api/cart/update

**请求体**：

```json
{
  "productId": 1,
  "quantity": 3
}
```

---

## 变更记录

| 版本 | 日期 | 变更内容 | 变更人 |
|------|------|----------|--------|
| v1.0 | 2026-06-06 | 初始版本 | @dev |
