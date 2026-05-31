CREATE TABLE catalog.recipe_step_prerequisites (
    step_id              VARCHAR(36) NOT NULL REFERENCES catalog.recipe_steps(id) ON DELETE CASCADE,
    prerequisite_step_id VARCHAR(36) NOT NULL REFERENCES catalog.recipe_steps(id) ON DELETE CASCADE,
    PRIMARY KEY (step_id, prerequisite_step_id),
    CONSTRAINT no_self_reference CHECK (step_id <> prerequisite_step_id)
);

CREATE INDEX idx_recipe_step_prerequisites_step_id
    ON catalog.recipe_step_prerequisites(step_id);
