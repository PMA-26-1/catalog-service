package store.catalog.culinaryitem;

import java.util.List;

public class MeasurementConversionParser {

    public static MeasurementConversionOut to(MeasurementConversion m) {

        return m == null ? null :
            MeasurementConversionOut.builder()
                .id(m.id())
                .fromUnit(m.fromUnit())
                .toUnit(m.toUnit())
                .factor(m.factor())
                .build();
    }

    public static List<MeasurementConversionOut> to(List<MeasurementConversion> l) {

        return l == null ? List.of() :
            l.stream().map(MeasurementConversionParser::to).toList();
    }

    public static MeasurementConversion to(MeasurementConversionIn in) {

        return in == null ? null :
            MeasurementConversion.builder()
                .fromUnit(in.fromUnit())
                .toUnit(in.toUnit())
                .factor(in.factor())
                .build();
    }

}
