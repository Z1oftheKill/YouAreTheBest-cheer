<script setup lang="ts">
import { User } from '@element-plus/icons-vue'
import { getCategoryMeta } from '../constants/taskMeta'
import type { UserInfo } from '../types/auth'
import type { CategoryStat, DashboardStats } from '../types/task'

defineProps<{
  stats: DashboardStats
  categoryStats: CategoryStat[]
  activeCategory?: string
  currentUser: UserInfo | null
}>()

const emit = defineEmits<{
  selectCategory: [category: string]
  openLogin: []
  logout: []
}>()
</script>

<template>
  <div class="stats-panel">
    <div class="hero-banner">
      <div>
        <div class="title-row">
          <h1 class="hero-title">Cheer 成长待办看板</h1>
          <div class="user-bar">
            <template v-if="currentUser">
              <el-tag type="success" effect="plain" round>
                <el-icon style="vertical-align: -2px; margin-right: 4px"><User /></el-icon>
                {{ currentUser.nickname }} ({{ currentUser.username }})
              </el-tag>
              <el-button size="small" type="primary" link @click="emit('openLogin')">切换账号</el-button>
              <el-button size="small" type="danger" link @click="emit('logout')">退出</el-button>
            </template>
            <el-button v-else size="small" type="primary" plain @click="emit('openLogin')">
              登录 / 注册
            </el-button>
          </div>
        </div>
        <p class="hero-quote">{{ stats.cheerQuote || '正在加载成长打卡进度...' }}</p>
      </div>
      <div class="rate-box">
        <el-progress type="dashboard" :percentage="stats.completionRate" :width="84" />
        <span class="rate-label">总完成率</span>
      </div>
    </div>

    <el-row :gutter="16" class="stat-cards">
      <el-col :xs="12" :sm="6">
        <el-card shadow="hover">
          <div class="stat-title">全部待办</div>
          <div class="stat-num">{{ stats.totalCount }}</div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6">
        <el-card shadow="hover">
          <div class="stat-title">进行中</div>
          <div class="stat-num highlight-blue">{{ stats.inProgressCount }}</div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6">
        <el-card shadow="hover">
          <div class="stat-title">累计已打卡</div>
          <div class="stat-num highlight-green">{{ stats.completedCount }}</div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6">
        <el-card shadow="hover">
          <div class="stat-title">今日完成打卡</div>
          <div class="stat-num highlight-amber">{{ stats.todayCompletedCount }}</div>
        </el-card>
      </el-col>
    </el-row>

    <div v-if="categoryStats.length > 0" class="category-bar">
      <span class="category-bar-label">分类分布：</span>
      <el-tag
        v-for="item in categoryStats"
        :key="item.category"
        :type="getCategoryMeta(item.category).tagType"
        :effect="activeCategory === item.category ? 'dark' : 'light'"
        class="category-pill"
        @click="emit('selectCategory', activeCategory === item.category ? '' : item.category)"
      >
        {{ getCategoryMeta(item.category).label }} · {{ item.count }}
      </el-tag>
    </div>
  </div>
</template>

<style scoped>
.stats-panel {
  margin-bottom: 18px;
}
.hero-banner {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  margin-bottom: 16px;
  border-radius: 12px;
  background: linear-gradient(135deg, #eff6ff 0%, #eef2ff 100%);
  border: 1px solid #dbeafe;
  gap: 16px;
}
.title-row {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 12px;
  margin-bottom: 8px;
}
.hero-title {
  margin: 0;
  font-size: 22px;
  color: #1e293b;
}
.user-bar {
  display: flex;
  align-items: center;
  gap: 8px;
}
.hero-quote {
  margin: 0;
  color: #475569;
  font-size: 14px;
}
.rate-box {
  display: flex;
  flex-direction: column;
  align-items: center;
}
.rate-label {
  font-size: 12px;
  color: #64748b;
  margin-top: -4px;
}
.stat-title {
  font-size: 13px;
  color: #64748b;
  margin-bottom: 6px;
}
.stat-num {
  font-size: 26px;
  font-weight: 700;
  color: #0f172a;
}
.highlight-blue { color: #2563eb; }
.highlight-green { color: #16a34a; }
.highlight-amber { color: #d97706; }
.category-bar {
  margin-top: 12px;
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 8px;
}
.category-bar-label {
  font-size: 13px;
  color: #64748b;
}
.category-pill {
  cursor: pointer;
  user-select: none;
}
</style>
