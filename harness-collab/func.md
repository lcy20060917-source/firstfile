# 功能资产总表

本文档是项目所有功能的资产登记表，记录每个功能的当前状态、负责人和关联文档。每次功能状态发生变更时，必须同步更新本表。

**最后更新**：2026-06-06

---

## 功能列表

| 功能名称 | 状态 | 负责人 | 需求文档 | 设计文档 | API 文档 | 最后更新 |
|----------|------|--------|----------|----------|----------|----------|
| 用户管理 | 已交付 | @dev | [用户管理需求](01-product-specs/ecommerce-platform-spec.md) | [电商平台设计](02-design-docs/ecommerce-platform-design.md) | [用户 API](04-api-docs/user-api.md) | 2026-06-06 |
| 商品管理 | 已交付 | @dev | [商品管理需求](01-product-specs/ecommerce-platform-spec.md) | [电商平台设计](02-design-docs/ecommerce-platform-design.md) | [商品 API](04-api-docs/product-api.md) | 2026-06-06 |
| 购物车管理 | 已交付 | @dev | [购物车需求](01-product-specs/ecommerce-platform-spec.md) | [电商平台设计](02-design-docs/ecommerce-platform-design.md) | [购物车 API](04-api-docs/cart-api.md) | 2026-06-06 |
| 订单管理 | 已交付 | @dev | [订单需求](01-product-specs/ecommerce-platform-spec.md) | [电商平台设计](02-design-docs/ecommerce-platform-design.md) | [订单 API](04-api-docs/order-api.md) | 2026-06-06 |

---

## 状态说明

| 状态 | 含义 | 下一步行动 |
|------|------|-----------|
| 🔵 规划中 | 功能已列入计划，尚未开始开发 | 创建需求文档，进入需求分析阶段 |
| 🟡 开发中 | 功能正在开发，代码尚未完成 | 完成编码实现，进入测试验证阶段 |
| 🟠 测试中 | 代码已完成，正在进行测试验证 | 完成测试，更新执行计划，进入文档同步阶段 |
| 🟢 已交付 | 功能已完成所有阶段，CI 已通过 | 无（持续维护） |
| ⚫ 已废弃 | 功能已下线或不再维护 | 归档相关文档 |
