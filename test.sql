use mall;
-- 创建 pms_search_history 表
CREATE TABLE pms_search_history (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键 ID',
                                    member_id BIGINT NOT NULL COMMENT '用户 ID',
                                    keyword VARCHAR(255) NOT NULL COMMENT '关键词',
                                    create_time DATETIME NOT NULL COMMENT '搜索时间',
    -- 设置外键约束，关联 ums_member 表的 id 字段，删除用户时级联删除搜索记录
                                    FOREIGN KEY (member_id) REFERENCES ums_member(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品搜索历史表';


