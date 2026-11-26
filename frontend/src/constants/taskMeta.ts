import type { TaskCategory, TaskPriority, TaskStatus } from '../types/task'

export const CATEGORY_OPTIONS: { value: TaskCategory; label: string; tagType: 'primary' | 'success' | 'warning' | 'danger' }[] = [
  { value: 'STUDY', label: '学习进阶', tagType: 'primary' },
  { value: 'WORK', label: '工作交付', tagType: 'warning' },
  { value: 'LIFE', label: '生活习惯', tagType: 'success' },
  { value: 'FITNESS', label: '运动健康', tagType: 'danger' }
]

export const PRIORITY_OPTIONS: { value: TaskPriority; label: string; tagType: 'info' | 'warning' | 'danger' }[] = [
  { value: 1, label: '普通', tagType: 'info' },
  { value: 2, label: '重要', tagType: 'warning' },
  { value: 3, label: '紧急', tagType: 'danger' }
]

export const STATUS_OPTIONS: { value: TaskStatus; label: string; tagType: 'info' | 'primary' | 'success' }[] = [
  { value: 0, label: '待开始', tagType: 'info' },
  { value: 1, label: '进行中', tagType: 'primary' },
  { value: 2, label: '已打卡', tagType: 'success' }
]

export function getCategoryMeta(val: string) {
  return CATEGORY_OPTIONS.find((item) => item.value === val) ?? CATEGORY_OPTIONS[0]
}

export function getPriorityMeta(val: number) {
  return PRIORITY_OPTIONS.find((item) => item.value === val) ?? PRIORITY_OPTIONS[1]
}

export function getStatusMeta(val: number) {
  return STATUS_OPTIONS.find((item) => item.value === val) ?? STATUS_OPTIONS[0]
}

