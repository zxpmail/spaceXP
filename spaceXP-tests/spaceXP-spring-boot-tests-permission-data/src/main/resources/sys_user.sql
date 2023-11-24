/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50738
 Source Host           : localhost:3306
 Source Schema         : test1

 Target Server Type    : MySQL
 Target Server Version : 50738
 File Encoding         : 65001

 Date: 07/10/2023 14:30:13
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `sex` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `state` bigint(20) NULL DEFAULT NULL,
  `change_password_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `password_update_time` datetime(0) NULL DEFAULT NULL,
  `create_time` datetime(3) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `dept_id` bigint(20) NULL DEFAULT 1 COMMENT '部门ID',
  `del_flag` tinyint(1) NULL DEFAULT 0,
  `creator` bigint(20) NULL DEFAULT 1,
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '$2a$10$MN2b/7KQEf2a2D0WVy3YC.bXe94oftuC60A38a4XLZOHiy5CRSR/W', '系统管理员', '男', 0, 'Y', '2022-07-26 10:26:17', '2021-01-05 23:28:43.000', '2022-07-26 10:26:17', 1, 0, 1, 1);
INSERT INTO `sys_user` VALUES (2, 'test', '$2a$10$MN2b/7KQEf2a2D0WVy3YC.bXe94oftuC60A38a4XLZOHiy5CRSR/W', 'test', '女', 0, 'Y', '2023-01-16 10:31:51', '2023-01-16 10:31:56.568', '2023-01-16 10:32:00', 0, 0, 1, 0);
INSERT INTO `sys_user` VALUES (3, NULL, NULL, '', NULL, NULL, '', NULL, NULL, NULL, 0, 0, 1, 0);
INSERT INTO `sys_user` VALUES (4, NULL, NULL, '', NULL, NULL, '', NULL, NULL, NULL, 0, 0, 1, 0);

SET FOREIGN_KEY_CHECKS = 1;
