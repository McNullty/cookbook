package hr.vgsoft.cookbook.web.rest;

import hr.vgsoft.cookbook.domain.Recipe;
import hr.vgsoft.cookbook.repository.RecipeRepository;
import hr.vgsoft.cookbook.service.RecipeService;
import hr.vgsoft.cookbook.service.dto.RecipeWithDetailsDTO;
import hr.vgsoft.cookbook.service.RecipeService;
import hr.vgsoft.cookbook.service.dto.DetailsDTO;
import hr.vgsoft.cookbook.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link hr.vgsoft.cookbook.domain.Recipe}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RecipeResource {

    private final Logger log = LoggerFactory.getLogger(RecipeResource.class);

    private static final String ENTITY_NAME = "recipe";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RecipeRepository recipeRepository;
    private final RecipeService recipeService;

    public RecipeResource(RecipeRepository recipeRepository, RecipeService recipeService) {
    private final RecipeService recipeService;

    public RecipeResource(RecipeRepository recipeRepository,
        RecipeService recipeService) {
        this.recipeRepository = recipeRepository;
        this.recipeService = recipeService;
    }

    /**
     * {@code POST  /recipes} : Create a new recipe.
     *
     * @param recipeWithDetailsDTO the recipe to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new recipe, or with status {@code 400 (Bad Request)} if the recipe has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/recipes")
    public ResponseEntity<Recipe> createRecipe(@Valid @RequestBody RecipeWithDetailsDTO recipeWithDetailsDTO) throws URISyntaxException {
        log.debug("REST request to save Recipe : {}", recipeWithDetailsDTO);
        Recipe result = recipeService.createNewRecipe(recipeWithDetailsDTO);
        return ResponseEntity
            .created(new URI("/api/recipes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }


    /**
     * {@code PUT  /recipes/:id} : Updates an existing recipe.
     *
     * @param id the id of the recipe to save.
     * @param recipe the recipe to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recipe,
     * or with status {@code 400 (Bad Request)} if the recipe is not valid,
     * or with status {@code 500 (Internal Server Error)} if the recipe couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/recipes/{id}")
    public ResponseEntity<Recipe> updateRecipe(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Recipe recipe
    ) throws URISyntaxException {
        log.debug("REST request to update Recipe : {}, {}", id, recipe);
        if (recipe.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recipe.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recipeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Recipe result = recipeRepository.save(recipe);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recipe.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /recipes/:id} : Partial updates given fields of an existing recipe, field will ignore if it is null
     *
     * @param id the id of the recipe to save.
     * @param recipe the recipe to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recipe,
     * or with status {@code 400 (Bad Request)} if the recipe is not valid,
     * or with status {@code 404 (Not Found)} if the recipe is not found,
     * or with status {@code 500 (Internal Server Error)} if the recipe couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/recipes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Recipe> partialUpdateRecipe(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Recipe recipe
    ) throws URISyntaxException {
        log.debug("REST request to partial update Recipe partially : {}, {}", id, recipe);
        if (recipe.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recipe.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recipeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Recipe> result = recipeRepository
            .findById(recipe.getId())
            .map(existingRecipe -> {
                if (recipe.getName() != null) {
                    existingRecipe.setName(recipe.getName());
                }
                if (recipe.getDescription() != null) {
                    existingRecipe.setDescription(recipe.getDescription());
                }

                return existingRecipe;
            })
            .map(recipeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recipe.getId().toString())
        );
    }

    /**
     * {@code GET  /recipes} : get all the recipes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of recipes in body.
     */
    @GetMapping("/recipes")
    public List<Recipe> getAllRecipes() {
        log.debug("REST request to get all Recipes");
        return recipeRepository.findAll();
    }

    /**
     * {@code GET  /recipes/:id} : get the "id" recipe.
     *
     * @param id the id of the recipe to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the recipe, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/recipes/{id}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable Long id) {
        log.debug("REST request to get Recipe : {}", id);
        Optional<Recipe> recipe = recipeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(recipe);
    }

    /**
     * {@code GET  /recipes/:id/details} : get the "id" recipe with details.
     *
     * @param id the id of the recipe to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the recipe with details, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/recipes/{id}/details")
    public ResponseEntity<RecipeWithDetailsDTO> getRecipeWithDetails(@PathVariable Long id) {
        log.debug("REST request to get Recipe with details : {}", id);
        Optional<RecipeWithDetailsDTO> recipe = recipeService.getRecipeWithDetails(id);
        return ResponseUtil.wrapOrNotFound(recipe);
    }

    /**
     * {@code DELETE  /recipes/:id} : delete the "id" recipe.
     *
     * @param id the id of the recipe to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/recipes/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        log.debug("REST request to delete Recipe : {}", id);
        recipeRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /recipes/:id} : get the "id" recipe.
     *
     * @param id the id of the recipe to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the recipe, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/recipes/{id}/details")
    public ResponseEntity<DetailsDTO> getRecipeWithDetails(@PathVariable Long id) {
        log.debug("REST request to get Recipe with details : {}", id);
        Optional<DetailsDTO> detailsDTO = recipeService.retrieveReceiptWithAllDetails(id);
        return ResponseUtil.wrapOrNotFound(detailsDTO);
    }

}
