package store.catalog.recipe;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import store.catalog.procedure.ProcedureModel;

@Entity
@Table(name = "recipe_steps")
@Getter @Setter @Accessors(chain = true, fluent = true)
@NoArgsConstructor @AllArgsConstructor
public class RecipeStepModel {

    // ID is client-supplied: the parser either accepts the
    // client's ID or generates one in advance, so prerequisite references can be
    // wired up before persistence.

    @Id
    @Column(name = "id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private RecipeModel recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "procedure_id", nullable = false)
    private ProcedureModel procedure;

    @Column(name = "estimated_time_minutes")
    private Integer estimatedTimeMinutes;

    @Column(name = "passive_time_minutes")
    private Integer passiveTimeMinutes;

    @OneToMany(
        mappedBy = "step",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<RecipeItemModel> items = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "recipe_step_prerequisites",
        joinColumns = @JoinColumn(name = "step_id"),
        inverseJoinColumns = @JoinColumn(name = "prerequisite_step_id")
    )
    private List<RecipeStepModel> prerequisites = new ArrayList<>();

    public RecipeStep to() {
        return RecipeStep.builder()
            .id(this.id)
            .procedureId(this.procedure == null ? null : this.procedure.id())
            .procedureName(this.procedure == null ? null : this.procedure.name())
            .estimatedTimeMinutes(this.estimatedTimeMinutes)
            .passiveTimeMinutes(this.passiveTimeMinutes)
            .items(
                this.items == null ? List.of() :
                    this.items.stream().map(RecipeItemModel::to).toList()
            )
            .prerequisiteStepsIds(
                this.prerequisites == null ? List.of() :
                    this.prerequisites.stream().map(RecipeStepModel::id).toList()
            )
            .build();
    }

}
