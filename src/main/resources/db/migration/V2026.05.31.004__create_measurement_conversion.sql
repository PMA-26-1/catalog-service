CREATE TABLE catalog.measurement_conversions (
    id               VARCHAR(36) PRIMARY KEY,
    from_unit        VARCHAR(64) NOT NULL,
    to_unit          VARCHAR(64) NOT NULL,
    factor           NUMERIC NOT NULL,
    culinary_item_id VARCHAR(36) NOT NULL REFERENCES catalog.culinary_items(id) ON DELETE CASCADE
);

CREATE INDEX idx_measurement_conversions_culinary_item_id
    ON catalog.measurement_conversions(culinary_item_id);
