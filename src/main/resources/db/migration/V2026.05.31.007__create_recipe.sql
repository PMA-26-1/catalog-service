CREATE TABLE catalog.recipes (
    id                  VARCHAR(36) PRIMARY KEY REFERENCES catalog.culinary_items(id) ON DELETE CASCADE,
    servings            INTEGER,
    difficulty          VARCHAR(16),
    min_time_minutes    INTEGER,
    total_work_minutes  INTEGER,
    account_id          VARCHAR(36)
);

CREATE INDEX idx_recipes_account_id ON catalog.recipes(account_id);
