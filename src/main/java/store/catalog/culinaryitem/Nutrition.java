package store.catalog.culinaryitem;

import java.math.BigDecimal;
import java.util.Map;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder @Accessors(chain = true, fluent = true)
public class Nutrition {

    private String id;
    private BigDecimal calories;
    private BigDecimal protein;
    private BigDecimal fat;
    private BigDecimal carbohydrate;
    private BigDecimal fiber;
    private Map<String, BigDecimal> other;

}
