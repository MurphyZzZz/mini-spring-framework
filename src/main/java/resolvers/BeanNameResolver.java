package resolvers;

import java.util.function.Function;
import java.util.stream.Stream;

public class BeanNameResolver {
    static public String resolveBeanName(Class<?> clz) {

        Function<Class<?>, String> classStringFunction = Stream.<Function<Class<?>, String>>of(
                NamedResolver::getNameOnAnnotationNamed,
                QualifierResolver::getNameOnAnnotationQualifier
        )
                .filter(s -> s.apply(clz) != null)
                .findFirst().orElse(null);

        if (classStringFunction != null) return classStringFunction.apply(clz);
        else return clz.getName();
    }
}
