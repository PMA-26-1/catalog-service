package store.catalog.culinaryitem;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

// MeasurementConversion.java in same package
@Data @Builder @Accessors(chain = true, fluent = true)
public class MeasurementConversion {
    private String id;
    private String fromUnit;
    private String toUnit;
    private BigDecimal factor;
}
