/**
 * 商品 Mock 数据 — 后端不可用时自动启用
 *
 * 模拟真实分页：每页一次请求，根据 page/size 返回对应切片数据
 * 支持分类筛选和关键词搜索
 */

// 生成 50 条模拟商品
const ALL_PRODUCTS = Array.from({ length: 50 }, (_, i) => ({
  id: i + 1,
  name: [
    'iPhone 15 Pro Max', '华为 Mate 60 Pro', 'MacBook Pro 14', 'AirPods Pro 2',
    'iPad Air', '华为 Watch GT4', '小米 14 Ultra', '联想 ThinkPad X1',
    'Sony WH-1000XM5', '戴森 V15 吸尘器', 'Switch OLED', 'PS5 光驱版',
    '大疆 DJI Mini 4 Pro', 'Kindle Paperwhite', 'Apple Watch Ultra',
    '男士休闲夹克', '女士碎花连衣裙', '复古牛仔外套', '羊绒围巾', '运动跑鞋',
    '每日坚果大礼包', '有机绿茶礼盒', '进口咖啡豆', '黑巧克力礼盒', '特级橄榄油',
    '机械键盘 Cherry MX', '4K显示器 27寸', '无线鼠标', 'USB-C扩展坞', '桌面音箱',
    '投影仪 XGIMI', '电动牙刷', '剃须刀飞利浦', '吹风机 Dyson', '扫地机器人',
    '空气炸锅', '电饭煲 IH', '破壁机', '净水器', '加湿器',
    '台灯 LED护眼', '人体工学椅', '升降桌', '书包双肩', '行李箱 20寸',
    '帐篷户外', '睡袋羽绒', '登山鞋', '运动水壶', '瑜伽垫'
  ][i],
  description: `这是商品 ${i + 1} 的详细描述，品质优良，值得购买。库存充足，下单即发。`,
  price: [6999, 6499, 14999, 1899, 4599, 1488, 4999, 8999, 2299, 3990,
          2599, 3899, 4199, 998, 6299, 599, 399, 799, 299, 599,
          99, 299, 159, 89, 199, 799, 2999, 199, 299, 499,
          2699, 399, 599, 2999, 3499, 599, 999, 799, 1999, 399,
          299, 2499, 1899, 399, 699, 499, 699, 899, 129, 59][i],
  stock: Math.floor(Math.random() * 200) + 1,
  categoryId: [4, 4, 5, 1, 5, 4, 4, 5, 1, 1,
               1, 1, 1, 1, 4, 6, 7, 6, 6, 6,
               3, 3, 3, 3, 3, 1, 5, 1, 5, 1,
               1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
               1, 1, 1, 6, 6, 6, 6, 6, 6, 6][i],
  imageUrl: '',
  status: 1
}))

// 分类映射
const CATEGORIES = {
  1: '电子产品', 2: '服装鞋帽', 3: '食品饮料',
  4: '手机', 5: '电脑', 6: '男装', 7: '女装'
}

/**
 * Mock 分页查询商品
 * 每次调用模拟一次真实 HTTP 请求，有网络延迟
 *
 * @param {object} params - { keyword, page, size, category }
 * @returns {Promise} 模拟 API 响应
 */
export async function mockGetProducts(params = {}) {
  const { keyword, page = 0, size = 12, category } = params

  // 模拟网络延迟 300-600ms
  const delay = 300 + Math.random() * 300
  await new Promise(resolve => setTimeout(resolve, delay))

  console.log(`[Mock] GET /api/products?page=${page}&size=${size}&keyword=${keyword || ''}&category=${category || ''}`)

  let filtered = [...ALL_PRODUCTS]

  // 关键词搜索
  if (keyword) {
    const kw = keyword.toLowerCase()
    filtered = filtered.filter(p => p.name.toLowerCase().includes(kw))
  }

  // 分类筛选
  if (category) {
    filtered = filtered.filter(p => p.categoryId === Number(category))
  }

  const total = filtered.length
  const totalPages = Math.ceil(total / size)
  const start = page * size
  const content = filtered.slice(start, start + size)

  // 返回与后端一致的格式
  return {
    code: 200,
    message: 'success',
    data: {
      content,
      totalElements: total,
      totalPages,
      size,
      number: page
    }
  }
}

/**
 * Mock 商品详情
 */
export async function mockGetProduct(id) {
  await new Promise(resolve => setTimeout(resolve, 200))
  const product = ALL_PRODUCTS.find(p => p.id === Number(id))
  if (!product) {
    return { code: 404, message: '商品不存在', data: null }
  }
  return { code: 200, message: 'success', data: product }
}

/**
 * 获取分类列表
 */
export function getCategoryList() {
  return Object.entries(CATEGORIES).map(([id, name]) => ({
    id: Number(id), name, parentId: id <= 3 ? null : Math.floor(Number(id) / 2)
  }))
}
