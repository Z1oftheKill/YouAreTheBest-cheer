package com.cheer.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * 数据库表实体类（Entity）—— 与 MySQL 中的 cheer_task 表字段 1:1 严格映射
 * （对应前端 types/task.ts 中的 interface CheerTask）
 *
 * 注解说明：
 * - @TableName("cheer_task")：告诉 MyBatis-Plus 这个 Java 类对应数据库里的 cheer_task 表
 */
@TableName("cheer_task")
public class CheerTask {

    /**
     * 主键 ID（使用包装类 Long 对应 MySQL 的 BIGINT）
     * 
     * @TableId(type = IdType.AUTO)：声明这是表的主键，且插入数据时使用 MySQL 自增（AUTO_INCREMENT）
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 任务所属用户 ID（关联 sys_user.id，用于多用户数据隔离） */
    private Long userId;

    /** 任务/打卡标题（对应表中 title 列，VARCHAR(100)） */
    private String title;

    /** 任务详细描述或学习笔记（对应表中 description 列，VARCHAR(500)） */
    private String description;

    /** 任务所属分类：STUDY-学习进阶 / WORK-工作交付 / LIFE-生活习惯 / FITNESS-运动健康 */
    private String category;

    /** 任务优先级：1-普通 / 2-重要 / 3-紧急（使用包装类 Integer 对应 TINYINT） */
    private Integer priority;

    /** 当前状态：0-待开始 / 1-进行中 / 2-已打卡完成 */
    private Integer status;

    /**
     * 打卡鼓励语 / 完成感悟
     * 注意：数据库中列名叫 cheer_note（下划线），Java 属性名叫 cheerNote（小驼峰），
     * 因为 application.yml 开启了 map-underscore-to-camel-case: true，MyBatis-Plus
     * 会自动完成映射！
     */
    private String cheerNote;

    /**
     * 完成打卡时间（未完成时为 null）
     * 
     * @JsonFormat：告诉 Jackson 在把对象转成 JSON 发给前端时，按 "yyyy-MM-dd HH:mm:ss" 格式化，去掉中间的
     *                'T'
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime completedAt;

    /** 记录创建时间（由数据库 DEFAULT CURRENT_TIMESTAMP 自动生成） */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    /** 记录最后更新时间（由数据库 ON UPDATE CURRENT_TIMESTAMP 自动维护） */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    // ==================== 标准 Getter / Setter 方法 ====================
    // （Java 面向对象封装规范：属性设为 private 私有，对外通过 public getXxx / setXxx 方法读写）

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCheerNote() {
        return cheerNote;
    }

    public void setCheerNote(String cheerNote) {
        this.cheerNote = cheerNote;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
