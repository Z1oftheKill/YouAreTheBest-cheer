import axios from 'axios'
import { ElMessage } from 'element-plus'
import type { ApiResult } from '../types/task'

const http = axios.create({
  baseURL: '/api',
  timeout: 10000
})

http.interceptors.response.use(
  (response) => {
    const body = response.data as ApiResult<unknown>
    if (body.code !== 200) {
      ElMessage.error(body.message || '请求处理失败')
      return Promise.reject(new Error(body.message || 'Error'))
    }
    return response
  },
  (error) => {
    const msg = error.response?.data?.message || error.message || '网络请求异常'
    ElMessage.error(msg)
    return Promise.reject(error)
  }
)

export async function request<T>(config: Parameters<typeof http.request>[0]): Promise<T> {
  const res = await http.request<ApiResult<T>>(config)
  return res.data.data
}

