package container;

import exception.BeanInstantiationException;
import exception.BeanNoFoundException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static checker.CircularDependencyChecker.circularDependencyCheck;
import static resolvers.BeanNameResolver.resolveBeanName;
import static utils.DependencyInjectUtils.getInjectConstructor;
import static utils.DependencyInjectUtils.getParamComponentName;

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
        return resolveBeanName(clz);
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
            if (injectConstructor != null) {
                Object constructorParamsInstance = instantiateConstructorClass(injectConstructor);
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

    private Object instantiateConstructorClass(Constructor<?> injectConstructor) throws InstantiationException, IllegalAccessException, InvocationTargetException {
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
        return injectConstructor.newInstance(constructorParams.toArray());
    }
}
