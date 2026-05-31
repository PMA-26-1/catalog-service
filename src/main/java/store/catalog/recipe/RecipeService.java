package store.catalog.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import store.catalog.culinaryitem.CulinaryItemModel;
import store.catalog.culinaryitem.MeasurementConversionModel;
import store.catalog.culinaryitem.NutritionModel;
import store.catalog.ingredient.IngredientRepository;
import store.catalog.procedure.ProcedureModel;
import store.catalog.procedure.ProcedureRepository;

@Service
@Transactional
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private ProcedureRepository procedureRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    public Recipe create(Recipe r) {

        validateDag(r.recipeSteps());

        RecipeModel model = new RecipeModel();
        applyRecipeFields(model, r);

        computeTimeBounds(model);

        return modelTo(recipeRepository.save(model));
    }

    public Recipe update(String id, Recipe r) {

        RecipeModel model = recipeRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));

        validateDag(r.recipeSteps());

        model.recipeItems().clear();
        model.recipeSteps().clear();

        applyRecipeFields(model, r);
        computeTimeBounds(model);

        return modelTo(recipeRepository.save(model));
    }

    public void delete(String id) {
        recipeRepository.deleteById(id);
    }

    public Recipe findById(String id) {

        return recipeRepository.findById(id)
            .map(this::modelTo)
            .orElse(null);
    }

    public List<Recipe> findAll() {

        return StreamSupport.stream(
            recipeRepository.findAll().spliterator(),
            false
        ).map(this::modelTo).toList();
    }

    public List<Recipe> findByAccountId(String accountId) {

        return recipeRepository.findByAccountId(accountId).stream()
            .map(this::modelTo)
            .toList();
    }

    // === DAG validation ===

    private void validateDag(List<RecipeStep> steps) {

        if (steps == null || steps.isEmpty()) return;

        Set<String> stepIds = new HashSet<>();
        for (RecipeStep s : steps) {
            if (s.id() == null) continue;
            if (!stepIds.add(s.id())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duplicate step id: " + s.id());
            }
        }

        for (RecipeStep s : steps) {
            if (s.prerequisiteStepsIds() == null) continue;
            for (String prereq : s.prerequisiteStepsIds()) {
                if (prereq.equals(s.id())) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Step references itself: " + s.id());
                }
                if (!stepIds.contains(prereq)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown prerequisite: " + prereq);
                }
            }
        }

        Map<String, List<String>> adj = new HashMap<>();
        for (RecipeStep s : steps) {
            adj.put(s.id(), s.prerequisiteStepsIds() == null ? List.of() : s.prerequisiteStepsIds());
        }

        Set<String> visited = new HashSet<>();
        Set<String> stack = new HashSet<>();
        for (String id : adj.keySet()) {
            if (hasCycle(id, adj, visited, stack)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Recipe steps contain a cycle");
            }
        }
    }

    private boolean hasCycle(String node, Map<String, List<String>> adj,
                              Set<String> visited, Set<String> stack) {
        if (stack.contains(node)) return true;
        if (visited.contains(node)) return false;

        visited.add(node);
        stack.add(node);
        for (String next : adj.getOrDefault(node, List.of())) {
            if (hasCycle(next, adj, visited, stack)) return true;
        }
        stack.remove(node);
        return false;
    }

    // === Time bounds ===

    private void computeTimeBounds(RecipeModel model) {

        int total = model.recipeSteps().stream()
            .mapToInt(s -> nullSafe(s.estimatedTimeMinutes()))
            .sum();
        model.totalWorkMinutes(total);

        // Critical path: longest accumulated time along any chain through the DAG.
        Map<String, Integer> longest = new HashMap<>();
        Map<String, RecipeStepModel> byId = new HashMap<>();
        Map<String, List<String>> adj = new HashMap<>();

        for (RecipeStepModel s : model.recipeSteps()) {
            byId.put(s.id(), s);
            adj.put(
                s.id(),
                s.prerequisites().stream().map(RecipeStepModel::id).toList()
            );
        }

        int crit = 0;
        for (RecipeStepModel s : model.recipeSteps()) {
            crit = Math.max(crit, longestPath(s.id(), adj, byId, longest));
        }
        model.minTimeMinutes(crit);
    }

    private int longestPath(String id, Map<String, List<String>> adj,
                             Map<String, RecipeStepModel> byId, Map<String, Integer> memo) {

        if (memo.containsKey(id)) return memo.get(id);

        int self = nullSafe(byId.get(id).estimatedTimeMinutes());
        int best = 0;
        for (String prereq : adj.getOrDefault(id, List.of())) {
            best = Math.max(best, longestPath(prereq, adj, byId, memo));
        }
        int result = self + best;
        memo.put(id, result);
        return result;
    }

    private int nullSafe(Integer i) { return i == null ? 0 : i; }

    // === Entity assembly ===

    private void applyRecipeFields(RecipeModel model, Recipe r) {

        model.servings(r.servings())
             .difficulty(r.difficulty())
             .accountId(r.accountId());

        model.name(r.name())
             .category(r.category())
             .iconType(r.iconType())
             .nutrition(r.nutritionValues() == null ? null : new NutritionModel(r.nutritionValues()));

        // Measurement conversions (inherited from CulinaryItemModel)
        if (model.measurementConversions() == null) {
            model.measurementConversions(new ArrayList<>());
        } else {
            model.measurementConversions().clear();
        }
        if (r.measurementConversions() != null) {
            r.measurementConversions().forEach(mc -> {
                MeasurementConversionModel m = new MeasurementConversionModel(mc);
                m.culinaryItem(model);
                model.measurementConversions().add(m);
            });
        }

        // Recipe-level items (not attached to any step)
        if (r.recipeItems() != null) {
            r.recipeItems().forEach(item -> {
                RecipeItemModel m = buildRecipeItemModel(item);
                m.recipe(model);
                model.recipeItems().add(m);
            });
        }

        // Steps: first pass — create RecipeStepModel for each, no prereqs yet
        Map<String, RecipeStepModel> stepByClientId = new HashMap<>();
        if (r.recipeSteps() != null) {
            for (RecipeStep s : r.recipeSteps()) {
                String stepId = s.id() != null ? s.id() : UUID.randomUUID().toString();
                ProcedureModel proc = procedureRepository.findById(s.procedureId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown procedure: " + s.procedureId()));

                RecipeStepModel stepModel = new RecipeStepModel()
                    .id(stepId)
                    .recipe(model)
                    .procedure(proc)
                    .estimatedTimeMinutes(s.estimatedTimeMinutes())
                    .passiveTimeMinutes(s.passiveTimeMinutes())
                    .items(new ArrayList<>())
                    .prerequisites(new ArrayList<>());

                if (s.items() != null) {
                    s.items().forEach(item -> {
                        RecipeItemModel im = buildRecipeItemModel(item);
                        im.step(stepModel);
                        stepModel.items().add(im);
                    });
                }

                stepByClientId.put(stepId, stepModel);
                model.recipeSteps().add(stepModel);
            }

            // Second pass — wire up prerequisites
            for (RecipeStep s : r.recipeSteps()) {
                if (s.prerequisiteStepsIds() == null || s.prerequisiteStepsIds().isEmpty()) continue;
                RecipeStepModel target = stepByClientId.get(s.id());
                for (String prereqId : s.prerequisiteStepsIds()) {
                    RecipeStepModel prereq = stepByClientId.get(prereqId);
                    target.prerequisites().add(prereq);
                }
            }
        }
    }

    private RecipeItemModel buildRecipeItemModel(RecipeItem item) {

        CulinaryItemModel ci = ingredientRepository.findById(item.culinaryItemId())
            .map(ing -> (CulinaryItemModel) ing)
            .orElseGet(() -> recipeRepository.findById(item.culinaryItemId())
                .map(rec -> (CulinaryItemModel) rec)
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Unknown culinary item: " + item.culinaryItemId()
                ))
            );

        return new RecipeItemModel()
            .id(item.id() != null ? item.id() : UUID.randomUUID().toString())
            .culinaryItem(ci)
            .quantity(item.quantity())
            .measurementUnit(item.measurementUnit());
    }

    // === Model → Domain ===

    private Recipe modelTo(RecipeModel m) {

        return Recipe.builder()
            .id(m.id())
            .name(m.name())
            .category(m.category())
            .iconType(m.iconType())
            .nutritionValues(m.nutrition() == null ? null : m.nutrition().to())
            .measurementConversions(
                m.measurementConversions() == null ? List.of() :
                    m.measurementConversions().stream()
                        .map(MeasurementConversionModel::to)
                        .toList()
            )
            .recipeItems(
                m.recipeItems() == null ? List.of() :
                    m.recipeItems().stream().map(RecipeItemModel::to).toList()
            )
            .recipeSteps(
                m.recipeSteps() == null ? List.of() :
                    m.recipeSteps().stream().map(RecipeStepModel::to).toList()
            )
            .servings(m.servings())
            .difficulty(m.difficulty())
            .minTimeMinutes(m.minTimeMinutes())
            .totalWorkMinutes(m.totalWorkMinutes())
            .accountId(m.accountId())
            .build();
    }

}
