package utils;

import javax.inject.Inject;
import java.lang.reflect.Constructor;

public class DependencyInjectUtils {
    static public Constructor<?> getInjectConstructor(Constructor<?>[] constructors) {
        for (Constructor<?> constructor: constructors) {
            if (constructor.getParameterCount() >= 1 && constructor.isAnnotationPresent(Inject.class))
                return constructor;
        }
        return null;
    }
}
