package hr.vgsoft.cookbook.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Entity
@Table(name = "recipe_search")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RecipeSearch implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;


    @NotNull
    @Column(name = "ingredientsCombination", nullable = false)
    private String ingredientsCombination;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "recipeSearch" }, allowSetters = true)
    private Recipe recipe;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIngredientsCombination() {
        return ingredientsCombination;
    }

    public void setIngredientsCombination(String ingredientsCombination) {
        this.ingredientsCombination = ingredientsCombination;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }


}
