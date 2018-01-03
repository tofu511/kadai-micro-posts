CREATE TABLE favorites (
    id            BIGSERIAL       NOT NULL,
    user_id       BIGINT          NOT NULL,
    favorite_id   BIGINT          NOT NULL,
    create_at     TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_at     TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (favorite_id) REFERENCES micro_posts(id),
    UNIQUE(user_id, favorite_id)
);