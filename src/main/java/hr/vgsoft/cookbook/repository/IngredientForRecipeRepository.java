package hr.vgsoft.cookbook.repository;

import hr.vgsoft.cookbook.domain.IngredientForRecipe;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the IngredientForRecipe entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IngredientForRecipeRepository extends JpaRepository<IngredientForRecipe, Long> {}
