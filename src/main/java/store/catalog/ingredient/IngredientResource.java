package store.catalog.ingredient;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IngredientResource implements IngredientController {

    @Autowired
    private IngredientService ingredientService;

    @Override
    public ResponseEntity<IngredientOut> createIngredient(IngredientIn in) {

        Ingredient created = ingredientService.create(IngredientParser.to(in));
        return ResponseEntity.ok(IngredientParser.to(created));
    }

    @Override
    public ResponseEntity<IngredientOut> updateIngredient(String id, IngredientIn in) {

        Ingredient updated = ingredientService.update(id, IngredientParser.to(in));
        return ResponseEntity.ok(IngredientParser.to(updated));
    }

    @Override
    public ResponseEntity<Void> deleteIngredient(String id) {

        ingredientService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<IngredientOut>> findAllIngredients() {

        return ResponseEntity.ok(
            IngredientParser.to(ingredientService.findAll())
        );
    }

    @Override
    public ResponseEntity<IngredientOut> findIngredientById(String id) {

        Ingredient out = ingredientService.findById(id);
        return out == null ?
            ResponseEntity.notFound().build() :
            ResponseEntity.ok(IngredientParser.to(out));
    }

}
