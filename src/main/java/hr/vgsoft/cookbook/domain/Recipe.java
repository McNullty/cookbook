package hr.vgsoft.cookbook.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Recipe.
 */
@Entity
@Table(name = "recipe")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Recipe extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;


    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "recipe", cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ingredient", "unit", "recipe" }, allowSetters = true)
    private Set<IngredientForRecipe> ingredientForRecipes = new HashSet<>();
    @Column(name = "processed")
    private boolean processed;

    public Recipe() {
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Recipe id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Recipe name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Recipe description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<IngredientForRecipe> getIngredientForRecipes() {
        return this.ingredientForRecipes;
    }

    public void setIngredientForRecipes(Set<IngredientForRecipe> ingredientForRecipes) {
        if (this.ingredientForRecipes != null) {
            this.ingredientForRecipes.forEach(i -> i.setRecipe(null));
        }
        if (ingredientForRecipes != null) {
            ingredientForRecipes.forEach(i -> i.setRecipe(this));
        }
        this.ingredientForRecipes = ingredientForRecipes;
    }

    public Recipe ingredientForRecipes(Set<IngredientForRecipe> ingredientForRecipes) {
        this.setIngredientForRecipes(ingredientForRecipes);
        return this;
    }

    public Recipe addIngredientForRecipe(IngredientForRecipe ingredientForRecipe) {
        this.ingredientForRecipes.add(ingredientForRecipe);
        ingredientForRecipe.setRecipe(this);
        return this;
    }

    public Recipe removeIngredientForRecipe(IngredientForRecipe ingredientForRecipe) {
        this.ingredientForRecipes.remove(ingredientForRecipe);
        return this;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Recipe)) {
            return false;
        }
        return id != null && id.equals(((Recipe) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Recipe{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
