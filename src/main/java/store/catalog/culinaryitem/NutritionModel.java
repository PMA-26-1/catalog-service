package store.catalog.culinaryitem;

import java.math.BigDecimal;
import java.util.Map;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Table(name = "nutrition")
@Getter @Setter @Accessors(chain = true, fluent = true)
@NoArgsConstructor @AllArgsConstructor
public class NutritionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @Column(name = "calories")
    private BigDecimal calories;

    @Column(name = "protein")
    private BigDecimal protein;

    @Column(name = "fat")
    private BigDecimal fat;

    @Column(name = "carbohydrate")
    private BigDecimal carbohydrate;

    @Column(name = "fiber")
    private BigDecimal fiber;

    @ElementCollection
    @CollectionTable(
        name = "nutrition_other",
        joinColumns = @JoinColumn(name = "nutrition_id")
    )
    @MapKeyColumn(name = "nutrient_name")
    @Column(name = "amount")
    private Map<String, BigDecimal> other;

    public NutritionModel(Nutrition n) {
        this.id = n.id();
        this.calories = n.calories();
        this.protein = n.protein();
        this.fat = n.fat();
        this.carbohydrate = n.carbohydrate();
        this.fiber = n.fiber();
        this.other = n.other();
    }

    public Nutrition to() {
        return Nutrition.builder()
            .id(this.id)
            .calories(this.calories)
            .protein(this.protein)
            .fat(this.fat)
            .carbohydrate(this.carbohydrate)
            .fiber(this.fiber)
            .other(this.other)
            .build();
    }

}
