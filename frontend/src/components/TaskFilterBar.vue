<script setup lang="ts">
import { Search, Plus, Refresh } from '@element-plus/icons-vue'
import { CATEGORY_OPTIONS, STATUS_OPTIONS, PRIORITY_OPTIONS } from '../constants/taskMeta'
import type { TaskQueryParams } from '../types/task'

const query = defineModel<TaskQueryParams>('query', { required: true })

const emit = defineEmits<{
  search: []
  reset: []
  create: []
}>()
</script>

<template>
  <el-card shadow="never" class="filter-card">
    <div class="filter-bar">
      <div class="filter-left">
        <el-input
          v-model="query.keyword"
          placeholder="搜索任务标题或笔记关键词..."
          clearable
          style="width: 240px"
          @keyup.enter="emit('search')"
          @clear="emit('search')"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>

        <el-select
          v-model="query.category"
          placeholder="全部分类"
          clearable
          style="width: 140px"
          @change="emit('search')"
        >
          <el-option
            v-for="item in CATEGORY_OPTIONS"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>

        <el-select
          v-model="query.status"
          placeholder="全部状态"
          clearable
          style="width: 140px"
          @change="emit('search')"
        >
          <el-option
            v-for="item in STATUS_OPTIONS"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
        <el-select
          v-model="query.priority"
          placeholder="全部优先级"
          clearable
          style="width: 140px"
          @change="emit('search')"
        >
          <el-option
            v-for="item in PRIORITY_OPTIONS"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>

        <el-button type="primary" plain :icon="Search" @click="emit('search')">查询</el-button>
        <el-button :icon="Refresh" @click="emit('reset')">重置</el-button>
      </div>

      <el-button type="primary" :icon="Plus" @click="emit('create')">新建目标</el-button>
    </div>
  </el-card>
</template>

<style scoped>
.filter-card {
  margin-bottom: 16px;
  border-radius: 10px;
}
.filter-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 12px;
}
.filter-left {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  align-items: center;
}
</style>
