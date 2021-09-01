package container;

import exception.BeanInstantiationException;
import exception.BeanNoFoundException;

import javax.inject.Inject;
import java.lang.reflect.*;
import java.util.*;

public class Container {

    Map<String, Class<?>> componentsMap = new HashMap<>();
    Map<String, Object> instancesMap = new HashMap<>();

    public void addComponent(Class<?> componentClass) {
        String className = componentClass.getName();
        componentsMap.put(className, componentClass);
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

    private Object instantiate(Class<?> componentClass) {
        String className = componentClass.getName();
        Object instance = instancesMap.getOrDefault(className, null);
        if (instance != null) {
            return instance;
        }
        try {
            Class<?> cls = componentsMap.get(className);
            Constructor<?> injectConstructor = getInjectConstructor(cls.getDeclaredConstructors());
            // @inject and number > 1
            if (injectConstructor != null) {
                Class<?>[] parameterClasses = injectConstructor.getParameterTypes();
                ArrayList<Object> constructorParams = new ArrayList<Object>();
                for (Class<?> paraClz: parameterClasses) {
                    String paramClsName = paraClz.getName();
                    Object paramIns = instancesMap.getOrDefault(paramClsName, null);
                    if (paramIns == null){
                        constructorParams.add(instantiate(paraClz));
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
