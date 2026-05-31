CREATE TABLE catalog.culinary_items (
    id           VARCHAR(36) PRIMARY KEY,
    item_type    VARCHAR(32) NOT NULL,
    name         VARCHAR(255) NOT NULL,
    category     VARCHAR(128),
    icon_type    VARCHAR(64),
    nutrition_id VARCHAR(36) REFERENCES catalog.nutrition(id) ON DELETE SET NULL
);

CREATE INDEX idx_culinary_items_item_type ON catalog.culinary_items(item_type);
CREATE INDEX idx_culinary_items_name ON catalog.culinary_items(name);
