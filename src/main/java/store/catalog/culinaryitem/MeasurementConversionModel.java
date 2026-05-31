package store.catalog.culinaryitem;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Table(name = "measurement_conversions")
@Getter @Setter @Accessors(chain = true, fluent = true)
@NoArgsConstructor @AllArgsConstructor
public class MeasurementConversionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @Column(name = "from_unit")
    private String fromUnit;

    @Column(name = "to_unit")
    private String toUnit;

    @Column(name = "factor")
    private BigDecimal factor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "culinary_item_id", nullable = false)
    private CulinaryItemModel culinaryItem;

    public MeasurementConversionModel(MeasurementConversion m) {
        this.id = m.id();
        this.fromUnit = m.fromUnit();
        this.toUnit = m.toUnit();
        this.factor = m.factor();
    }
    
    public MeasurementConversion to() {
        return MeasurementConversion.builder()
            .id(this.id)
            .fromUnit(this.fromUnit)
            .toUnit(this.toUnit)
            .factor(this.factor)
            .build();
    }

}
