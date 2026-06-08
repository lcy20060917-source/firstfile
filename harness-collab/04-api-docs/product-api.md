# 商品管理 API 文档

**关联设计文档**：[../02-design-docs/ecommerce-platform-design.md](../02-design-docs/ecommerce-platform-design.md)  
**文档版本**：v1.0  
**创建时间**：2026-06-06  
**最后更新**：2026-06-06  
**负责人**：@dev

---

## 概述

- **基础路径**：`/api/products`
- **API 版本**：v1
- **认证方式**：无需认证（公开接口）
- **内容类型**：`application/json`

---

## 接口列表

| 方法 | 路径 | 描述 | 认证 |
|------|------|------|------|
| GET | `/api/products` | 搜索商品（分页） | 否 |
| GET | `/api/products/{id}` | 商品详情 | 否 |

---

## 接口详情

### GET /api/products

**描述**：分页搜索商品，支持关键词模糊搜索和分类筛选。

**请求参数**：

| 参数名 | 位置 | 类型 | 必填 | 默认值 | 描述 |
|--------|------|------|------|--------|------|
| keyword | query | String | 否 | — | 搜索关键词 |
| page | query | Integer | 否 | 0 | 页码 |
| size | query | Integer | 否 | 10 | 每页数量 |
| category | query | Long | 否 | — | 分类 ID |

**响应示例（200 OK）**：

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "content": [
      {
        "id": 1,
        "name": "iPhone 15",
        "price": 6999.00,
        "stock": 100,
        "imageUrl": ""
      }
    ],
    "totalElements": 10,
    "totalPages": 1,
    "size": 10,
    "number": 0
  }
}
```

### GET /api/products/{id}

**描述**：根据 ID 查询商品详情。

**响应示例（200 OK）**：

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "name": "iPhone 15",
    "description": "Apple iPhone 15 256GB 智能手机",
    "price": 6999.00,
    "stock": 100,
    "categoryId": 4,
    "imageUrl": ""
  }
}
```

---

## 变更记录

| 版本 | 日期 | 变更内容 | 变更人 |
|------|------|----------|--------|
| v1.0 | 2026-06-06 | 初始版本 | @dev |
