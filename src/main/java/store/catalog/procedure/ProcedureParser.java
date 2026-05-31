package store.catalog.procedure;

import java.util.List;

public class ProcedureParser {

    public static ProcedureOut to(Procedure p) {

        return p == null ? null :
            ProcedureOut.builder()
                .id(p.id())
                .name(p.name())
                .build();
    }

    public static List<ProcedureOut> to(List<Procedure> l) {

        return l.stream().map(ProcedureParser::to).toList();
    }

    public static Procedure to(ProcedureIn in) {

        return in == null ? null :
            Procedure.builder()
                .name(in.name())
                .build();
    }

}
