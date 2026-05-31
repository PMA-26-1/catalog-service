CREATE TABLE catalog.recipe_items (
    id               VARCHAR(36) PRIMARY KEY,
    culinary_item_id VARCHAR(36) NOT NULL REFERENCES catalog.culinary_items(id),
    quantity         NUMERIC,
    measurement_unit VARCHAR(64),
    recipe_id        VARCHAR(36) REFERENCES catalog.recipes(id) ON DELETE CASCADE,
    step_id          VARCHAR(36) REFERENCES catalog.recipe_steps(id) ON DELETE CASCADE,
    CONSTRAINT exactly_one_owner CHECK (
        (recipe_id IS NOT NULL AND step_id IS NULL) OR
        (recipe_id IS NULL AND step_id IS NOT NULL)
    )
);

CREATE INDEX idx_recipe_items_culinary_item_id ON catalog.recipe_items(culinary_item_id);
CREATE INDEX idx_recipe_items_recipe_id ON catalog.recipe_items(recipe_id);
CREATE INDEX idx_recipe_items_step_id ON catalog.recipe_items(step_id);
