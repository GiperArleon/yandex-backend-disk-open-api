CREATE TABLE IF NOT EXISTS history (
    id VARCHAR(100) NOT NULL,
    file_url VARCHAR(255),
    update_time TIMESTAMP WITH TIME ZONE NOT NULL,
    parent_id VARCHAR(100),
    item_type ITEM_TYPE NOT NULL,
    item_size bigint,
    PRIMARY KEY (update_time,id),
    FOREIGN KEY (id) REFERENCES items(id) ON DELETE CASCADE
);
