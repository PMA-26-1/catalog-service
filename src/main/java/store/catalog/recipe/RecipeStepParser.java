package store.catalog.recipe;

import java.util.List;

public class RecipeStepParser {

    public static RecipeStepOut to(RecipeStep s) {

        return s == null ? null :
            RecipeStepOut.builder()
                .id(s.id())
                .procedureId(s.procedureId())
                .procedureName(s.procedureName())
                .estimatedTimeMinutes(s.estimatedTimeMinutes())
                .passiveTimeMinutes(s.passiveTimeMinutes())
                .items(
                    s.items() == null ? List.of() :
                        s.items().stream().map(RecipeItemParser::to).toList()
                )
                .prerequisiteStepsIds(
                    s.prerequisiteStepsIds() == null ? List.of() :
                        s.prerequisiteStepsIds()
                )
                .build();
    }

    public static RecipeStep to(RecipeStepIn in) {

        return in == null ? null :
            RecipeStep.builder()
                .id(in.id())
                .procedureId(in.procedureId())
                .estimatedTimeMinutes(in.estimatedTimeMinutes())
                .passiveTimeMinutes(in.passiveTimeMinutes())
                .items(
                    in.items() == null ? List.of() :
                        in.items().stream().map(RecipeItemParser::to).toList()
                )
                .prerequisiteStepsIds(
                    in.prerequisiteStepsIds() == null ? List.of() :
                        in.prerequisiteStepsIds()
                )
                .build();
    }

}
