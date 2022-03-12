package hr.vgsoft.cookbook.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A IngredientForRecipe.
 */
@Entity
@Table(name = "ingredient_for_recipe")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class IngredientForRecipe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Double quantity;

    @ManyToOne(optional = false)
    @NotNull
    private Ingredient ingredient;

    @ManyToOne(optional = false)
    @NotNull
    private Unit unit;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "ingredientForRecipes" }, allowSetters = true)
    private Recipe recipe;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IngredientForRecipe id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getQuantity() {
        return this.quantity;
    }

    public IngredientForRecipe quantity(Double quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Ingredient getIngredient() {
        return this.ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public IngredientForRecipe ingredient(Ingredient ingredient) {
        this.setIngredient(ingredient);
        return this;
    }

    public Unit getUnit() {
        return this.unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public IngredientForRecipe unit(Unit unit) {
        this.setUnit(unit);
        return this;
    }

    public Recipe getRecipe() {
        return this.recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public IngredientForRecipe recipe(Recipe recipe) {
        this.setRecipe(recipe);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IngredientForRecipe)) {
            return false;
        }
        return id != null && id.equals(((IngredientForRecipe) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IngredientForRecipe{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            "}";
    }
}
