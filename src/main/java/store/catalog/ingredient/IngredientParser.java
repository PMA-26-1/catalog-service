package store.catalog.ingredient;

import java.util.List;

import store.catalog.culinaryitem.MeasurementConversionParser;
import store.catalog.culinaryitem.NutritionParser;

public class IngredientParser {

    public static IngredientOut to(Ingredient i) {

        return i == null ? null :
            IngredientOut.builder()
                .id(i.id())
                .name(i.name())
                .category(i.category())
                .iconType(i.iconType())
                .nutritionValues(NutritionParser.to(i.nutritionValues()))
                .measurementConversions(
                    MeasurementConversionParser.to(i.measurementConversions())
                )
                .build();
    }

    public static List<IngredientOut> to(List<Ingredient> l) {

        return l.stream().map(IngredientParser::to).toList();
    }

    public static Ingredient to(IngredientIn in) {

        return in == null ? null :
            Ingredient.builder()
                .name(in.name())
                .category(in.category())
                .iconType(in.iconType())
                .nutritionValues(NutritionParser.to(in.nutritionValues()))
                .measurementConversions(
                    in.measurementConversions() == null ? List.of() :
                        in.measurementConversions().stream()
                            .map(MeasurementConversionParser::to)
                            .toList()
                )
                .build();
    }

}
