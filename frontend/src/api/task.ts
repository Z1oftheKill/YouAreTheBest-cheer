import { request } from '../utils/request'
import type {
  CheerTask,
  DashboardStats,
  PageResult,
  TaskQueryParams,
  TaskSavePayload,
  TaskStatus
} from '../types/task'

export const taskApi = {
  getStats() {
    return request<DashboardStats>({ url: '/tasks/stats', method: 'GET' })
  },

  getPage(params: TaskQueryParams) {
    const cleanParams = {
      ...params,
      status: params.status === '' ? undefined : params.status,
      category: params.category || undefined,
      keyword: params.keyword?.trim() || undefined,
      priority: params.priority === '' ? undefined : params.priority
    }
    return request<PageResult<CheerTask>>({ url: '/tasks', method: 'GET', params: cleanParams })
  },

  create(data: TaskSavePayload) {
    return request<CheerTask>({ url: '/tasks', method: 'POST', data })
  },

  update(id: number, data: TaskSavePayload) {
    return request<CheerTask>({ url: `/tasks/${id}`, method: 'PUT', data })
  },

  patchStatus(id: number, status: TaskStatus, cheerNote?: string) {
    return request<CheerTask>({
      url: `/tasks/${id}/status`,
      method: 'PATCH',
      data: { status, cheerNote }
    })
  },

  remove(id: number) {
    return request<void>({ url: `/tasks/${id}`, method: 'DELETE' })
  },

  batchRemove(ids: number[]) {
    return request<void>({ url: '/tasks/batch-delete', method: 'POST', data: ids })
  }
}
