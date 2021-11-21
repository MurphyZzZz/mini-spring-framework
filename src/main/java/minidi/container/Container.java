package minidi.container;

import minidi.exception.BeanInstantiationException;
import minidi.exception.BeanNoFoundException;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static minidi.checker.CircularDependencyChecker.circularDependencyCheck;
import static minidi.resolvers.BeanNameResolver.resolveBeanName;
import static minidi.utils.DependencyInjectUtils.getInjectConstructor;
import static minidi.utils.DependencyInjectUtils.getParamBeanName;

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
        this.registerContainerSelf();
        this.scanBeans();
        this.instantiateBeans();
    }

    private void registerContainerSelf() {
        instancesMap.put(this.getClass().getName(), this);
    }

    private void instantiateBeans() {
        beanMap.keySet().forEach(this::instantiateBeanFromBeanMap);
    }

    private void instantiateBeanFromBeanMap(String name) {
        Class<?> clz = beanMap.get(name);
        circularDependencyCheck(clz);
        instantiate(clz);
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
        reflections.getTypesAnnotatedWith(MiniDi.class).forEach(this::registerBeanIntoBeanMap);
    }

    private void registerBeanIntoBeanMap(Class<?> clz) {
        String className = getBeanName(clz);
        beanMap.put(className, clz);
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

    public void registerBean(Class<?> beanClz) {
        registerBeanIntoBeanMap(beanClz);
    }
}
