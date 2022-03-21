package hr.vgsoft.cookbook.service;

import hr.vgsoft.cookbook.domain.IngredientForRecipe;
import hr.vgsoft.cookbook.domain.Recipe;
import hr.vgsoft.cookbook.repository.IngredientForRecipeRepository;
import hr.vgsoft.cookbook.repository.RecipeRepository;
import hr.vgsoft.cookbook.service.dto.DetailsDTO;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RecipeServiceImpl implements RecipeService{

    private final Logger log = LoggerFactory.getLogger(RecipeServiceImpl.class);

    private final IngredientForRecipeRepository ingredientRepository;
    private final RecipeRepository recipeRepository;

    public RecipeServiceImpl(
        IngredientForRecipeRepository ingredientRepository,
        RecipeRepository recipeRepository) {
        this.ingredientRepository = ingredientRepository;
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Optional<DetailsDTO> retrieveReceiptWithAllDetails(long id) {

        Recipe recipe = recipeRepository.getById(id);
        log.info("Retrieved recipe: {}", recipe);
        IngredientForRecipe ingredientForRecipe = ingredientRepository.getById(id);
        log.info("Retrieved ingredients: {}", ingredientForRecipe);
        DetailsDTO detailsDTO = new DetailsDTO(recipe);
        log.info("Created DTO: {}", recipe);
        return Optional.of(detailsDTO);
    }
}
