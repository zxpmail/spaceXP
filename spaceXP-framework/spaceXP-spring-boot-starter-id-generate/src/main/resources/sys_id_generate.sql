/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 80037
 Source Host           : localhost:3306
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 80037
 File Encoding         : 65001

 Date: 10/03/2025 09:49:45
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_id_generate
-- ----------------------------
DROP TABLE IF EXISTS `sys_id_generate`;
CREATE TABLE `sys_id_generate`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `start` bigint NULL DEFAULT NULL COMMENT '当前id所在阶段的开始值',
  `end` bigint NULL DEFAULT NULL COMMENT '当前id所在阶段的结束值',
  `step` int NULL DEFAULT NULL COMMENT 'id递增区间',
  `ordered` tinyint NULL DEFAULT NULL COMMENT '0无序，1有序',
  `biz` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '业务信息',
  `version` int NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_id_generate
-- ----------------------------
INSERT INTO `sys_id_generate` VALUES (1, 2401, 2450, 50, 1, 'user_id', 49, '用户id生成策略', '2023-05-23 12:38:21', '2025-03-10 09:48:47');

SET FOREIGN_KEY_CHECKS = 1;
