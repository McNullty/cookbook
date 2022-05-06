package hr.vgsoft.cookbook.service;

import hr.vgsoft.cookbook.domain.IngredientForRecipe;
import hr.vgsoft.cookbook.domain.Recipe;
import hr.vgsoft.cookbook.repository.IngredientForRecipeRepository;
import hr.vgsoft.cookbook.repository.IngredientRepository;
import hr.vgsoft.cookbook.repository.RecipeRepository;
import hr.vgsoft.cookbook.repository.UnitRepository;
import hr.vgsoft.cookbook.service.dto.RecipeItemsDTO;
import hr.vgsoft.cookbook.service.dto.RecipeWithDetailsDTO;

import java.util.*;

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

    private final IngredientRepository ingredientRepository;

    private final UnitRepository unitRepository;

    public RecipeService(RecipeRepository recipeRepository,
                         IngredientForRecipeRepository ingredientForRecipeRepository, IngredientRepository ingredientRepository, UnitRepository unitRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientForRecipeRepository = ingredientForRecipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.unitRepository = unitRepository;
    }

    @Transactional(readOnly = true)
    public Optional<RecipeWithDetailsDTO> getRecipeWithDetails(final Long recipeId) {

        Optional<Recipe> recipe = recipeRepository.findById(recipeId);

        if (recipe.isEmpty()) {
            return Optional.empty();
        }

        RecipeWithDetailsDTO recipeWithDetailsDTO = new RecipeWithDetailsDTO(recipe.get());

        log.debug("Returning recipe with details: {}", recipeWithDetailsDTO);

        return Optional.of(recipeWithDetailsDTO);
    }

    public Recipe createNewRecipe(RecipeWithDetailsDTO recipeWithDetailsDTO) {
        Recipe recipe = new Recipe();
        recipe.setName(recipeWithDetailsDTO.getName());
        recipe.setDescription(recipeWithDetailsDTO.getDescription());
        Recipe result = recipeRepository.save(recipe);

        Set<IngredientForRecipe> ingredientForRecipeList = new HashSet<>();

        List<RecipeItemsDTO> recipeItemsDTOs = recipeWithDetailsDTO.getRecipeItems();
        recipeItemsDTOs.forEach(recipeItemsDTO -> {
            IngredientForRecipe ingredientForRecipe = new IngredientForRecipe();
            ingredientForRecipe.setIngredient(ingredientRepository.findByName(recipeItemsDTO.getIngredient()));
            ingredientForRecipe.setUnit(unitRepository.findByName(recipeItemsDTO.getUnit()));
            ingredientForRecipe.setQuantity(recipeItemsDTO.getQuantity());
            ingredientForRecipe.setRecipe(recipe);
            ingredientForRecipeList.add(ingredientForRecipeRepository.save(ingredientForRecipe));
        });

        return result;
    }
}
