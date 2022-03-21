package hr.vgsoft.cookbook.service;

public class RecipeNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public RecipeNotFoundException() {
        super("Recipe not found!");
    }

}
