package container;

import exception.BeanInstantiationException;
import exception.BeanNoFoundException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Qualifier;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static checker.CircularDependencyChecker.circularDependencyCheck;

public class Container {

    Map<String, Class<?>> componentsMap = new HashMap<>();
    Map<String, Object> instancesMap = new HashMap<>();

    public void addComponent(Class<?> clz) {
        String className = getComponentName(clz);
        componentsMap.put(className, clz);
    }

    public void lunch(){
        Set<String> componentsName = componentsMap.keySet();
        for (String name: componentsName) {
            Class<?> clz = componentsMap.get(name);
            circularDependencyCheck(clz);
            instantiate(clz);
        }
    }

    public Collection<Class<?>> getComponents() {
        return componentsMap.values();
    }

    public Object getComponent(Class<?> componentClass) {
        String className = componentClass.getName();
        Object instance = instancesMap.get(className);
        if (instance == null) throw new BeanNoFoundException(className);
        return instance;
    }

    private String getComponentName(Class<?> clz) {
        // qualifier和named的优先级？
        if (clz.isAnnotationPresent(Named.class))
            return clz.getAnnotation(Named.class).value();

        String qualifierName = getQualifierComponentName(clz);
        if (qualifierName != null) return qualifierName;

        return clz.getName();
    }

    private String getQualifierComponentName(Class<?> clz){
        Annotation[] declaredAnnotations = clz.getDeclaredAnnotations();
        for (Annotation annotation: declaredAnnotations) {
            Class<?> annotationClz = annotation.annotationType();
            // 以第一个实现qualifier的annotation为准
            if (annotationClz.isAnnotationPresent(Qualifier.class)){
                return annotationClz.getSimpleName();
            }
        }
        return null;
    }

    private Object instantiate(Class<?> clz) {
        String className = getComponentName(clz);
        Object instance = instancesMap.getOrDefault(className, null);
        if (instance != null) {
            return instance;
        }
        try {
            Class<?> cls = componentsMap.get(className);
            Constructor<?> injectConstructor = getInjectConstructor(cls.getDeclaredConstructors());
            // @inject and number > 1
            if (injectConstructor != null) {
                ArrayList<Object> constructorParams = new ArrayList<>();
                Parameter[] parameters = injectConstructor.getParameters();
                for (Parameter param: parameters) {
                    String paramComponentName = getParamComponentName(param);
                    Object paramIns = instancesMap.getOrDefault(paramComponentName, null);
                    if (paramIns == null){
                        constructorParams.add(instantiate(param.getType()));
                    } else {
                        constructorParams.add(paramIns);
                    }
                }
                Object constructorParamsInstance = injectConstructor.newInstance(constructorParams.toArray());
                return instancesMap.put(className, constructorParamsInstance);
            } else {
                Constructor<?> constructor = cls.getDeclaredConstructor();
                Object newInstance = constructor.newInstance();
                return instancesMap.put(className, newInstance);
            }
        } catch (NullPointerException | NoSuchMethodException e) {
            e.printStackTrace();
            throw new BeanNoFoundException(className);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new BeanInstantiationException();
        }
    }

    private String getParamComponentName(Parameter param) {
        Class<?> paramClz = param.getType();
        if (param.isAnnotationPresent(Named.class)) {
            return param.getDeclaredAnnotation(Named.class).value();
        }

        String qualifierName = getParamQualifierComponentName(param);
        if (qualifierName != null) return qualifierName;

        return paramClz.getName();
    }

    private String getParamQualifierComponentName(Parameter param){
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

    private Constructor<?> getInjectConstructor(Constructor<?>[] constructors) {
        for (Constructor<?> constructor: constructors) {
            if (constructor.getParameterCount() >= 1 && constructor.isAnnotationPresent(Inject.class))
                return constructor;
        }
        return null;
    }
}
