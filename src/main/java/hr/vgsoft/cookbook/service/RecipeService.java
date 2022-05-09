package hr.vgsoft.cookbook.service;

import hr.vgsoft.cookbook.domain.Recipe;
import hr.vgsoft.cookbook.service.dto.DetailsDTO;
import java.util.Optional;

public interface RecipeService {

    Optional<DetailsDTO> retrieveReceiptWithAllDetails(long id);
    Recipe processAndSave(DetailsDTO detailsDTO);

}
