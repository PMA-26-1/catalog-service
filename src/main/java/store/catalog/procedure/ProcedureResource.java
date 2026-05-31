package store.catalog.procedure;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProcedureResource implements ProcedureController {

    @Autowired
    private ProcedureService procedureService;

    @Override
    public ResponseEntity<ProcedureOut> createProcedure(ProcedureIn in) {

        Procedure created = procedureService.create(ProcedureParser.to(in));
        return ResponseEntity.ok(ProcedureParser.to(created));
    }

    @Override
    public ResponseEntity<ProcedureOut> updateProcedure(String id, ProcedureIn in) {

        Procedure updated = procedureService.update(id, ProcedureParser.to(in));
        return ResponseEntity.ok(ProcedureParser.to(updated));
    }

    @Override
    public ResponseEntity<Void> deleteProcedure(String id) {

        procedureService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<ProcedureOut>> findAllProcedures() {

        return ResponseEntity.ok(
            ProcedureParser.to(procedureService.findAll())
        );
    }

    @Override
    public ResponseEntity<ProcedureOut> findProcedureById(String id) {

        Procedure p = procedureService.findById(id);
        return p == null ?
            ResponseEntity.notFound().build() :
            ResponseEntity.ok(ProcedureParser.to(p));
    }

}
