package hr.vgsoft.cookbook.repository;

import hr.vgsoft.cookbook.domain.IngredientForRecipe;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the IngredientForRecipe entity.
 */
@Repository
public interface IngredientForRecipeRepository extends JpaRepository<IngredientForRecipe, Long> {
    default Optional<IngredientForRecipe> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<IngredientForRecipe> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<IngredientForRecipe> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct ingredientForRecipe from IngredientForRecipe ingredientForRecipe left join fetch ingredientForRecipe.ingredient left join fetch ingredientForRecipe.unit left join fetch ingredientForRecipe.recipe",
        countQuery = "select count(distinct ingredientForRecipe) from IngredientForRecipe ingredientForRecipe"
    )
    Page<IngredientForRecipe> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct ingredientForRecipe from IngredientForRecipe ingredientForRecipe left join fetch ingredientForRecipe.ingredient left join fetch ingredientForRecipe.unit left join fetch ingredientForRecipe.recipe"
    )
    List<IngredientForRecipe> findAllWithToOneRelationships();

    @Query(
        "select ingredientForRecipe from IngredientForRecipe ingredientForRecipe left join fetch ingredientForRecipe.ingredient left join fetch ingredientForRecipe.unit left join fetch ingredientForRecipe.recipe where ingredientForRecipe.id =:id"
    )
    Optional<IngredientForRecipe> findOneWithToOneRelationships(@Param("id") Long id);

    @Query(
        "select distinct ingredientForRecipe from IngredientForRecipe ingredientForRecipe "
            + "left join fetch ingredientForRecipe.ingredient i "
            + "left join fetch ingredientForRecipe.unit u "
            + "left join fetch ingredientForRecipe.recipe r where r.id = :recipeId"
    )
    List<IngredientForRecipe> findByRecipeIdWithDetails(@Param("recipeId") Long recipeId);
}
