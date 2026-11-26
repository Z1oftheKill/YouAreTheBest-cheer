-- =====================================================================
-- 第四关 B：用户表与多用户任务隔离升级脚本
-- =====================================================================

SET NAMES utf8mb4;
USE `cheer_demo`;

-- 1. 创建系统用户表 sys_user
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户主键ID',
  `username` VARCHAR(50) NOT NULL COMMENT '登录用户名（唯一）',
  `password_hash` VARCHAR(128) NOT NULL COMMENT '加盐SHA-256密码哈希值',
  `nickname` VARCHAR(50) NOT NULL COMMENT '展示昵称',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统用户表';

-- 2. 预置两个演示账号（初始密码均为 123456，与 JwtUtils.hashPassword 算法完全一致）
INSERT INTO `sys_user` (`id`, `username`, `password_hash`, `nickname`) VALUES
(1, 'cheer', LOWER(SHA2(CONCAT('cheer-salt:', '123456'), 256)), 'Cheer全栈学员'),
(2, 'guest', LOWER(SHA2(CONCAT('cheer-salt:', '123456'), 256)), '二号体验官');

-- 3. 为 cheer_task 表增加 user_id 字段（已有数据默认归属给 id=1 的 cheer 账号）
DROP PROCEDURE IF EXISTS `upgrade_cheer_task_user_id`;
DELIMITER $$
CREATE PROCEDURE `upgrade_cheer_task_user_id`()
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = 'cheer_demo' AND TABLE_NAME = 'cheer_task' AND COLUMN_NAME = 'user_id'
  ) THEN
    ALTER TABLE `cheer_task`
      ADD COLUMN `user_id` BIGINT NOT NULL DEFAULT 1 COMMENT '所属用户ID' AFTER `id`,
      ADD KEY `idx_user_id` (`user_id`);
  END IF;
END$$
DELIMITER ;
CALL `upgrade_cheer_task_user_id`();
DROP PROCEDURE IF EXISTS `upgrade_cheer_task_user_id`;

-- 4. 为 2 号账号 guest 插入两条专属测试数据，方便切换账号直观对比数据隔离效果
DELETE FROM `cheer_task` WHERE `user_id` = 2;
INSERT INTO `cheer_task` (`user_id`, `title`, `description`, `category`, `priority`, `status`, `cheer_note`, `completed_at`) VALUES
(2, '【二号账号专属】体验多用户数据隔离效果', '切换到 cheer 账号将看不到这条记录，每个人拥有独立看板！', 'STUDY', 3, 1, '', NULL),
(2, '【二号账号专属】周末去公园骑行 10 公里', '测试不同用户之间的完成率统计互不干扰。', 'FITNESS', 2, 2, '骑行吹风太舒服啦！', NOW());

