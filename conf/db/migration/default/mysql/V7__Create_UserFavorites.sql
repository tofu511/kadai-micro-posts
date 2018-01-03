CREATE TABLE `favorites` (
    `id`            BIGINT          AUTO_INCREMENT,
    `user_id`       BIGINT          NOT NULL,
    `microPost_id`  BIGINT          NOT NULL,
    `create_at`     TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_at`     TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES users(`id`),
    FOREIGN KEY (`microPost_id`) REFERENCES micro_posts(`id`),
    UNIQUE(`user_id`, `microPost_id`)
) ENGINE=InnoDB;