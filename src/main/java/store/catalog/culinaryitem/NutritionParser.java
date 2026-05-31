package store.catalog.culinaryitem;

public class NutritionParser {

    public static NutritionOut to(Nutrition n) {

        return n == null ? null :
            NutritionOut.builder()
                .id(n.id())
                .calories(n.calories())
                .protein(n.protein())
                .fat(n.fat())
                .carbohydrate(n.carbohydrate())
                .fiber(n.fiber())
                .other(n.other())
                .build();
    }

    public static Nutrition to(NutritionIn in) {

        return in == null ? null :
            Nutrition.builder()
                .calories(in.calories())
                .protein(in.protein())
                .fat(in.fat())
                .carbohydrate(in.carbohydrate())
                .fiber(in.fiber())
                .other(in.other())
                .build();
    }

}
