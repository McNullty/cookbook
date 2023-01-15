package hr.vgsoft.cookbook.repository;

import hr.vgsoft.cookbook.domain.Recipe;
import java.util.Collection;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the Recipe entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    List<Recipe> findAllByProcessed(boolean processed);

    @Query("SELECT r "
        + "FROM Recipe r "
        + "JOIN RecipeSearch rs ON(r.id = rs.recipe.id) "
        + "WHERE rs.ingredientsCombination IN :search "
        + "GROUP BY r.id, r.name, r.description, r.processed, r.createdBy, r.createdDate, r.lastModifiedBy, r.lastModifiedDate "
        + "ORDER BY MAX(rs.nrCombinations) DESC")
    List<Recipe> findAllBySearch(@Param("search") Collection<String> search);

}
