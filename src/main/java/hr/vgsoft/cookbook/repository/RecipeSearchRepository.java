package hr.vgsoft.cookbook.repository;

import hr.vgsoft.cookbook.domain.RecipeSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface RecipeSearchRepository extends JpaRepository<RecipeSearch, Long> {


}
