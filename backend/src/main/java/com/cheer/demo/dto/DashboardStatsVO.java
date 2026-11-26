package com.cheer.demo.dto;

/**
 * 顶部成长统计看板视图对象（VO = View Object）
 * （对应前端 types/task.ts 中的 DashboardStats 以及 StatsHeader.vue 组件所需的数据）
 *
 * 什么是 VO？
 * 数据库里并没有一张叫 dashboard_stats 的表，这些字段是由 Service 层执行多条 COUNT 统计并计算百分比后，
 * 专门按照前端 UI 展示的需要组装出来的返回对象。
 */
public class DashboardStatsVO {

    /** 全部待办任务总数量 */
    private Long totalCount;

    /** 累计已完成打卡（status = 2）的任务数量 */
    private Long completedCount;

    /** 当前进行中（status = 1）的任务数量 */
    private Long inProgressCount;

    /** 今日（当天 00:00 ~ 23:59）完成打卡的任务数量 */
    private Long todayCompletedCount;

    /** 总完成率百分比（0 ~ 100 的整数，供前端 el-progress 仪表盘直接使用） */
    private Integer completionRate;

    /** 根据当前完成进度动态生成的激励提示语 */
    private String cheerQuote;

    // ==================== 标准 Getter / Setter 方法 ====================

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public Long getCompletedCount() {
        return completedCount;
    }

    public void setCompletedCount(Long completedCount) {
        this.completedCount = completedCount;
    }

    public Long getInProgressCount() {
        return inProgressCount;
    }

    public void setInProgressCount(Long inProgressCount) {
        this.inProgressCount = inProgressCount;
    }

    public Long getTodayCompletedCount() {
        return todayCompletedCount;
    }

    public void setTodayCompletedCount(Long todayCompletedCount) {
        this.todayCompletedCount = todayCompletedCount;
    }

    public Integer getCompletionRate() {
        return completionRate;
    }

    public void setCompletionRate(Integer completionRate) {
        this.completionRate = completionRate;
    }

    public String getCheerQuote() {
        return cheerQuote;
    }

    public void setCheerQuote(String cheerQuote) {
        this.cheerQuote = cheerQuote;
    }
}
