package exception;

public class BeanNoFoundException extends RuntimeException {
    public BeanNoFoundException(String message) {
        super("Bean"+ " " + message + " does not found.");
    }
}
