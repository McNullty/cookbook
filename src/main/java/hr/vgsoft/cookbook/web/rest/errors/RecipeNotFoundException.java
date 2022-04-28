package hr.vgsoft.cookbook.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class RecipeNotFoundException extends AbstractThrowableProblem {

    private static final long serialVersionUID = -7638244251750984757L;

    public RecipeNotFoundException() {
        super(ErrorConstants.DEFAULT_TYPE, "Recipe was not found", Status.NOT_FOUND);
    }
}
