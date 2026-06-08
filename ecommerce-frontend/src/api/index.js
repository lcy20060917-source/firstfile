import axios from 'axios'
import router from '../router'

const api = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器：自动添加 Token
api.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => Promise.reject(error)
)

// 响应拦截器：统一处理错误
api.interceptors.response.use(
  response => {
    const { data } = response
    if (data.code === 200 || data.code === 201) {
      return data
    }
    return Promise.reject(new Error(data.message || '请求失败'))
  },
  error => {
    if (error.response) {
      const { status, data } = error.response
      if (status === 401) {
        localStorage.removeItem('token')
        localStorage.removeItem('username')
        router.push({ name: 'Login', query: { redirect: router.currentRoute.value.fullPath } })
      }
      // 保留完整的响应信息，供上层使用
      const err = new Error(data?.message || `请求失败 (${status})`)
      err.response = error.response
      return Promise.reject(err)
    }
    return Promise.reject(new Error(error.message || '网络连接失败，请检查后端是否启动'))
  }
)

export default api
