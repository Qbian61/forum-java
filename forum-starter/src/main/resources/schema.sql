SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for forum_article_type
-- ----------------------------
DROP TABLE IF EXISTS `forum_article_type`;
CREATE TABLE `forum_article_type` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `audit_state` varchar(64) NOT NULL COMMENT '审核状态',
  `name` varchar(64) NOT NULL COMMENT '名称',
  `description` varchar(1024) NOT NULL COMMENT '描述',
  `ref_count` bigint(11) NOT NULL DEFAULT '0' COMMENT '引用统计',
  `scope` varchar(32) NOT NULL COMMENT '作用域',
  `creator_id` bigint(11) NOT NULL COMMENT '创建人',
  `is_delete` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '删除标识（0:未删除、1:已删除）',
  `create_at` datetime NOT NULL COMMENT '记录创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_name_state` (`name`,`audit_state`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章类别表';

-- ----------------------------
-- Table structure for forum_cache
-- ----------------------------
DROP TABLE IF EXISTS `forum_cache`;
CREATE TABLE `forum_cache` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `key` varchar(100) NOT NULL COMMENT '缓存键',
  `value` longtext NOT NULL COMMENT '缓存值',
  `type` varchar(64) NOT NULL COMMENT '业务类型',
  `is_delete` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '删除标识（0:未删除、1:已删除）',
  `create_at` datetime NOT NULL COMMENT '记录创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_key` (`key`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='缓存表';

-- ----------------------------
-- Table structure for forum_comment
-- ----------------------------
DROP TABLE IF EXISTS `forum_comment`;
CREATE TABLE `forum_comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(11) NOT NULL COMMENT '评论人ID',
  `reply_id` bigint(11) DEFAULT NULL COMMENT '被评论ID',
  `reply_reply_id` bigint(11) DEFAULT NULL COMMENT '二次被评论ID',
  `posts_id` bigint(11) NOT NULL COMMENT '帖子ID',
  `content` longtext NOT NULL COMMENT '内容',
  `is_delete` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标识（0:未删除、1:已删除）',
  `create_at` datetime NOT NULL COMMENT '记录创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_uid_pid` (`user_id`,`posts_id`),
  KEY `idx_postsid` (`posts_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='帖子评论表';

-- ----------------------------
-- Table structure for forum_message
-- ----------------------------
DROP TABLE IF EXISTS `forum_message`;
CREATE TABLE `forum_message` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `channel` varchar(64) NOT NULL COMMENT '发送渠道',
  `type` varchar(64) NOT NULL COMMENT '消息类型',
  `read` varchar(64) NOT NULL COMMENT '是否已读',
  `sender_type` varchar(64) NOT NULL COMMENT '发送人类型',
  `sender` varchar(64) NOT NULL COMMENT '发送人',
  `receiver_type` varchar(64) NOT NULL COMMENT '接收人类型',
  `receiver` varchar(64) NOT NULL COMMENT '接收人',
  `title` varchar(256) NOT NULL COMMENT '标题',
  `content_type` varchar(64) NOT NULL COMMENT '发送内容类型',
  `content` longtext NOT NULL COMMENT '发送内容',
  `is_delete` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '删除标识（0:未删除、1:已删除）',
  `create_at` datetime NOT NULL COMMENT '记录创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_sender` (`sender`),
  KEY `idx_receiver_type` (`receiver`,`type`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息表';

-- ----------------------------
-- Table structure for forum_opt_log
-- ----------------------------
DROP TABLE IF EXISTS `forum_opt_log`;
CREATE TABLE `forum_opt_log` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type` varchar(64) NOT NULL COMMENT '操作类型',
  `operator_id` bigint(11) NOT NULL COMMENT '操作人ID',
  `content` longtext NOT NULL COMMENT '操作内容',
  `is_delete` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '删除标识（0:未删除、1:已删除）',
  `create_at` datetime NOT NULL COMMENT '记录创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_operator_id` (`operator_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

-- ----------------------------
-- Table structure for forum_posts
-- ----------------------------
DROP TABLE IF EXISTS `forum_posts`;
CREATE TABLE `forum_posts` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `audit_state` varchar(64) NOT NULL COMMENT '审核状态',
  `category` varchar(64) NOT NULL COMMENT '类别',
  `author_id` bigint(11) NOT NULL COMMENT '作者ID',
  `title` varchar(256) NOT NULL COMMENT '标题',
  `content_type` varchar(64) NOT NULL COMMENT '内容类型',
  `markdown_content` longtext NOT NULL COMMENT 'markdown内容',
  `html_content` longtext NOT NULL COMMENT 'html内容',
  `views` bigint(11) NOT NULL DEFAULT '0' COMMENT '浏览量',
  `approvals` bigint(11) NOT NULL DEFAULT '0' COMMENT '点赞量/收藏量',
  `comments` bigint(11) NOT NULL DEFAULT '0' COMMENT '评论量',
  `type_id` bigint(11) NOT NULL DEFAULT '0' COMMENT '文章类型ID',
  `head_img` varchar(8192) NOT NULL DEFAULT '' COMMENT '文章头图',
  `official` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '官方',
  `top` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '置顶',
  `sort` int(4) NOT NULL DEFAULT '1000' COMMENT '排序',
  `marrow` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '精华',
  `comment_id` bigint(11) NOT NULL COMMENT '问答最佳答案ID',
  `is_delete` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '删除标识（0:未删除、1:已删除）',
  `create_at` datetime NOT NULL COMMENT '记录创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_author` (`author_id`),
  KEY `idx_category_state_views` (`category`,`audit_state`,`views`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='帖子表';

-- ----------------------------
-- Table structure for forum_search
-- ----------------------------
DROP TABLE IF EXISTS `forum_search`;
CREATE TABLE `forum_search` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type` varchar(64) NOT NULL COMMENT '类型',
  `entity_id` bigint(11) NOT NULL COMMENT '实体ID',
  `title` varchar(256) NOT NULL COMMENT '标题',
  `content` longtext NOT NULL COMMENT '内容',
  `is_delete` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '删除标识（0:未删除、1:已删除）',
  `create_at` datetime NOT NULL COMMENT '记录创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_type_title` (`type`,`title`(191)),
  KEY `idx_type_create` (`type`,`create_at`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='搜索内容表';

-- ----------------------------
-- Table structure for forum_tag
-- ----------------------------
DROP TABLE IF EXISTS `forum_tag`;
CREATE TABLE `forum_tag` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `audit_state` varchar(64) NOT NULL COMMENT '审核状态',
  `group_name` varchar(255) CHARACTER SET utf8mb4 NOT NULL COMMENT '所属分组',
  `name` varchar(64) NOT NULL COMMENT '名称',
  `description` varchar(1024) NOT NULL COMMENT '描述',
  `ref_count` bigint(11) NOT NULL DEFAULT '0' COMMENT '引用统计',
  `creator_id` bigint(11) NOT NULL COMMENT '创建人',
  `is_delete` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '删除标识（0:未删除、1:已删除）',
  `create_at` datetime NOT NULL COMMENT '记录创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_name_state` (`name`,`audit_state`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='标签表';

-- ----------------------------
-- Table structure for forum_tag_posts_mapping
-- ----------------------------
DROP TABLE IF EXISTS `forum_tag_posts_mapping`;
CREATE TABLE `forum_tag_posts_mapping` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tag_id` bigint(11) NOT NULL COMMENT '标签ID',
  `posts_id` bigint(11) NOT NULL COMMENT '帖子ID',
  `is_delete` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '删除标识（0:未删除、1:已删除）',
  `create_at` datetime NOT NULL COMMENT '记录创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_posts_tag` (`posts_id`,`tag_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='标签-帖子关系表';

-- ----------------------------
-- Table structure for forum_user
-- ----------------------------
DROP TABLE IF EXISTS `forum_user`;
CREATE TABLE `forum_user` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `email` varchar(64) NOT NULL COMMENT '邮箱',
  `nickname` varchar(64) NOT NULL COMMENT '昵称',
  `password` varchar(128) NOT NULL COMMENT '密码',
  `role` varchar(32) NOT NULL COMMENT '角色',
  `state` varchar(64) NOT NULL COMMENT '状态',
  `sex` varchar(32) NOT NULL COMMENT '性别',
  `avatar` varchar(256) NOT NULL DEFAULT '' COMMENT '头像',
  `signature` varchar(1024) NOT NULL DEFAULT '' COMMENT '个人简介',
  `last_login_time` datetime NOT NULL COMMENT '最后登录时间',
  `is_delete` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '删除标识（0:未删除、1:已删除）',
  `create_at` datetime NOT NULL COMMENT '记录创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ----------------------------
-- Table structure for forum_user_follow
-- ----------------------------
DROP TABLE IF EXISTS `forum_user_follow`;
CREATE TABLE `forum_user_follow` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `followed` bigint(11) NOT NULL COMMENT '被关注主体',
  `followed_type` varchar(64) NOT NULL COMMENT '被关注主体类型',
  `follower` bigint(11) NOT NULL COMMENT '关注人',
  `is_delete` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标识（0:未删除、1:已删除）',
  `create_at` datetime NOT NULL COMMENT '记录创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_followed_follower` (`followed`,`followed_type`,`follower`) USING BTREE,
  KEY `idx_follower` (`follower`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户关注表';

-- ----------------------------
-- Table structure for forum_user_food
-- ----------------------------
DROP TABLE IF EXISTS `forum_user_food`;
CREATE TABLE `forum_user_food` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(11) NOT NULL COMMENT '用户ID',
  `posts_id` bigint(11) NOT NULL COMMENT '帖子ID',
  `is_delete` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '删除标识（0:未删除、1:已删除）',
  `create_at` datetime NOT NULL COMMENT '记录创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_uid_pid` (`user_id`,`posts_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户关注表';

-- ----------------------------
-- Table structure for forum_config
-- ----------------------------
DROP TABLE IF EXISTS `forum_config`;
CREATE TABLE `forum_config` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `state` varchar(64) NOT NULL COMMENT '状态',
  `type` varchar(64) NOT NULL COMMENT '类型',
  `name` varchar(1024) NOT NULL COMMENT '名称',
  `content` longtext NOT NULL COMMENT '内容',
  `start_at` datetime NOT NULL COMMENT '开始时间',
  `end_at` datetime NOT NULL COMMENT '结束时间',
  `creator` bigint(11) NOT NULL COMMENT '创建人ID',

  `is_delete` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '删除标识（0:未删除、1:已删除）',
  `create_at` datetime NOT NULL COMMENT '记录创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='配置表';

ALTER TABLE forum_user ADD `source` varchar(64) default 'REGISTER' COMMENT '来源' AFTER `sex`;
ALTER TABLE forum_user ADD `ext` blob COMMENT '扩展信息' AFTER `signature`;

SET FOREIGN_KEY_CHECKS = 1;
