CREATE TABLE task (
                      id               BIGSERIAL PRIMARY KEY,
                      creator_id       BIGINT,
                      assigned_user_id BIGINT,
                      status           VARCHAR(20),
                      create_date_time TIMESTAMP,
                      deadline_date    TIMESTAMP,
                      done_date_time   TIMESTAMP,
                      priority         VARCHAR(10)
);