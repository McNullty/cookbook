package hr.vgsoft.cookbook.repository;

import hr.vgsoft.cookbook.domain.Recipe;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Recipe entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Override
    Optional<Recipe> findById(Long aLong);
}
