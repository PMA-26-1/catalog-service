package store.catalog.procedure;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface ProcedureRepository extends CrudRepository<ProcedureModel, String> {

    Optional<ProcedureModel> findByName(String name);

}
