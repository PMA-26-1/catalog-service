CREATE TABLE catalog.ingredients (
    id VARCHAR(36) PRIMARY KEY REFERENCES catalog.culinary_items(id) ON DELETE CASCADE
);
