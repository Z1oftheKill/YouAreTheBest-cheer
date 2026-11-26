<script setup lang="ts">
import { onBeforeUnmount, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { taskApi } from './api/task'
import AuthDialog from './components/AuthDialog.vue'
import StatsHeader from './components/StatsHeader.vue'
import TaskFilterBar from './components/TaskFilterBar.vue'
import TaskTable from './components/TaskTable.vue'
import TaskFormDialog from './components/TaskFormDialog.vue'
import { TOKEN_KEY, USER_KEY } from './utils/request'
import type { LoginResult, UserInfo } from './types/auth'
import type {
  CategoryStat,
  CheerTask,
  DashboardStats,
  TaskQueryParams,
  TaskSavePayload
} from './types/task'

function readStoredUser(): UserInfo | null {
  try {
    const raw = localStorage.getItem(USER_KEY)
    return raw ? (JSON.parse(raw) as UserInfo) : null
  } catch {
    return null
  }
}

const currentUser = ref<UserInfo | null>(readStoredUser())
const authVisible = ref(false)

const stats = ref<DashboardStats>({
  totalCount: 0,
  completedCount: 0,
  inProgressCount: 0,
  todayCompletedCount: 0,
  completionRate: 0,
  cheerQuote: ''
})

const categoryStats = ref<CategoryStat[]>([])

const query = reactive<TaskQueryParams>({
  keyword: '',
  category: '',
  status: '',
  pageNum: 1,
  pageSize: 10,
  priority: ''
})

const records = ref<CheerTask[]>([])
const total = ref(0)
const loading = ref(false)

const dialogVisible = ref(false)
const submitting = ref(false)
const editingTask = ref<CheerTask | null>(null)

async function loadAll() {
  loading.value = true
  try {
    const [statsData, catStatsData, pageData] = await Promise.all([
      taskApi.getStats(),
      taskApi.getCategoryStats(),
      taskApi.getPage(query)
    ])
    stats.value = statsData
    categoryStats.value = catStatsData
    records.value = pageData.records
    total.value = pageData.total
  } catch {
    // 若未登录触发 401，request 拦截器已自动弹出登录弹窗
  } finally {
    loading.value = false
  }
}

function handleAuthSuccess(res: LoginResult) {
  localStorage.setItem(TOKEN_KEY, res.token)
  localStorage.setItem(USER_KEY, JSON.stringify(res.user))
  currentUser.value = res.user
  authVisible.value = false
  query.pageNum = 1
  loadAll()
}

function handleLogout() {
  localStorage.removeItem(TOKEN_KEY)
  localStorage.removeItem(USER_KEY)
  currentUser.value = null
  records.value = []
  categoryStats.value = []
  total.value = 0
  authVisible.value = true
  ElMessage.info('已退出登录')
}

function onUnauthorized() {
  currentUser.value = null
  authVisible.value = true
}

function handleSelectCategory(category: string) {
  query.category = category
  handleSearch()
}

function handleSearch() {
  query.pageNum = 1
  loadAll()
}

function handleReset() {
  query.keyword = ''
  query.category = ''
  query.status = ''
  query.priority = ''
  query.pageNum = 1
  loadAll()
}

function openCreate() {
  editingTask.value = null
  dialogVisible.value = true
}

function openEdit(row: CheerTask) {
  editingTask.value = row
  dialogVisible.value = true
}

async function handleSave(payload: TaskSavePayload) {
  submitting.value = true
  try {
    if (editingTask.value) {
      await taskApi.update(editingTask.value.id, payload)
      ElMessage.success('更新成功')
    } else {
      await taskApi.create(payload)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    await loadAll()
  } finally {
    submitting.value = false
  }
}

async function handleQuickCheer(row: CheerTask) {
  try {
    const { value } = await ElMessageBox.prompt(
      '写一句完成打卡的收获或自我鼓励吧（可留空直接确认）：',
      '完成打卡',
      {
        confirmButtonText: '确认打卡',
        cancelButtonText: '取消',
        inputValue: row.cheerNote || '今日目标达成，继续加油！'
      }
    )
    await taskApi.patchStatus(row.id, 2, value)
    ElMessage.success('打卡成功！')
    await loadAll()
  } catch {
    // 用户取消弹窗
  }
}

async function handleDelete(row: CheerTask) {
  try {
    await ElMessageBox.confirm(`确定删除「${row.title}」吗？`, '删除确认', {
      type: 'warning',
      confirmButtonText: '删除',
      cancelButtonText: '取消'
    })
    await taskApi.remove(row.id)
    ElMessage.success('已删除')
    await loadAll()
  } catch {
    // 用户取消删除
  }
}

async function handleBatchDelete(ids: number[]) {
  try {
    await ElMessageBox.confirm(`确定批量删除已勾选的 ${ids.length} 项任务吗？`, '批量删除确认', {
      type: 'warning',
      confirmButtonText: '确认删除',
      cancelButtonText: '取消'
    })
    await taskApi.batchRemove(ids)
    ElMessage.success(`已成功删除 ${ids.length} 项任务`)
    await loadAll()
  } catch {
    // 用户取消删除
  }
}

onMounted(() => {
  window.addEventListener('auth:unauthorized', onUnauthorized)
  loadAll()
})

onBeforeUnmount(() => {
  window.removeEventListener('auth:unauthorized', onUnauthorized)
})
</script>

<template>
  <div class="page-shell">
    <div class="main-container">
      <StatsHeader
        :stats="stats"
        :category-stats="categoryStats"
        :active-category="query.category"
        :current-user="currentUser"
        @select-category="handleSelectCategory"
        @open-login="authVisible = true"
        @logout="handleLogout"
      />

      <TaskFilterBar
        v-model:query="query"
        @search="handleSearch"
        @reset="handleReset"
        @create="openCreate"
      />

      <TaskTable
        :records="records"
        :total="total"
        :loading="loading"
        v-model:page-num="query.pageNum"
        v-model:page-size="query.pageSize"
        @page-change="loadAll"
        @edit="openEdit"
        @remove="handleDelete"
        @batch-remove="handleBatchDelete"
        @quick-cheer="handleQuickCheer"
      />

      <TaskFormDialog
        v-model:visible="dialogVisible"
        :editing-task="editingTask"
        :submitting="submitting"
        @submit="handleSave"
      />

      <AuthDialog v-model:visible="authVisible" @success="handleAuthSuccess" />
    </div>
  </div>
</template>

<style>
body {
  margin: 0;
  background-color: #f8fafc;
  font-family:
    -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'PingFang SC', 'Microsoft YaHei',
    sans-serif;
}
.page-shell {
  min-height: 100vh;
  padding: 24px 16px;
}
.main-container {
  max-width: 1120px;
  margin: 0 auto;
}
</style>
