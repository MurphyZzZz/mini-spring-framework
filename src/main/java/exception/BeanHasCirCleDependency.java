package exception;

public class BeanHasCirCleDependency extends RuntimeException {
    public BeanHasCirCleDependency(String className,  String dependentClassName) {
        super(String.format("%s and %s has a cycle dependency.", className, dependentClassName));
    }
}
