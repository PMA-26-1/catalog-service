package store.catalog.ingredient;

import java.util.ArrayList;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import store.catalog.culinaryitem.CulinaryItemModel;
import store.catalog.culinaryitem.MeasurementConversionModel;
import store.catalog.culinaryitem.NutritionModel;

@Entity
@Table(name = "ingredients")
@DiscriminatorValue("INGREDIENT")
@PrimaryKeyJoinColumn(name = "id")
@Getter @Setter @Accessors(chain = true, fluent = true)
@NoArgsConstructor
public class IngredientModel extends CulinaryItemModel {

    // JOINED inheritance: this child table only carries the FK to culinary_items.id.
    // All ingredient-specific fields would go here in the future (inventory, price).

    public IngredientModel(Ingredient i) {
        this.id(i.id())
            .name(i.name())
            .category(i.category())
            .iconType(i.iconType())
            .nutrition(i.nutritionValues() == null ? null : new NutritionModel(i.nutritionValues()))
            .measurementConversions(new ArrayList<>());

        if (i.measurementConversions() != null) {
            i.measurementConversions().forEach(mc -> {
                MeasurementConversionModel m = new MeasurementConversionModel(mc);
                m.culinaryItem(this);
                this.measurementConversions().add(m);
            });
        }
    }

    public Ingredient to() {
        return Ingredient.builder()
            .id(this.id())
            .name(this.name())
            .category(this.category())
            .iconType(this.iconType())
            .nutritionValues(this.nutrition() == null ? null : this.nutrition().to())
            .measurementConversions(
                this.measurementConversions() == null ? null :
                    this.measurementConversions().stream()
                        .map(MeasurementConversionModel::to)
                        .toList()
            )
            .build();
    }

}
