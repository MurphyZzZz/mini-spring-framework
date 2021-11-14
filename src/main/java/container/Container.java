package container;

import exception.BeanInstantiationException;
import exception.BeanNoFoundException;
import org.reflections.Reflections;

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
import static utils.DependencyInjectUtils.getParamBeanName;

public class Container {

    String packageName;
    Map<String, Class<?>> beanMap;
    Map<String, Object> instancesMap;
    private final Reflections reflections;

    public Container(String packageName) {
        this.packageName = packageName;
        beanMap = new HashMap<>();
        instancesMap = new HashMap<>();
        reflections = new Reflections(packageName);
    }

    public void lunch(){
        instancesMap.put(this.getClass().getName(), this);
        this.scanBeans();
        this.instantiateBeans();
    }

    private void instantiateBeans() {
        Set<String> beansName = beanMap.keySet();
        for (String name: beansName) {
            Class<?> clz = beanMap.get(name);
            circularDependencyCheck(clz);
            instantiate(clz);
        }
    }

    public Collection<Class<?>> getAllBeans() {
        return beanMap.values();
    }

    public Object getBeanInstance(Class<?> beanClass) {
        String className = beanClass.getName();
        Object instance = instancesMap.get(className);
        if (instance == null) throw new BeanNoFoundException(className);
        return instance;
    }

    private void scanBeans() {
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
            if (instancesMap.getOrDefault(paramBeanName, null) == null){
                instantiate(beanMap.get(paramBeanName));
            }
            Object paramIns = instancesMap.getOrDefault(paramBeanName, null);
            constructorParams.add(paramIns);
        }
        return injectConstructor.newInstance(constructorParams.toArray());
    }
}
