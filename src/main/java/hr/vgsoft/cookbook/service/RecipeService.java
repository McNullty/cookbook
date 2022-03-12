package hr.vgsoft.cookbook.service;

import hr.vgsoft.cookbook.domain.IngredientForRecipe;
import hr.vgsoft.cookbook.domain.Recipe;
import hr.vgsoft.cookbook.repository.IngredientForRecipeRepository;
import hr.vgsoft.cookbook.repository.RecipeRepository;
import hr.vgsoft.cookbook.service.dto.RecipeWithDetailsDTO;
import hr.vgsoft.cookbook.web.rest.errors.RecipeNotFoundException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RecipeService {

    private final Logger log = LoggerFactory.getLogger(RecipeService.class);

    private final RecipeRepository recipeRepository;

    private final IngredientForRecipeRepository ingredientForRecipeRepository;

    public RecipeService(RecipeRepository recipeRepository,
        IngredientForRecipeRepository ingredientForRecipeRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientForRecipeRepository = ingredientForRecipeRepository;
    }

    @Transactional(readOnly = true)
    public Optional<RecipeWithDetailsDTO> getRecipeWithDetails(final Long recipeId) {

        Optional<Recipe> recipe = recipeRepository.findById(recipeId);

        if (recipe.isEmpty()) {
            return Optional.empty();
        }

        RecipeWithDetailsDTO recipeWithDetailsDTO = new RecipeWithDetailsDTO(recipe.get());

        List<IngredientForRecipe> ingredients =
            ingredientForRecipeRepository.findByRecipeIdWithDetails(recipeId);

        ingredients.forEach(ingredient -> recipeWithDetailsDTO.addRecipeItem(
            ingredient.getIngredient().getName(),
            ingredient.getQuantity(),
            ingredient.getUnit().getName()));

        log.debug("Returning recipe with details: {}", recipeWithDetailsDTO);

        return Optional.of(recipeWithDetailsDTO);
    }
}
