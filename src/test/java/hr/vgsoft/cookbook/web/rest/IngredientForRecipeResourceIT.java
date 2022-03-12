package hr.vgsoft.cookbook.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import hr.vgsoft.cookbook.IntegrationTest;
import hr.vgsoft.cookbook.domain.Ingredient;
import hr.vgsoft.cookbook.domain.IngredientForRecipe;
import hr.vgsoft.cookbook.domain.Recipe;
import hr.vgsoft.cookbook.domain.Unit;
import hr.vgsoft.cookbook.repository.IngredientForRecipeRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link IngredientForRecipeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IngredientForRecipeResourceIT {

    private static final Double DEFAULT_QUANTITY = 1D;
    private static final Double UPDATED_QUANTITY = 2D;

    private static final String ENTITY_API_URL = "/api/ingredient-for-recipes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IngredientForRecipeRepository ingredientForRecipeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIngredientForRecipeMockMvc;

    private IngredientForRecipe ingredientForRecipe;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IngredientForRecipe createEntity(EntityManager em) {
        IngredientForRecipe ingredientForRecipe = new IngredientForRecipe().quantity(DEFAULT_QUANTITY);
        // Add required entity
        Ingredient ingredient;
        if (TestUtil.findAll(em, Ingredient.class).isEmpty()) {
            ingredient = IngredientResourceIT.createEntity(em);
            em.persist(ingredient);
            em.flush();
        } else {
            ingredient = TestUtil.findAll(em, Ingredient.class).get(0);
        }
        ingredientForRecipe.setIngredient(ingredient);
        // Add required entity
        Unit unit;
        if (TestUtil.findAll(em, Unit.class).isEmpty()) {
            unit = UnitResourceIT.createEntity(em);
            em.persist(unit);
            em.flush();
        } else {
            unit = TestUtil.findAll(em, Unit.class).get(0);
        }
        ingredientForRecipe.setUnit(unit);
        // Add required entity
        Recipe recipe;
        if (TestUtil.findAll(em, Recipe.class).isEmpty()) {
            recipe = RecipeResourceIT.createEntity(em);
            em.persist(recipe);
            em.flush();
        } else {
            recipe = TestUtil.findAll(em, Recipe.class).get(0);
        }
        ingredientForRecipe.setRecipe(recipe);
        return ingredientForRecipe;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IngredientForRecipe createUpdatedEntity(EntityManager em) {
        IngredientForRecipe ingredientForRecipe = new IngredientForRecipe().quantity(UPDATED_QUANTITY);
        // Add required entity
        Ingredient ingredient;
        if (TestUtil.findAll(em, Ingredient.class).isEmpty()) {
            ingredient = IngredientResourceIT.createUpdatedEntity(em);
            em.persist(ingredient);
            em.flush();
        } else {
            ingredient = TestUtil.findAll(em, Ingredient.class).get(0);
        }
        ingredientForRecipe.setIngredient(ingredient);
        // Add required entity
        Unit unit;
        if (TestUtil.findAll(em, Unit.class).isEmpty()) {
            unit = UnitResourceIT.createUpdatedEntity(em);
            em.persist(unit);
            em.flush();
        } else {
            unit = TestUtil.findAll(em, Unit.class).get(0);
        }
        ingredientForRecipe.setUnit(unit);
        // Add required entity
        Recipe recipe;
        if (TestUtil.findAll(em, Recipe.class).isEmpty()) {
            recipe = RecipeResourceIT.createUpdatedEntity(em);
            em.persist(recipe);
            em.flush();
        } else {
            recipe = TestUtil.findAll(em, Recipe.class).get(0);
        }
        ingredientForRecipe.setRecipe(recipe);
        return ingredientForRecipe;
    }

    @BeforeEach
    public void initTest() {
        ingredientForRecipe = createEntity(em);
    }

    @Test
    @Transactional
    void createIngredientForRecipe() throws Exception {
        int databaseSizeBeforeCreate = ingredientForRecipeRepository.findAll().size();
        // Create the IngredientForRecipe
        restIngredientForRecipeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ingredientForRecipe))
            )
            .andExpect(status().isCreated());

        // Validate the IngredientForRecipe in the database
        List<IngredientForRecipe> ingredientForRecipeList = ingredientForRecipeRepository.findAll();
        assertThat(ingredientForRecipeList).hasSize(databaseSizeBeforeCreate + 1);
        IngredientForRecipe testIngredientForRecipe = ingredientForRecipeList.get(ingredientForRecipeList.size() - 1);
        assertThat(testIngredientForRecipe.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void createIngredientForRecipeWithExistingId() throws Exception {
        // Create the IngredientForRecipe with an existing ID
        ingredientForRecipe.setId(1L);

        int databaseSizeBeforeCreate = ingredientForRecipeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIngredientForRecipeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ingredientForRecipe))
            )
            .andExpect(status().isBadRequest());

        // Validate the IngredientForRecipe in the database
        List<IngredientForRecipe> ingredientForRecipeList = ingredientForRecipeRepository.findAll();
        assertThat(ingredientForRecipeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = ingredientForRecipeRepository.findAll().size();
        // set the field null
        ingredientForRecipe.setQuantity(null);

        // Create the IngredientForRecipe, which fails.

        restIngredientForRecipeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ingredientForRecipe))
            )
            .andExpect(status().isBadRequest());

        List<IngredientForRecipe> ingredientForRecipeList = ingredientForRecipeRepository.findAll();
        assertThat(ingredientForRecipeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllIngredientForRecipes() throws Exception {
        // Initialize the database
        ingredientForRecipeRepository.saveAndFlush(ingredientForRecipe);

        // Get all the ingredientForRecipeList
        restIngredientForRecipeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ingredientForRecipe.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.doubleValue())));
    }

    @Test
    @Transactional
    void getIngredientForRecipe() throws Exception {
        // Initialize the database
        ingredientForRecipeRepository.saveAndFlush(ingredientForRecipe);

        // Get the ingredientForRecipe
        restIngredientForRecipeMockMvc
            .perform(get(ENTITY_API_URL_ID, ingredientForRecipe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ingredientForRecipe.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingIngredientForRecipe() throws Exception {
        // Get the ingredientForRecipe
        restIngredientForRecipeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewIngredientForRecipe() throws Exception {
        // Initialize the database
        ingredientForRecipeRepository.saveAndFlush(ingredientForRecipe);

        int databaseSizeBeforeUpdate = ingredientForRecipeRepository.findAll().size();

        // Update the ingredientForRecipe
        IngredientForRecipe updatedIngredientForRecipe = ingredientForRecipeRepository.findById(ingredientForRecipe.getId()).get();
        // Disconnect from session so that the updates on updatedIngredientForRecipe are not directly saved in db
        em.detach(updatedIngredientForRecipe);
        updatedIngredientForRecipe.quantity(UPDATED_QUANTITY);

        restIngredientForRecipeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIngredientForRecipe.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedIngredientForRecipe))
            )
            .andExpect(status().isOk());

        // Validate the IngredientForRecipe in the database
        List<IngredientForRecipe> ingredientForRecipeList = ingredientForRecipeRepository.findAll();
        assertThat(ingredientForRecipeList).hasSize(databaseSizeBeforeUpdate);
        IngredientForRecipe testIngredientForRecipe = ingredientForRecipeList.get(ingredientForRecipeList.size() - 1);
        assertThat(testIngredientForRecipe.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void putNonExistingIngredientForRecipe() throws Exception {
        int databaseSizeBeforeUpdate = ingredientForRecipeRepository.findAll().size();
        ingredientForRecipe.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIngredientForRecipeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ingredientForRecipe.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ingredientForRecipe))
            )
            .andExpect(status().isBadRequest());

        // Validate the IngredientForRecipe in the database
        List<IngredientForRecipe> ingredientForRecipeList = ingredientForRecipeRepository.findAll();
        assertThat(ingredientForRecipeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIngredientForRecipe() throws Exception {
        int databaseSizeBeforeUpdate = ingredientForRecipeRepository.findAll().size();
        ingredientForRecipe.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIngredientForRecipeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ingredientForRecipe))
            )
            .andExpect(status().isBadRequest());

        // Validate the IngredientForRecipe in the database
        List<IngredientForRecipe> ingredientForRecipeList = ingredientForRecipeRepository.findAll();
        assertThat(ingredientForRecipeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIngredientForRecipe() throws Exception {
        int databaseSizeBeforeUpdate = ingredientForRecipeRepository.findAll().size();
        ingredientForRecipe.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIngredientForRecipeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ingredientForRecipe))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IngredientForRecipe in the database
        List<IngredientForRecipe> ingredientForRecipeList = ingredientForRecipeRepository.findAll();
        assertThat(ingredientForRecipeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIngredientForRecipeWithPatch() throws Exception {
        // Initialize the database
        ingredientForRecipeRepository.saveAndFlush(ingredientForRecipe);

        int databaseSizeBeforeUpdate = ingredientForRecipeRepository.findAll().size();

        // Update the ingredientForRecipe using partial update
        IngredientForRecipe partialUpdatedIngredientForRecipe = new IngredientForRecipe();
        partialUpdatedIngredientForRecipe.setId(ingredientForRecipe.getId());

        partialUpdatedIngredientForRecipe.quantity(UPDATED_QUANTITY);

        restIngredientForRecipeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIngredientForRecipe.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIngredientForRecipe))
            )
            .andExpect(status().isOk());

        // Validate the IngredientForRecipe in the database
        List<IngredientForRecipe> ingredientForRecipeList = ingredientForRecipeRepository.findAll();
        assertThat(ingredientForRecipeList).hasSize(databaseSizeBeforeUpdate);
        IngredientForRecipe testIngredientForRecipe = ingredientForRecipeList.get(ingredientForRecipeList.size() - 1);
        assertThat(testIngredientForRecipe.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void fullUpdateIngredientForRecipeWithPatch() throws Exception {
        // Initialize the database
        ingredientForRecipeRepository.saveAndFlush(ingredientForRecipe);

        int databaseSizeBeforeUpdate = ingredientForRecipeRepository.findAll().size();

        // Update the ingredientForRecipe using partial update
        IngredientForRecipe partialUpdatedIngredientForRecipe = new IngredientForRecipe();
        partialUpdatedIngredientForRecipe.setId(ingredientForRecipe.getId());

        partialUpdatedIngredientForRecipe.quantity(UPDATED_QUANTITY);

        restIngredientForRecipeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIngredientForRecipe.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIngredientForRecipe))
            )
            .andExpect(status().isOk());

        // Validate the IngredientForRecipe in the database
        List<IngredientForRecipe> ingredientForRecipeList = ingredientForRecipeRepository.findAll();
        assertThat(ingredientForRecipeList).hasSize(databaseSizeBeforeUpdate);
        IngredientForRecipe testIngredientForRecipe = ingredientForRecipeList.get(ingredientForRecipeList.size() - 1);
        assertThat(testIngredientForRecipe.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void patchNonExistingIngredientForRecipe() throws Exception {
        int databaseSizeBeforeUpdate = ingredientForRecipeRepository.findAll().size();
        ingredientForRecipe.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIngredientForRecipeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ingredientForRecipe.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ingredientForRecipe))
            )
            .andExpect(status().isBadRequest());

        // Validate the IngredientForRecipe in the database
        List<IngredientForRecipe> ingredientForRecipeList = ingredientForRecipeRepository.findAll();
        assertThat(ingredientForRecipeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIngredientForRecipe() throws Exception {
        int databaseSizeBeforeUpdate = ingredientForRecipeRepository.findAll().size();
        ingredientForRecipe.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIngredientForRecipeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ingredientForRecipe))
            )
            .andExpect(status().isBadRequest());

        // Validate the IngredientForRecipe in the database
        List<IngredientForRecipe> ingredientForRecipeList = ingredientForRecipeRepository.findAll();
        assertThat(ingredientForRecipeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIngredientForRecipe() throws Exception {
        int databaseSizeBeforeUpdate = ingredientForRecipeRepository.findAll().size();
        ingredientForRecipe.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIngredientForRecipeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ingredientForRecipe))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IngredientForRecipe in the database
        List<IngredientForRecipe> ingredientForRecipeList = ingredientForRecipeRepository.findAll();
        assertThat(ingredientForRecipeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIngredientForRecipe() throws Exception {
        // Initialize the database
        ingredientForRecipeRepository.saveAndFlush(ingredientForRecipe);

        int databaseSizeBeforeDelete = ingredientForRecipeRepository.findAll().size();

        // Delete the ingredientForRecipe
        restIngredientForRecipeMockMvc
            .perform(delete(ENTITY_API_URL_ID, ingredientForRecipe.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<IngredientForRecipe> ingredientForRecipeList = ingredientForRecipeRepository.findAll();
        assertThat(ingredientForRecipeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
