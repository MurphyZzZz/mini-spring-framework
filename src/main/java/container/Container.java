package container;

import exception.BeanInstantiationException;
import exception.BeanNoFoundException;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.*;
import java.util.*;

public class Container {

    Map<String, Class<?>> componentsMap = new HashMap<>();
    Map<String, Object> instancesMap = new HashMap<>();

    public void addComponent(Class<?> clz) {
        String className = getComponentName(clz);
        componentsMap.put(className, clz);
    }

    private String getComponentName(Class<?> clz) {
        if (clz.isAnnotationPresent(Named.class)) {
            Named namedAnnotation = clz.getAnnotation(Named.class);
            return namedAnnotation.value();
        }
        return clz.getName();
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
            return param.getAnnotation(Named.class).value();
        } else {
            return paramClz.getName();
        }
    }

    private Constructor<?> getInjectConstructor(Constructor<?>[] constructors) {
        for (Constructor<?> constructor: constructors) {
            if (constructor.getParameterCount() >= 1 && constructor.isAnnotationPresent(Inject.class))
                return constructor;
        }
        return null;
    }

    public void lunch(){
        Set<String> componentsName = componentsMap.keySet();
        for (String name: componentsName) {
            instantiate(componentsMap.get(name));
        }
    }
}
