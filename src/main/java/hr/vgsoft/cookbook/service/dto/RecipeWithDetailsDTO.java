package hr.vgsoft.cookbook.service.dto;

import hr.vgsoft.cookbook.domain.IngredientForRecipe;
import hr.vgsoft.cookbook.domain.Recipe;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.constraints.NotNull;

public class RecipeWithDetailsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private final String name;

    private final String description;

    private final String created_by;

    private List<RecipeItemsDTO> recipeItems;

    public RecipeWithDetailsDTO(String name, String description,String created_by, List<RecipeItemsDTO> recipeItems){
        this.name=name;
        this.description=description;
        this.created_by = created_by;
        this.recipeItems=recipeItems;
    }

    public RecipeWithDetailsDTO(Recipe recipe) {
        this.name = recipe.getName();
        this.description = recipe.getDescription();
        this.created_by=recipe.getCreatedBy();

        final Set<IngredientForRecipe> ingredientForRecipes = recipe.getIngredientForRecipes();
        ingredientForRecipes.forEach( ingredientForRecipe -> {
            addRecipeIngredient(ingredientForRecipe.getIngredient().getName(),
                ingredientForRecipe.getQuantity(), ingredientForRecipe.getUnit().getName());
        });
    }

    private void addRecipeIngredient(String ingredient, Double quantity, String unit) {
        if (recipeItems == null) {
            recipeItems = new ArrayList<>();
        }
        recipeItems.add(new RecipeItemsDTO(ingredient, quantity, unit));
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCreated_by() { return created_by; }

    public List<RecipeItemsDTO> getRecipeItems() {
        if (recipeItems == null) {
            return new ArrayList<>();
        }

        return recipeItems;
    }
}
