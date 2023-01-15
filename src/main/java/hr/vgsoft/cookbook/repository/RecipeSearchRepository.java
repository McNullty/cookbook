package hr.vgsoft.cookbook.repository;

import hr.vgsoft.cookbook.domain.Recipe;
import hr.vgsoft.cookbook.domain.RecipeSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface RecipeSearchRepository extends JpaRepository<RecipeSearch, Long> {

    Page<RecipeSearch> findAllByIngredientsCombinationIn(List<String> ingredientsCombination, Pageable pageable);

}