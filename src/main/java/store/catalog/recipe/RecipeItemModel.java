package store.catalog.recipe;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import store.catalog.culinaryitem.CulinaryItemModel;

@Entity
@Table(name = "recipe_items")
@Getter @Setter @Accessors(chain = true, fluent = true)
@NoArgsConstructor @AllArgsConstructor
public class RecipeItemModel {

    // Belongs to either a Recipe (recipe-level ingredient) OR a RecipeStep
    // (step-level ingredient). Exactly one of `recipe` / `step` is non-null,
    // enforced by a DB CHECK constraint and by RecipeService on save.

    @Id
    @Column(name = "id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "culinary_item_id", nullable = false)
    private CulinaryItemModel culinaryItem;

    @Column(name = "quantity")
    private BigDecimal quantity;

    @Column(name = "measurement_unit")
    private String measurementUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private RecipeModel recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "step_id")
    private RecipeStepModel step;

    public RecipeItem to() {
        return RecipeItem.builder()
            .id(this.id)
            .culinaryItemId(this.culinaryItem == null ? null : this.culinaryItem.id())
            .culinaryItemName(this.culinaryItem == null ? null : this.culinaryItem.name())
            .quantity(this.quantity)
            .measurementUnit(this.measurementUnit)
            .build();
    }

}
