package hr.vgsoft.cookbook.service.dto;

import java.io.Serializable;

public class RecipeItemsDTO implements Serializable {

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
