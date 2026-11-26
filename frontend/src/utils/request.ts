import axios from 'axios'
import { ElMessage } from 'element-plus'
import type { ApiResult } from '../types/task'

export const TOKEN_KEY = 'cheer_token'
export const USER_KEY = 'cheer_user'

const http = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// 请求拦截器：每次发请求前自动把本地存储的 JWT Token 塞进 Authorization 请求头
http.interceptors.request.use((config) => {
  const token = localStorage.getItem(TOKEN_KEY)
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// 响应拦截器：统一解包 Result<T>，遇到 401 未登录自动通知页面弹出登录框
http.interceptors.response.use(
  (response) => {
    const body = response.data as ApiResult<unknown>
    if (body.code === 401) {
      localStorage.removeItem(TOKEN_KEY)
      localStorage.removeItem(USER_KEY)
      window.dispatchEvent(new CustomEvent('auth:unauthorized'))
      ElMessage.warning(body.message || '请先登录后再操作')
      return Promise.reject(new Error(body.message || 'Unauthorized'))
    }
    if (body.code !== 200) {
      ElMessage.error(body.message || '请求处理失败')
      return Promise.reject(new Error(body.message || 'Error'))
    }
    return response
  },
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem(TOKEN_KEY)
      localStorage.removeItem(USER_KEY)
      window.dispatchEvent(new CustomEvent('auth:unauthorized'))
    }
    const msg = error.response?.data?.message || error.message || '网络请求异常'
    ElMessage.error(msg)
    return Promise.reject(error)
  }
)

export async function request<T>(config: Parameters<typeof http.request>[0]): Promise<T> {
  const res = await http.request<ApiResult<T>>(config)
  return res.data.data
}
