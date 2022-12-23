package hr.vgsoft.cookbook.service;

import hr.vgsoft.cookbook.domain.Ingredient;
import hr.vgsoft.cookbook.domain.IngredientForRecipe;
import hr.vgsoft.cookbook.domain.Recipe;
import hr.vgsoft.cookbook.domain.Unit;
import hr.vgsoft.cookbook.repository.IngredientForRecipeRepository;
import hr.vgsoft.cookbook.repository.IngredientRepository;
import hr.vgsoft.cookbook.repository.RecipeRepository;
import hr.vgsoft.cookbook.repository.UnitRepository;
import hr.vgsoft.cookbook.service.dto.RecipeItemsDTO;
import hr.vgsoft.cookbook.service.dto.RecipeWithDetailsDTO;

import java.util.*;

import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public Page<Recipe> getAllRecipe(Integer pageNo, Integer pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<Recipe> pagedResult = recipeRepository.findAll(paging);
        return pagedResult;

    }

    public Recipe updateRecipe(RecipeWithDetailsDTO recipeWithDetailsDTO, Long id) {
        Recipe existingRecipe = recipeRepository.getById(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        if (!currentPrincipalName.equals(existingRecipe.getCreatedBy())) {
            throw new DifferentUserException();
        }
        existingRecipe.setName(recipeWithDetailsDTO.getName());
        existingRecipe.setDescription(recipeWithDetailsDTO.getDescription());

        final HashMap<String, IngredientForRecipe> existingIngredientForRecipe = new HashMap<>();
        for (IngredientForRecipe it: existingRecipe.getIngredientForRecipes()) {
            existingIngredientForRecipe.put(it.getIngredient().getName(), it);
        }

        final HashMap<String, RecipeItemsDTO> newIngredientForRecipe = new HashMap<>();
        for (RecipeItemsDTO recipeItemsDTO: recipeWithDetailsDTO.getRecipeItems()) {
            newIngredientForRecipe.put(recipeItemsDTO.getIngredient(), recipeItemsDTO);
        }

        final Set<String> existingIngredientForRecipeKeys = existingIngredientForRecipe.keySet();
        final Set<String> newIngredientForRecipeKeys = newIngredientForRecipe.keySet();

        final Set<String> forDeleting = existingIngredientForRecipeKeys.stream()
            .filter(it -> !newIngredientForRecipeKeys.contains(it)).collect(Collectors.toSet());

        final Set<String> forAdding = newIngredientForRecipeKeys.stream().filter(it -> !existingIngredientForRecipeKeys.contains(it))
            .collect(Collectors.toSet());

        final Set<String> forUpdating = existingIngredientForRecipeKeys.stream().filter(newIngredientForRecipeKeys::contains)
            .collect(Collectors.toSet());

        updateExisting(existingIngredientForRecipe, newIngredientForRecipe, forUpdating);

        for (String name: forDeleting) {
            existingRecipe.removeIngredientForRecipe(existingIngredientForRecipe.get(name));
        }

        final List<String> existingUnits = forAdding.stream().map(it -> newIngredientForRecipe.get(it).getUnit())
            .collect(Collectors.toList());
        final Set<Unit> units = unitRepository.findAllByNameIn(existingUnits);

        final List<String> existingIngredients = forAdding.stream().map(it -> newIngredientForRecipe.get(it).getIngredient())
            .collect(Collectors.toList());
        final Set<Ingredient> ingredients = ingredientRepository.findAllByNameIn(
            existingIngredients);
        for (String name: forAdding) {
            final RecipeItemsDTO recipeItemsDTO = newIngredientForRecipe.get(name);

            final IngredientForRecipe ingredientForRecipe = new IngredientForRecipe();
            ingredientForRecipe.setUnit(
                units.stream()
                    .filter(it -> it.getName().equals(recipeItemsDTO.getUnit()))
                    .findFirst().get());
            ingredientForRecipe.setIngredient(
                ingredients.stream()
                    .filter(it -> it.getName().equals(recipeItemsDTO.getIngredient()))
                    .findFirst().get());
            ingredientForRecipe.setQuantity(recipeItemsDTO.getQuantity());

            existingRecipe.addIngredientForRecipe(ingredientForRecipe);
        }

        return recipeRepository.save(existingRecipe);
    }

    private void updateExisting(HashMap<String, IngredientForRecipe> existingIngredientForRecipe,
        HashMap<String, RecipeItemsDTO> newIngredientForRecipe, Set<String> forUpdating) {
        final List<String> existingUnits = forUpdating.stream().map(it -> newIngredientForRecipe.get(it).getUnit())
            .collect(Collectors.toList());

        final Set<Unit> units = unitRepository.findAllByNameIn(existingUnits);

        for (String ingredientName : forUpdating) {
            final IngredientForRecipe ingredientForRecipe = existingIngredientForRecipe.get(ingredientName);
            final RecipeItemsDTO recipeItemsDTO = newIngredientForRecipe.get(ingredientName);

            ingredientForRecipe.setQuantity(recipeItemsDTO.getQuantity());
            ingredientForRecipe.setUnit(units.stream()
                .filter(it -> it.getName().equals(recipeItemsDTO.getUnit())).findFirst().get());
        }
    }

    public Recipe updateRecipeMG(RecipeWithDetailsDTO recipeWithDetailsDTO, Long id) {

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
