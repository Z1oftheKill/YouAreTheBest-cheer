export type TaskCategory = 'STUDY' | 'WORK' | 'LIFE' | 'FITNESS'
export type TaskPriority = 1 | 2 | 3
export type TaskStatus = 0 | 1 | 2

/** 对照后端 com.cheer.demo.common.Result<T> */
export interface ApiResult<T> {
  code: number
  message: string
  data: T
}

/** 对照后端 com.cheer.demo.common.PageResult<T> */
export interface PageResult<T> {
  records: T[]
  total: number
  pageNum: number
  pageSize: number
}

/** 对照后端 com.cheer.demo.entity.CheerTask */
export interface CheerTask {
  id: number
  title: string
  description: string
  category: TaskCategory
  priority: TaskPriority
  status: TaskStatus
  cheerNote: string
  completedAt: string | null
  createdAt: string
  updatedAt: string
}

/** 对照后端 com.cheer.demo.dto.TaskSaveDTO */
export interface TaskSavePayload {
  title: string
  description: string
  category: TaskCategory
  priority: TaskPriority
  status: TaskStatus
  cheerNote: string
}

/** 对照后端 com.cheer.demo.dto.DashboardStatsVO */
export interface DashboardStats {
  totalCount: number
  completedCount: number
  inProgressCount: number
  todayCompletedCount: number
  completionRate: number
  cheerQuote: string
}

export interface TaskQueryParams {
  keyword?: string
  category?: string
  status?: number | ''
  pageNum: number
  pageSize: number
  priority?: number | ''
}
