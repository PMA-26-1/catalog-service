package store.catalog.recipe;

import java.util.List;

import store.catalog.culinaryitem.MeasurementConversionParser;
import store.catalog.culinaryitem.NutritionParser;

public class RecipeParser {

    public static RecipeOut to(Recipe r) {

        return r == null ? null :
            RecipeOut.builder()
                .id(r.id())
                .name(r.name())
                .category(r.category())
                .iconType(r.iconType())
                .nutritionValues(NutritionParser.to(r.nutritionValues()))
                .measurementConversions(
                    MeasurementConversionParser.to(r.measurementConversions())
                )
                .recipeItems(
                    r.recipeItems() == null ? List.of() :
                        r.recipeItems().stream().map(RecipeItemParser::to).toList()
                )
                .recipeSteps(
                    r.recipeSteps() == null ? List.of() :
                        r.recipeSteps().stream().map(RecipeStepParser::to).toList()
                )
                .servings(r.servings())
                .difficulty(r.difficulty())
                .minTimeMinutes(r.minTimeMinutes())
                .totalWorkMinutes(r.totalWorkMinutes())
                .accountId(r.accountId())
                .build();
    }

    public static List<RecipeOut> to(List<Recipe> l) {

        return l.stream().map(RecipeParser::to).toList();
    }

    public static Recipe to(RecipeIn in) {

        return in == null ? null :
            Recipe.builder()
                .name(in.name())
                .category(in.category())
                .iconType(in.iconType())
                .measurementConversions(
                    in.measurementConversions() == null ? List.of() :
                        in.measurementConversions().stream()
                            .map(MeasurementConversionParser::to)
                            .toList()
                )
                .recipeItems(
                    in.recipeItems() == null ? List.of() :
                        in.recipeItems().stream().map(RecipeItemParser::to).toList()
                )
                .recipeSteps(
                    in.recipeSteps() == null ? List.of() :
                        in.recipeSteps().stream().map(RecipeStepParser::to).toList()
                )
                .servings(in.servings())
                .difficulty(in.difficulty())
                .accountId(in.accountId())
                .build();
    }

}
