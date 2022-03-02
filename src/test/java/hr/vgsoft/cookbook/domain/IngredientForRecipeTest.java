package hr.vgsoft.cookbook.domain;

import static org.assertj.core.api.Assertions.assertThat;

import hr.vgsoft.cookbook.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IngredientForRecipeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IngredientForRecipe.class);
        IngredientForRecipe ingredientForRecipe1 = new IngredientForRecipe();
        ingredientForRecipe1.setId(1L);
        IngredientForRecipe ingredientForRecipe2 = new IngredientForRecipe();
        ingredientForRecipe2.setId(ingredientForRecipe1.getId());
        assertThat(ingredientForRecipe1).isEqualTo(ingredientForRecipe2);
        ingredientForRecipe2.setId(2L);
        assertThat(ingredientForRecipe1).isNotEqualTo(ingredientForRecipe2);
        ingredientForRecipe1.setId(null);
        assertThat(ingredientForRecipe1).isNotEqualTo(ingredientForRecipe2);
    }
}
