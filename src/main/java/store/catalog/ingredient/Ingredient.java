package store.catalog.ingredient;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import store.catalog.culinaryitem.MeasurementConversion;
import store.catalog.culinaryitem.Nutrition;

@Data
@Builder @Accessors(chain = true, fluent = true)
public class Ingredient {

    private String id;
    private String name;
    private String category;
    private String iconType;
    
    private Nutrition nutritionValues;
    private List<MeasurementConversion> measurementConversions;

}