package checker;

import exception.BeanHasCirCleDependency;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;

import static utils.DependencyInjectUtils.getInjectConstructor;

public class CircularDependencyChecker {
    static public void circularDependencyCheck(Class<?> clz) {
        Constructor<?> injectConstructor = getInjectConstructor(clz.getDeclaredConstructors());
        if (injectConstructor != null) {
            Parameter[] parameters = injectConstructor.getParameters();
            for (Parameter param: parameters) {
                Class<?> paramClz = param.getType();
                Constructor<?> paramConstructor = getInjectConstructor(paramClz.getDeclaredConstructors());
                if (paramConstructor != null) {
                    Parameter[] paramConstructorParameters = paramConstructor.getParameters();
                    for (Parameter parameter: paramConstructorParameters) {
                        String dependentClassName = parameter.getType().getName();
                        if (dependentClassName.equals(clz.getName()))
                            throw new BeanHasCirCleDependency(clz.getSimpleName(), paramClz.getName());
                    }
                }
            }
        }
    }
}
