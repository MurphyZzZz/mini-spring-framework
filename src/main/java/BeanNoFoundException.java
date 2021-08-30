public class BeanNoFoundException extends RuntimeException {
    public BeanNoFoundException(String message) {
        super(message + " does not found.");
    }
}
