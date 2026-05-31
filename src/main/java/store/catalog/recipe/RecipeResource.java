package store.catalog.recipe;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecipeResource implements RecipeController {

    @Autowired
    private RecipeService recipeService;

    @Override
    public ResponseEntity<RecipeOut> createRecipe(RecipeIn in) {

        Recipe created = recipeService.create(RecipeParser.to(in));
        return ResponseEntity.ok(RecipeParser.to(created));
    }

    @Override
    public ResponseEntity<RecipeOut> updateRecipe(String id, RecipeIn in) {

        Recipe updated = recipeService.update(id, RecipeParser.to(in));
        return ResponseEntity.ok(RecipeParser.to(updated));
    }

    @Override
    public ResponseEntity<Void> deleteRecipe(String id) {

        recipeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<RecipeOut>> findAllRecipes() {

        return ResponseEntity.ok(
            RecipeParser.to(recipeService.findAll())
        );
    }

    @Override
    public ResponseEntity<RecipeOut> findRecipeById(String id) {

        Recipe out = recipeService.findById(id);
        return out == null ?
            ResponseEntity.notFound().build() :
            ResponseEntity.ok(RecipeParser.to(out));
    }

    @Override
    public ResponseEntity<List<RecipeOut>> findRecipesByAccountId(String accountId) {

        return ResponseEntity.ok(
            RecipeParser.to(recipeService.findByAccountId(accountId))
        );
    }

}
