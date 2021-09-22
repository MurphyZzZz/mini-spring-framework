package utils;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Qualifier;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;

public class DependencyInjectUtils {
    static public Constructor<?> getInjectConstructor(Constructor<?>[] constructors) {
        for (Constructor<?> constructor: constructors) {
            if (constructor.getParameterCount() >= 1 && constructor.isAnnotationPresent(Inject.class))
                return constructor;
        }
        return null;
    }

    static public String getParamComponentName(Parameter param) {
        Class<?> paramClz = param.getType();
        if (param.isAnnotationPresent(Named.class)) {
            return param.getDeclaredAnnotation(Named.class).value();
        }

        String qualifierName = getParamQualifierComponentName(param);
        if (qualifierName != null) return qualifierName;

        return paramClz.getName();
    }

    static public String getParamQualifierComponentName(Parameter param){
        Annotation[] declaredAnnotations = param.getDeclaredAnnotations();
        for (Annotation annotation: declaredAnnotations) {
            Class<?> annotationClz = annotation.annotationType();
            // 以第一个实现qualifier的annotation为准
            if (annotationClz.isAnnotationPresent(Qualifier.class)){
                return annotationClz.getSimpleName();
            }
        }
        return null;
    }
}
