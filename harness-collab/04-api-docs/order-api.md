# 订单管理 API 文档

**关联设计文档**：[../02-design-docs/ecommerce-platform-design.md](../02-design-docs/ecommerce-platform-design.md)  
**文档版本**：v1.0  
**创建时间**：2026-06-06  
**最后更新**：2026-06-06  
**负责人**：@dev

---

## 概述

- **基础路径**：`/api/orders`
- **API 版本**：v1
- **认证方式**：需要 Bearer Token
- **内容类型**：`application/json`

---

## 接口列表

| 方法 | 路径 | 描述 | 认证 |
|------|------|------|------|
| POST | `/api/orders` | 创建订单 | 是 |
| GET | `/api/orders` | 订单列表 | 是 |
| GET | `/api/orders/{id}` | 订单详情 | 是 |
| PUT | `/api/orders/{id}/cancel` | 取消订单 | 是 |

---

## 接口详情

### POST /api/orders

**描述**：基于当前用户购物车创建订单。

**请求体**：

```json
{
  "receiverName": "张三",
  "receiverPhone": "13800138000",
  "receiverAddress": "北京市朝阳区xx街道xx号"
}
```

**响应示例（200 OK）**：

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "orderNo": "20260606120000abc12345",
    "totalAmount": 13998.00,
    "status": 0,
    "statusDesc": "待支付",
    "receiverName": "张三",
    "receiverPhone": "13800138000",
    "receiverAddress": "北京市朝阳区xx街道xx号",
    "items": [
      {
        "productName": "iPhone 15",
        "productPrice": 6999.00,
        "quantity": 2,
        "totalPrice": 13998.00
      }
    ]
  }
}
```

### GET /api/orders

**描述**：分页查询当前用户订单列表。

**请求参数**：

| 参数名 | 类型 | 必填 | 默认值 | 描述 |
|--------|------|------|--------|------|
| page | Integer | 否 | 0 | 页码 |
| size | Integer | 否 | 10 | 每页数量 |

### GET /api/orders/{id}

**描述**：查询订单详情。

### PUT /api/orders/{id}/cancel

**描述**：取消订单（仅待支付状态可取消），自动恢复库存。

---

## 变更记录

| 版本 | 日期 | 变更内容 | 变更人 |
|------|------|----------|--------|
| v1.0 | 2026-06-06 | 初始版本 | @dev |
