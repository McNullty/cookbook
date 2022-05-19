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

    public Recipe updateRecipe(RecipeWithDetailsDTO recipeWithDetailsDTO, Long id) {

        Recipe existingRecipe = recipeRepository.getById(id);
        if (!existingRecipe.getName().equals(recipeWithDetailsDTO.getName())) {
            existingRecipe.setName(recipeWithDetailsDTO.getName());
            log.debug("name of recipe is changed");
        } else log.debug("name of recipe is the same");
        if (!existingRecipe.getDescription().equals(recipeWithDetailsDTO.getDescription())) {
            existingRecipe.setDescription(recipeWithDetailsDTO.getDescription());
            log.debug("description of recipe is changed");
        } else log.debug("description of recipe is the same");

        List<RecipeItemsDTO> recipeItemsDTOs = recipeWithDetailsDTO.getRecipeItems();
        Set<IngredientForRecipe> ingredientsForExistingRecipe= existingRecipe.getIngredientForRecipes();

        for (RecipeItemsDTO recipeItemDTO : recipeItemsDTOs) {
            boolean ingredientIsFound=false;
            for (IngredientForRecipe ingredientForExistingRecipe : ingredientsForExistingRecipe) {
                log.debug("Comparing each ingredientForRecipe from new Recipe with each ingredientForRecipe from old Recipe");
                if (ingredientRepository.findByName(recipeItemDTO.getIngredient()).equals(ingredientForExistingRecipe.getIngredient())) {
                    log.debug("ingredient is the same");
                    if (!unitRepository.findByName(recipeItemDTO.getUnit()).equals(ingredientForExistingRecipe.getUnit())) {
                        ingredientForExistingRecipe.setUnit(unitRepository.findByName(recipeItemDTO.getUnit()));
                        log.debug("unit is changed");
                    }
                    if (recipeItemDTO.getQuantity()!=ingredientForExistingRecipe.getQuantity()) {
                        ingredientForExistingRecipe.setQuantity(recipeItemDTO.getQuantity());
                        log.debug("quantity is changed");
                    }
                    ingredientIsFound = true;
                    break;
                }
            }
            if (!ingredientIsFound) {
                log.debug("add new ingredient");
                    IngredientForRecipe ingredientForRecipe = new IngredientForRecipe();
                    ingredientForRecipe.setIngredient(ingredientRepository.findByName(recipeItemDTO.getIngredient()));
                    ingredientForRecipe.setUnit(unitRepository.findByName(recipeItemDTO.getUnit()));
                    ingredientForRecipe.setQuantity(recipeItemDTO.getQuantity());
                    ingredientForRecipe.setRecipe(existingRecipe);
                    ingredientsForExistingRecipe.add(ingredientForRecipeRepository.save(ingredientForRecipe));
            }
        }

        List<IngredientForRecipe> ingredientForRecipeForDelete = new ArrayList<>();

        for (IngredientForRecipe ingredientForExistingRecipe : ingredientsForExistingRecipe) {
            boolean ingredientNotFound=true;
            for (RecipeItemsDTO recipeItemDTO : recipeItemsDTOs) {
                if (ingredientRepository.findByName(recipeItemDTO.getIngredient()).equals(ingredientForExistingRecipe.getIngredient()))  {
                    ingredientNotFound=false;
                    break;
                }
            }
            if (ingredientNotFound)
            ingredientForRecipeForDelete.add(ingredientForExistingRecipe);
        }

        for (IngredientForRecipe ingredientForRecipe : ingredientForRecipeForDelete) {
            existingRecipe.removeIngredientForRecipe(ingredientForRecipe);
        }

        Recipe result = recipeRepository.save(existingRecipe);

        return result;
    }
}
