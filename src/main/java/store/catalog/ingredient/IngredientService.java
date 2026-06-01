package store.catalog.ingredient;

import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import store.catalog.culinaryitem.MeasurementConversionModel;
import store.catalog.culinaryitem.NutritionModel;

@Service
@Transactional
public class IngredientService {

    @Autowired
    private IngredientRepository ingredientRepository;

    public Ingredient create(Ingredient i) {

        return ingredientRepository.save(new IngredientModel(i)).to();
    }

    @CacheEvict(value = "ingredients", key = "#id")
    public Ingredient update(String id, Ingredient i) {

        IngredientModel model = ingredientRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ingredient not found"));

        model.name(i.name())
             .category(i.category())
             .iconType(i.iconType())
             .nutrition(i.nutritionValues() == null ? null : new NutritionModel(i.nutritionValues()));

        // Full-replace strategy for nested conversions. orphanRemoval=true on the
        // parent's @OneToMany causes Hibernate to delete the rows we drop here.
        model.measurementConversions().clear();
        if (i.measurementConversions() != null) {
            i.measurementConversions().forEach(mc -> {
                MeasurementConversionModel m = new MeasurementConversionModel(mc);
                m.culinaryItem(model);
                model.measurementConversions().add(m);
            });
        }

        return ingredientRepository.save(model).to();
    }

    @CacheEvict(value = "ingredients", key = "#id")
    public void delete(String id) {
        ingredientRepository.deleteById(id);
    }

    @Cacheable(value = "ingredients", key = "#id")
    public Ingredient findById(String id) {

        return ingredientRepository.findById(id)
            .map(IngredientModel::to)
            .orElse(null);
    }

    public List<Ingredient> findAll() {

        return StreamSupport.stream(
            ingredientRepository.findAll().spliterator(),
            false
        ).map(IngredientModel::to).toList();
    }

}
