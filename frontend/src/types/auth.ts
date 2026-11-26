export interface LoginPayload {
  username: string
  password: string
}

export interface RegisterPayload {
  username: string
  password: string
  nickname: string
}

export interface UserInfo {
  id: number
  username: string
  nickname: string
}

export interface LoginResult {
  token: string
  user: UserInfo
}

