CREATE TABLE catalog.recipe_steps (
    id                     VARCHAR(36) PRIMARY KEY,
    recipe_id              VARCHAR(36) NOT NULL REFERENCES catalog.recipes(id) ON DELETE CASCADE,
    procedure_id           VARCHAR(36) NOT NULL REFERENCES catalog.procedures(id),
    estimated_time_minutes INTEGER,
    passive_time_minutes   INTEGER
);

CREATE INDEX idx_recipe_steps_recipe_id ON catalog.recipe_steps(recipe_id);
CREATE INDEX idx_recipe_steps_procedure_id ON catalog.recipe_steps(procedure_id);
