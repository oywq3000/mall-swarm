use mall;
-- 创建 pms_search_history 表
CREATE TABLE sms_notice (
                            id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
                            title VARCHAR(255) DEFAULT NULL,
                            pic VARCHAR(255) DEFAULT NULL,
                            status INT DEFAULT NULL COMMENT '0->活动结束，1->活动进行中',
                            url VARCHAR(255) DEFAULT NULL,
                            startDate DATETIME DEFAULT NULL,
                            endDate DATETIME DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
