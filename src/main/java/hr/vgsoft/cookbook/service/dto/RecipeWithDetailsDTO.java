package hr.vgsoft.cookbook.service.dto;

import hr.vgsoft.cookbook.domain.Recipe;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class RecipeItemsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String ingredient;

    private final Double quantity;

    private final String unit;

    public RecipeItemsDTO(String ingredient, Double quantity, String unit) {
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.unit = unit;
    }

    public String getIngredient() {
        return ingredient;
    }

    public Double getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }
}

public class RecipeWithDetailsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Long id;

    private final String name;

    private final String description;

    private List<RecipeItemsDTO> recipeItems;

    public RecipeWithDetailsDTO(Recipe recipe) {
        this.id = recipe.getId();
        this.name = recipe.getName();
        this.description = recipe.getDescription();
    }

    public void addRecipeItem(String ingredient, Double quantity, String unit) {
        if (recipeItems == null) {
            recipeItems = new ArrayList<>();
        }
        recipeItems.add(new RecipeItemsDTO(ingredient, quantity, unit));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<RecipeItemsDTO> getRecipeItems() {
        return recipeItems;
    }
}
