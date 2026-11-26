<script setup lang="ts">
import { ref } from 'vue'
import { Check, Edit, Delete } from '@element-plus/icons-vue'
import { getCategoryMeta, getPriorityMeta, getStatusMeta } from '../constants/taskMeta'
import type { CheerTask } from '../types/task'

defineProps<{
  records: CheerTask[]
  total: number
  loading: boolean
}>()

const pageNum = defineModel<number>('pageNum', { required: true })
const pageSize = defineModel<number>('pageSize', { required: true })

const emit = defineEmits<{
  pageChange: []
  edit: [row: CheerTask]
  remove: [row: CheerTask]
  batchRemove: [ids: number[]]
  quickCheer: [row: CheerTask]
}>()

const selectedIds = ref<number[]>([])

function handleSelectionChange(rows: CheerTask[]) {
  selectedIds.value = rows.map((item) => item.id)
}
</script>

<template>
  <el-card shadow="never" class="table-card">
    <el-table
      :data="records"
      v-loading="loading"
      stripe
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />

      <el-table-column prop="title" label="目标与描述" min-width="260">
        <template #default="{ row }">
          <div :class="['task-title', { done: row.status === 2 }]">{{ row.title }}</div>
          <div v-if="row.description" class="task-desc">{{ row.description }}</div>
          <div v-if="row.cheerNote" class="cheer-badge">打卡记录：{{ row.cheerNote }}</div>
        </template>
      </el-table-column>

      <el-table-column label="分类" width="110" align="center">
        <template #default="{ row }">
          <el-tag :type="getCategoryMeta(row.category).tagType" effect="light">
            {{ getCategoryMeta(row.category).label }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column label="优先级" width="95" align="center">
        <template #default="{ row }">
          <el-tag :type="getPriorityMeta(row.priority).tagType" size="small">
            {{ getPriorityMeta(row.priority).label }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column label="状态" width="105" align="center">
        <template #default="{ row }">
          <el-tag :type="getStatusMeta(row.status).tagType" effect="plain">
            {{ getStatusMeta(row.status).label }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="updatedAt" label="最近更新" width="170" align="center" />

      <el-table-column label="操作" width="230" fixed="right" align="center">
        <template #default="{ row }">
          <el-button
            v-if="row.status !== 2"
            type="success"
            size="small"
            plain
            :icon="Check"
            @click="emit('quickCheer', row)"
          >
            打卡
          </el-button>
          <el-button type="primary" size="small" link :icon="Edit" @click="emit('edit', row)">
            编辑
          </el-button>
          <el-button type="danger" size="small" link :icon="Delete" @click="emit('remove', row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pager-wrap">
      <el-button
        type="danger"
        plain
        :icon="Delete"
        :disabled="selectedIds.length === 0"
        @click="emit('batchRemove', selectedIds)"
      >
        批量删除{{ selectedIds.length > 0 ? ` (${selectedIds.length})` : '' }}
      </el-button>

      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :page-sizes="[5, 10, 20]"
        layout="total, sizes, prev, pager, next"
        :total="total"
        @size-change="emit('pageChange')"
        @current-change="emit('pageChange')"
      />
    </div>
  </el-card>
</template>

<style scoped>
.table-card {
  border-radius: 10px;
}
.task-title {
  font-weight: 600;
  color: #1e293b;
}
.task-title.done {
  text-decoration: line-through;
  color: #94a3b8;
}
.task-desc {
  margin-top: 4px;
  font-size: 12px;
  color: #64748b;
}
.cheer-badge {
  margin-top: 6px;
  display: inline-block;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
  background: #f0fdf4;
  color: #15803d;
  border: 1px solid #bbf7d0;
}
.pager-wrap {
  margin-top: 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
