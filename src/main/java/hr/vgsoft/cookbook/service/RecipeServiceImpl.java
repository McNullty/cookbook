package hr.vgsoft.cookbook.service;

import hr.vgsoft.cookbook.domain.Recipe;
import hr.vgsoft.cookbook.repository.RecipeRepository;
import hr.vgsoft.cookbook.service.dto.DetailsDTO;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RecipeServiceImpl implements RecipeService{

    private final Logger log = LoggerFactory.getLogger(RecipeServiceImpl.class);

    private final RecipeRepository recipeRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Optional<DetailsDTO> retrieveReceiptWithAllDetails(long id) {

        Optional<Recipe> recipe = recipeRepository.findById(id);
        log.info("Retrieved recipe: {}", recipe);
        if (recipe.isPresent()) {
            DetailsDTO detailsDTO = new DetailsDTO(recipe.get());
            log.info("Created DTO: {}", recipe);
            return Optional.of(detailsDTO);

        } else {
            return Optional.empty();
        }


    }
}
