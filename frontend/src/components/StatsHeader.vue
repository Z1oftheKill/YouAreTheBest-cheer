<script setup lang="ts">
import type { DashboardStats } from '../types/task'

defineProps<{
  stats: DashboardStats
}>()
</script>

<template>
  <div class="stats-panel">
    <div class="hero-banner">
      <div>
        <h1 class="hero-title">Cheer 成长待办看板</h1>
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
}
.hero-title {
  margin: 0 0 8px;
  font-size: 22px;
  color: #1e293b;
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
</style>

