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

 Date: 08/07/2024 17:10:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` bigint(20) NOT NULL COMMENT '主键自增||dto||vo',
  `module` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模块名称||dto||vo||query',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '接口名称||dto||vo',
  `action_method` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '接口方法||dto||vo',
  `action_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '接口地址||dto||vo',
  `class_path` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '类路径||dto||vo',
  `request_method` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求方式||dto||vo',
  `ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求ip地址||dto||vo',
  `os` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '操作系统||dto||vo',
  `browser` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '浏览器||dto||vo',
  `params` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '请求参数||dto||vo',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间||dto||vo',
  `finish_time` datetime(0) NULL DEFAULT NULL COMMENT '完成时间||dto||vo',
  `request_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '请求编号||dto||vo',
  `response_data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '响应数据||dto||vo',
  `ex_detail` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '异常详情||dto||vo',
  `ex_desc` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '异常描述||dto||vo',
  `type` int(11) NULL DEFAULT NULL COMMENT '类型：1 操作记录 2异常记录||dto||vo||query',
  `dept_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '部门ID||DTO||VO||Query',
  `sub_system` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分系统标识',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户编号',
  `creator` bigint(20) NOT NULL COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期||vo||query||between',
  `user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名称',
  `consuming_time` bigint(20) NULL DEFAULT NULL COMMENT '消耗时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '执行日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_login_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log`  (
  `id` bigint(20) NOT NULL COMMENT '访问ID||VO',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
  `user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户账号||VO||Query||like',
  `ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录IP地址||VO||Query||like',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '浏览器类型||VO ',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作系统||VO ',
  `type` int(11) NULL DEFAULT NULL COMMENT '登录状态(1 操作记录 2异常记录）||VO||Query ',
  `msg` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '提示消息||DTO||VO ',
  `logined` int(11) NULL DEFAULT NULL COMMENT '是否登录0登录1退出||VO||Query ',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '访问时间||VO||Query||between',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统访问记录' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
