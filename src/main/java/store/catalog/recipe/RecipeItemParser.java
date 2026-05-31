package store.catalog.recipe;

public class RecipeItemParser {

    public static RecipeItemOut to(RecipeItem i) {

        return i == null ? null :
            RecipeItemOut.builder()
                .id(i.id())
                .culinaryItemId(i.culinaryItemId())
                .culinaryItemName(i.culinaryItemName())
                .quantity(i.quantity())
                .measurementUnit(i.measurementUnit())
                .build();
    }

    public static RecipeItem to(RecipeItemIn in) {

        return in == null ? null :
            RecipeItem.builder()
                .culinaryItemId(in.culinaryItemId())
                .quantity(in.quantity())
                .measurementUnit(in.measurementUnit())
                .build();
    }

}
