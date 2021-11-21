package minidi.exception;

public class BeanInstantiationException extends RuntimeException {
    public BeanInstantiationException() {
        super("Something went wrong when instantiating a class.");
    }
}
