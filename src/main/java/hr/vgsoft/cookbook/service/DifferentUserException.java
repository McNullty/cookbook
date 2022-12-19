package hr.vgsoft.cookbook.service;

public class DifferentUserException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DifferentUserException() {
        super("Update can't be done by different user ");
    }
}
