# 用户管理 API 文档

**关联设计文档**：[../02-design-docs/ecommerce-platform-design.md](../02-design-docs/ecommerce-platform-design.md)  
**文档版本**：v1.0  
**创建时间**：2026-06-06  
**最后更新**：2026-06-06  
**负责人**：@dev

---

## 概述

- **基础路径**：`/api/auth`
- **API 版本**：v1
- **认证方式**：Bearer Token（登录/注册接口无需认证）
- **内容类型**：`application/json`
- **字符编码**：UTF-8

---

## 接口列表

| 方法 | 路径 | 描述 | 认证 |
|------|------|------|------|
| POST | `/api/auth/register` | 用户注册 | 否 |
| POST | `/api/auth/login` | 用户登录 | 否 |

---

## 接口详情

### POST /api/auth/register

**描述**：用户注册。

**请求体**：

```json
{
  "username": "testuser",
  "password": "123456",
  "email": "test@example.com",
  "phone": "13800138000"
}
```

**响应示例（200 OK）**：

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "username": "testuser",
    "message": "注册成功"
  }
}
```

---

### POST /api/auth/login

**描述**：用户登录，返回 JWT Token。

**请求体**：

```json
{
  "username": "testuser",
  "password": "123456"
}
```

**响应示例（200 OK）**：

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "tokenType": "Bearer"
  }
}
```

---

## 变更记录

| 版本 | 日期 | 变更内容 | 变更人 |
|------|------|----------|--------|
| v1.0 | 2026-06-06 | 初始版本 | @dev |
