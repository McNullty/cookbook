package hr.vgsoft.cookbook.web.rest;

import hr.vgsoft.cookbook.domain.IngredientForRecipe;
import hr.vgsoft.cookbook.repository.IngredientForRecipeRepository;
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
 * REST controller for managing {@link hr.vgsoft.cookbook.domain.IngredientForRecipe}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class IngredientForRecipeResource {

    private final Logger log = LoggerFactory.getLogger(IngredientForRecipeResource.class);

    private static final String ENTITY_NAME = "ingredientForRecipe";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IngredientForRecipeRepository ingredientForRecipeRepository;

    public IngredientForRecipeResource(IngredientForRecipeRepository ingredientForRecipeRepository) {
        this.ingredientForRecipeRepository = ingredientForRecipeRepository;
    }

    /**
     * {@code POST  /ingredient-for-recipes} : Create a new ingredientForRecipe.
     *
     * @param ingredientForRecipe the ingredientForRecipe to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ingredientForRecipe, or with status {@code 400 (Bad Request)} if the ingredientForRecipe has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ingredient-for-recipes")
    public ResponseEntity<IngredientForRecipe> createIngredientForRecipe(@Valid @RequestBody IngredientForRecipe ingredientForRecipe)
        throws URISyntaxException {
        log.debug("REST request to save IngredientForRecipe : {}", ingredientForRecipe);
        if (ingredientForRecipe.getId() != null) {
            throw new BadRequestAlertException("A new ingredientForRecipe cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IngredientForRecipe result = ingredientForRecipeRepository.save(ingredientForRecipe);
        return ResponseEntity
            .created(new URI("/api/ingredient-for-recipes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ingredient-for-recipes/:id} : Updates an existing ingredientForRecipe.
     *
     * @param id the id of the ingredientForRecipe to save.
     * @param ingredientForRecipe the ingredientForRecipe to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ingredientForRecipe,
     * or with status {@code 400 (Bad Request)} if the ingredientForRecipe is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ingredientForRecipe couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ingredient-for-recipes/{id}")
    public ResponseEntity<IngredientForRecipe> updateIngredientForRecipe(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody IngredientForRecipe ingredientForRecipe
    ) throws URISyntaxException {
        log.debug("REST request to update IngredientForRecipe : {}, {}", id, ingredientForRecipe);
        if (ingredientForRecipe.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ingredientForRecipe.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ingredientForRecipeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        IngredientForRecipe result = ingredientForRecipeRepository.save(ingredientForRecipe);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ingredientForRecipe.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ingredient-for-recipes/:id} : Partial updates given fields of an existing ingredientForRecipe, field will ignore if it is null
     *
     * @param id the id of the ingredientForRecipe to save.
     * @param ingredientForRecipe the ingredientForRecipe to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ingredientForRecipe,
     * or with status {@code 400 (Bad Request)} if the ingredientForRecipe is not valid,
     * or with status {@code 404 (Not Found)} if the ingredientForRecipe is not found,
     * or with status {@code 500 (Internal Server Error)} if the ingredientForRecipe couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ingredient-for-recipes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IngredientForRecipe> partialUpdateIngredientForRecipe(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody IngredientForRecipe ingredientForRecipe
    ) throws URISyntaxException {
        log.debug("REST request to partial update IngredientForRecipe partially : {}, {}", id, ingredientForRecipe);
        if (ingredientForRecipe.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ingredientForRecipe.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ingredientForRecipeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IngredientForRecipe> result = ingredientForRecipeRepository
            .findById(ingredientForRecipe.getId())
            .map(existingIngredientForRecipe -> {
                if (ingredientForRecipe.getQuantity() != null) {
                    existingIngredientForRecipe.setQuantity(ingredientForRecipe.getQuantity());
                }

                return existingIngredientForRecipe;
            })
            .map(ingredientForRecipeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ingredientForRecipe.getId().toString())
        );
    }

    /**
     * {@code GET  /ingredient-for-recipes} : get all the ingredientForRecipes.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ingredientForRecipes in body.
     */
    @GetMapping("/ingredient-for-recipes")
    public List<IngredientForRecipe> getAllIngredientForRecipes(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all IngredientForRecipes");
        return ingredientForRecipeRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /ingredient-for-recipes/:id} : get the "id" ingredientForRecipe.
     *
     * @param id the id of the ingredientForRecipe to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ingredientForRecipe, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ingredient-for-recipes/{id}")
    public ResponseEntity<IngredientForRecipe> getIngredientForRecipe(@PathVariable Long id) {
        log.debug("REST request to get IngredientForRecipe : {}", id);
        Optional<IngredientForRecipe> ingredientForRecipe = ingredientForRecipeRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(ingredientForRecipe);
    }

    /**
     * {@code DELETE  /ingredient-for-recipes/:id} : delete the "id" ingredientForRecipe.
     *
     * @param id the id of the ingredientForRecipe to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ingredient-for-recipes/{id}")
    public ResponseEntity<Void> deleteIngredientForRecipe(@PathVariable Long id) {
        log.debug("REST request to delete IngredientForRecipe : {}", id);
        ingredientForRecipeRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
