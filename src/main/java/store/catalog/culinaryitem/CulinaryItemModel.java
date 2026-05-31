package store.catalog.culinaryitem;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Table(name = "culinary_items")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "item_type")
@Getter @Setter @Accessors(chain = true, fluent = true)
@NoArgsConstructor @AllArgsConstructor
public abstract class CulinaryItemModel {

    // Abstract parent of every culinary item (Ingredient, Recipe).
    // JOINED inheritance: shared fields live here, subtype-specific fields
    // live in child tables joined on id. The discriminator column "item_type"
    // identifies the concrete subtype without requiring a join.

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "category")
    private String category;

    @Column(name = "icon_type")
    private String iconType;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "nutrition_id")
    private NutritionModel nutrition;

    @OneToMany(
        mappedBy = "culinaryItem",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<MeasurementConversionModel> measurementConversions;

}
