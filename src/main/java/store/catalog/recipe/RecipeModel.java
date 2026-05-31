package store.catalog.recipe;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import store.catalog.culinaryitem.CulinaryItemModel;

@Entity
@Table(name = "recipes")
@DiscriminatorValue("RECIPE")
@PrimaryKeyJoinColumn(name = "id")
@Getter @Setter @Accessors(chain = true, fluent = true)
@NoArgsConstructor
public class RecipeModel extends CulinaryItemModel {

    @Column(name = "servings")
    private Integer servings;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty")
    private Difficulty difficulty;

    @Column(name = "min_time_minutes")
    private Integer minTimeMinutes;

    @Column(name = "total_work_minutes")
    private Integer totalWorkMinutes;

    @Column(name = "account_id")
    private String accountId;

    @OneToMany(
        mappedBy = "recipe",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<RecipeStepModel> recipeSteps = new ArrayList<>();

    @OneToMany(
        mappedBy = "recipe",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<RecipeItemModel> recipeItems = new ArrayList<>();

}
