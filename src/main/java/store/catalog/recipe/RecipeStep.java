package store.catalog.recipe;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder @Accessors(chain = true, fluent = true)
public class RecipeStep {

    private String id;
    private String procedureId;
    private String procedureName;
    private Integer estimatedTimeMinutes;
    private Integer passiveTimeMinutes;
    private List<RecipeItem> items;
    private List<String> prerequisiteStepsIds;

}
