package hr.vgsoft.cookbook.repository;

import hr.vgsoft.cookbook.domain.Recipe;
import hr.vgsoft.cookbook.domain.RecipeSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the Recipe entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    List<Recipe> findAllByProcessed(boolean processed);



}
