package hr.vgsoft.cookbook.service.dto;

import hr.vgsoft.cookbook.domain.IngredientForRecipe;

public class IngredientForRecipeDTO {

    private String ingredient;
    private Double quantity;
    private String unit;

    public IngredientForRecipeDTO() {
    }

    public IngredientForRecipeDTO(IngredientForRecipe ingredientForRecipe) {
        this.ingredient = ingredientForRecipe.getIngredient().getName();
        this.quantity = ingredientForRecipe.getQuantity();
        this.unit = ingredientForRecipe.getUnit().getName();
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IngredientForRecipeDTO)) {
            return false;
        }

        IngredientForRecipeDTO that = (IngredientForRecipeDTO) o;

        return ingredient.equals(that.ingredient);
    }

    @Override
    public int hashCode() {
        return ingredient.hashCode();
    }

    @Override
    public String toString() {
        return "IngredientForRecipeDTO{" +
            "ingredient='" + ingredient + '\'' +
            ", quantity=" + quantity +
            ", unit='" + unit + '\'' +
            '}';
    }
}
