import axios from 'axios'
import router from '../router'

/**
 * 封装的 HTTP 请求工具
 *
 * 功能:
 * - 自动附加 Token
 * - 统一错误处理和 401 跳转
 * - 请求去重（同一请求pending时不重复发送）
 * - 请求取消（页面切换时取消旧请求）
 * - 支持 Mock 模式（关闭后端时自动使用本地 Mock 数据）
 */

// ---- 请求去重：pending 中的请求映射 ----
const pendingMap = new Map()
function pendingKey(config) {
  const { method, url, params, data } = config
  return [method, url, JSON.stringify(params), JSON.stringify(data)].join('&')
}
function addPending(config) {
  const key = pendingKey(config)
  if (pendingMap.has(key)) {
    pendingMap.get(key).abort()
    pendingMap.delete(key)
  }
  const controller = new AbortController()
  config.signal = controller.signal
  pendingMap.set(key, controller)
}
function removePending(config) {
  const key = pendingKey(config)
  if (pendingMap.has(key)) {
    pendingMap.delete(key)
  }
}

// ---- Axios 实例 ----
const api = axios.create({
  baseURL: '/api',
  timeout: 15000,
  headers: { 'Content-Type': 'application/json' }
})

// 请求拦截器
api.interceptors.request.use(config => {
  addPending(config)
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
}, error => Promise.reject(error))

// 响应拦截器
api.interceptors.response.use(response => {
  removePending(response.config)
  const { data } = response
  if (data.code === 200 || data.code === 201) {
    return data
  }
  return Promise.reject(new Error(data.message || '请求失败'))
}, error => {
  if (axios.isCancel(error)) {
    console.log('[Request] 请求已取消:', error.message)
    return Promise.reject({ cancelled: true })
  }
  if (error.response) {
    removePending(error.response.config)
    const { status, data } = error.response
    if (status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('username')
      router.push({ name: 'Login', query: { redirect: router.currentRoute.value.fullPath } })
    }
    const err = new Error(data?.message || `请求失败 (${status})`)
    err.response = error.response
    return Promise.reject(err)
  }
  return Promise.reject(new Error(error.message || '网络连接失败'))
})

// ---- Mock 开关 ----
// 当后端不可用时，设置 VITE_USE_MOCK=true 使用 Mock 数据
const useMock = import.meta.env.VITE_USE_MOCK === 'true'

/**
 * 通用 GET 请求（自动降级到 Mock）
 * @param {string} url   - API 路径
 * @param {object} params - 查询参数
 * @param {function} mockFn - Mock 数据函数
 */
export async function get(url, params = {}, mockFn = null) {
  if (useMock && mockFn) {
    const result = await mockFn(params)
    return result
  }
  return api.get(url, { params })
}

/**
 * 通用 POST 请求
 */
export async function post(url, data = {}, mockFn = null) {
  if (useMock && mockFn) {
    return mockFn(data)
  }
  return api.post(url, data)
}

/**
 * 通用 PUT 请求
 */
export async function put(url, data = {}, mockFn = null) {
  if (useMock && mockFn) {
    return mockFn(data)
  }
  return api.put(url, data)
}

/**
 * 通用 DELETE 请求
 */
export async function del(url, mockFn = null) {
  if (useMock && mockFn) {
    return mockFn()
  }
  return api.delete(url)
}

/**
 * 移除所有 pending 请求（页面卸载时使用）
 */
export function cancelAllPending() {
  pendingMap.forEach(controller => controller.abort())
  pendingMap.clear()
}

export { api }
export default { get, post, put, del, cancelAllPending }
