import { request } from '../utils/request'
import type { LoginPayload, LoginResult, RegisterPayload, UserInfo } from '../types/auth'

export const authApi = {
  login(data: LoginPayload) {
    return request<LoginResult>({ url: '/auth/login', method: 'POST', data })
  },

  register(data: RegisterPayload) {
    return request<LoginResult>({ url: '/auth/register', method: 'POST', data })
  },

  getMe() {
    return request<UserInfo>({ url: '/auth/me', method: 'GET' })
  }
}

