package container;

import exception.BeanInstantiationException;
import exception.BeanNoFoundException;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static checker.CircularDependencyChecker.circularDependencyCheck;
import static resolvers.BeanNameResolver.resolveBeanName;
import static utils.DependencyInjectUtils.getInjectConstructor;
import static utils.DependencyInjectUtils.getParamBeanName;

public class Container {

    final String packageName = "container";
    Map<String, Class<?>> beanMap = new HashMap<>();
    Map<String, Object> instancesMap = new HashMap<>();
    private final Reflections reflections = new Reflections(packageName);

    public void lunch(){
        this.addBean();
        Set<String> beansName = beanMap.keySet();
        for (String name: beansName) {
            Class<?> clz = beanMap.get(name);
            circularDependencyCheck(clz);
            instantiate(clz);
        }
    }

    public Object getBean(Class<?> beanClass) {
        String className = beanClass.getName();
        Object instance = instancesMap.get(className);
        if (instance == null) throw new BeanNoFoundException(className);
        return instance;
    }

    private void addBean() {
        final Set<Class<?>> managedBeans = reflections.getTypesAnnotatedWith(MiniDi.class);
        for (Class<?> clz: managedBeans) {
            String className = getBeanName(clz);
            beanMap.put(className, clz);
        }
    }

    private String getBeanName(Class<?> clz) {
        return resolveBeanName(clz);
    }

    private Object instantiate(Class<?> clz) {
        String className = getBeanName(clz);
        Object instance = instancesMap.getOrDefault(className, null);
        if (instance != null) {
            return instance;
        }
        try {
            Class<?> cls = beanMap.get(className);
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
            String paramBeanName = getParamBeanName(param);
            Object paramIns = instancesMap.getOrDefault(paramBeanName, null);
            if (paramIns == null){
                constructorParams.add(instantiate(beanMap.get(paramBeanName)));
            } else {
                constructorParams.add(paramIns);
            }
        }
        return injectConstructor.newInstance(constructorParams.toArray());
    }
}
