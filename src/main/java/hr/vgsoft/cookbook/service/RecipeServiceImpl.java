package hr.vgsoft.cookbook.service;

import hr.vgsoft.cookbook.domain.IngredientForRecipe;
import hr.vgsoft.cookbook.domain.Recipe;
import hr.vgsoft.cookbook.repository.IngredientForRecipeRepository;
import hr.vgsoft.cookbook.repository.IngredientRepository;
import hr.vgsoft.cookbook.repository.RecipeRepository;
import hr.vgsoft.cookbook.repository.UnitRepository;
import hr.vgsoft.cookbook.service.dto.DetailsDTO;
import hr.vgsoft.cookbook.service.dto.IngredientForRecipeDTO;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RecipeServiceImpl implements RecipeService{

    private final Logger log = LoggerFactory.getLogger(RecipeServiceImpl.class);

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final UnitRepository unitRepository;
    private final IngredientForRecipeRepository ingredientForRecipeRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository,
        IngredientRepository ingredientRepository, UnitRepository unitRepository,
        IngredientForRecipeRepository ingredientForRecipeRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.unitRepository = unitRepository;
        this.ingredientForRecipeRepository = ingredientForRecipeRepository;
    }

    @Override
    public Optional<DetailsDTO> retrieveReceiptWithAllDetails(long id) {

        Optional<Recipe> recipe = recipeRepository.findById(id);
        log.info("Retrieved recipe: {}", recipe);
        if (recipe.isPresent()) {
            DetailsDTO detailsDTO = new DetailsDTO(recipe.get());
            log.info("Created DTO: {}", recipe);
            return Optional.of(detailsDTO);

        } else {
            return Optional.empty();
        }
    }

    @Override
    public Recipe processAndSave(DetailsDTO detailsDTO) {

        Recipe recipe = new Recipe();
        recipe.setName(detailsDTO.getName());
        recipe.setDescription(detailsDTO.getDescription());
        log.info("RECIPE: {}", recipe);
        Recipe savedRecipe = recipeRepository.save(recipe);
        Set<IngredientForRecipe> ingredientsForRecipes = new HashSet<>();

        for (IngredientForRecipeDTO ingredientForRecipeDTO : detailsDTO.getIngredientForRecipes()) {
            if (ingredientForRecipeDTO != null) {

                IngredientForRecipe ingredientForRecipe = new IngredientForRecipe();
                ingredientForRecipe.setQuantity(ingredientForRecipeDTO.getQuantity());
                ingredientForRecipe.setIngredient(ingredientRepository.findByName(ingredientForRecipeDTO.getIngredient()));
                ingredientForRecipe.setUnit(unitRepository.findByName(ingredientForRecipeDTO.getUnit()));
                ingredientForRecipe.setRecipe(recipe);

                log.info("IngredientForRecipe: {}", ingredientForRecipe);
                IngredientForRecipe result = ingredientForRecipeRepository.save(ingredientForRecipe);
                ingredientsForRecipes.add(result);
            }
        }
        savedRecipe.setIngredientForRecipes(ingredientsForRecipes);
        return savedRecipe;
    }
}
