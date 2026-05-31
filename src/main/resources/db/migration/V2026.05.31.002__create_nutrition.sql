CREATE TABLE catalog.nutrition (
    id           VARCHAR(36) PRIMARY KEY,
    calories     NUMERIC,
    protein      NUMERIC,
    fat          NUMERIC,
    carbohydrate NUMERIC,
    fiber        NUMERIC
);

CREATE TABLE catalog.nutrition_other (
    nutrition_id  VARCHAR(36) NOT NULL REFERENCES catalog.nutrition(id) ON DELETE CASCADE,
    nutrient_name VARCHAR(128) NOT NULL,
    amount        NUMERIC,
    PRIMARY KEY (nutrition_id, nutrient_name)
);
