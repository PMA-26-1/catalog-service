package store.catalog.recipe;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder @Accessors(chain = true, fluent = true)
public class RecipeItem {

    private String id;
    private String culinaryItemId;
    private String culinaryItemName;
    private BigDecimal quantity;
    private String measurementUnit;

}
