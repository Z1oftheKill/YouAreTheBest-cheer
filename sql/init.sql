-- =====================================================================
-- YouAreTheBest-cheer 个人待办与成长打卡看板 - 数据库初始化脚本
-- 【前端视角对照】：相当于 Prisma 的 migration.sql，定义表结构与初始种子数据
-- =====================================================================

SET NAMES utf8mb4;

CREATE DATABASE IF NOT EXISTS `cheer_demo`
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE `cheer_demo`;

DROP TABLE IF EXISTS `cheer_task`;

CREATE TABLE `cheer_task` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` VARCHAR(100) NOT NULL COMMENT '待办/打卡标题',
  `description` VARCHAR(500) DEFAULT '' COMMENT '任务详情或学习笔记',
  `category` VARCHAR(30) NOT NULL DEFAULT 'STUDY' COMMENT '分类：STUDY-学习 WORK-工作 LIFE-生活 FITNESS-运动',
  `priority` TINYINT NOT NULL DEFAULT 2 COMMENT '优先级：1-普通 2-重要 3-紧急',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0-待开始 1-进行中 2-已完成打卡',
  `cheer_note` VARCHAR(200) DEFAULT '' COMMENT '打卡鼓励语/完成感悟',
  `completed_at` DATETIME DEFAULT NULL COMMENT '完成打卡时间',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_category_status` (`category`, `status`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='个人待办与成长打卡表';

-- 插入初始演示数据（贴合前端转学 Java Web 的学习路线）
INSERT INTO `cheer_task` (`title`, `description`, `category`, `priority`, `status`, `cheer_note`, `completed_at`) VALUES
('跑通第一个 Spring Boot 3 + Vue 3 全栈项目', '理解前端 Axios 请求如何通过 Vite 代理转发到 8080 端口的 Controller 方法。', 'STUDY', 3, 2, '万事开头难，全链路跑通的那一刻超有成就感！You are the best!', NOW()),
('搞懂 Controller -> Service -> Mapper 三层架构职责', '对比前端路由、业务逻辑层与 ORM 数据层，阅读代码注释理清数据流转。', 'STUDY', 3, 1, '', NULL),
('动手练习：阅读并修改 DTO 参数校验规则 (@Valid)', '尝试提交空标题触发 GlobalExceptionHandler，观察返回的统一 Result<T> 格式。', 'STUDY', 2, 0, '', NULL),
('整理本周前端组件化与后端接口联调复盘笔记', '对比 TypeScript interface 与 Java Entity / DTO 类的异同点。', 'WORK', 2, 2, '强类型前后端配合真是丝滑，少踩了 90% 的字段拼写坑！', NOW()),
('下班后慢跑 3 公里放松大脑', '写代码久坐后记得活动肩颈，保持充沛精力继续进阶全栈工程师。', 'FITNESS', 1, 0, '', NULL),
('早睡不熬夜，保持每天 1 小时高效学习节奏', '长期主义胜过突击冲刺，每天进步一点点。', 'LIFE', 2, 1, '', NULL);

