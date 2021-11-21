package minidi.resolvers;

import javax.inject.Named;

public class NamedResolver {

    static public String getNameOnAnnotationNamed(Class<?> clz) {
        if (clz.isAnnotationPresent(Named.class)) return clz.getAnnotation(Named.class).value();
        else return null;
    }
}
