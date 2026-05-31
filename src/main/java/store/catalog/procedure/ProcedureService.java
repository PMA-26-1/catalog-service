package store.catalog.procedure;

import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProcedureService {

    @Autowired
    private ProcedureRepository procedureRepository;

    public Procedure create(Procedure p) {

        if (procedureRepository.findByName(p.name()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Procedure already exists");
        }

        return procedureRepository.save(new ProcedureModel(p)).to();
    }

    public Procedure update(String id, Procedure p) {

        ProcedureModel model = procedureRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Procedure not found"));

        model.name(p.name());

        return procedureRepository.save(model).to();
    }

    public void delete(String id) {
        procedureRepository.deleteById(id);
    }

    public Procedure findById(String id) {

        return procedureRepository.findById(id)
            .map(ProcedureModel::to)
            .orElse(null);
    }

    public List<Procedure> findAll() {

        return StreamSupport.stream(
            procedureRepository.findAll().spliterator(),
            false
        ).map(ProcedureModel::to).toList();
    }

}
