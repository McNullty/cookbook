package hr.vgsoft.cookbook.service.dto;

import hr.vgsoft.cookbook.domain.IngredientForRecipe;
import hr.vgsoft.cookbook.domain.Recipe;
import java.util.HashSet;
import java.util.Set;

public class DetailsDTO {

    public DetailsDTO(String name, String description,
        Set<IngredientForRecipeDTO> ingredientForRecipes) {
        this.name = name;
        this.description = description;
        this.ingredientForRecipes = ingredientForRecipes;
    }

    private Long id;
    private String name;
    private String description;
    private Set<IngredientForRecipeDTO> ingredientForRecipes;

    public DetailsDTO(Recipe recipe) {
        this.id = recipe.getId();
        this.name = recipe.getName();
        this.description = recipe.getDescription();
        this.ingredientForRecipes = fillInIngredients(recipe);
    }

    public DetailsDTO() {
    }

    private HashSet<IngredientForRecipeDTO> fillInIngredients(Recipe recipe) {
        HashSet<IngredientForRecipeDTO> ingredientForRecipeDTOS = new HashSet<>();
        for (IngredientForRecipe ingredient : recipe.getIngredientForRecipes()) {
            IngredientForRecipeDTO ingredientForRecipeDTO = new IngredientForRecipeDTO(ingredient);
            ingredientForRecipeDTOS.add(ingredientForRecipeDTO);
        }
        return ingredientForRecipeDTOS;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<IngredientForRecipeDTO> getIngredientForRecipes() {
        return ingredientForRecipes;
    }

    public void setIngredientForRecipes(
        Set<IngredientForRecipeDTO> ingredientForRecipes) {
        this.ingredientForRecipes = ingredientForRecipes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DetailsDTO)) {
            return false;
        }

        DetailsDTO that = (DetailsDTO) o;

        if (!id.equals(that.id)) {
            return false;
        }
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "DetailsDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", ingredientForRecipes=" + ingredientForRecipes +
            '}';
    }
}
