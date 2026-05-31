package store.catalog.recipe;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface RecipeRepository extends CrudRepository<RecipeModel, String> {

    List<RecipeModel> findByAccountId(String accountId);

}
