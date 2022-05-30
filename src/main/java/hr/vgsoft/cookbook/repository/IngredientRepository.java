package hr.vgsoft.cookbook.repository;

import hr.vgsoft.cookbook.domain.Ingredient;
import java.util.Set;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the Ingredient entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    Ingredient findByName(String name);

    Set<Ingredient> findAllByNameIn(List<String> names);
}
