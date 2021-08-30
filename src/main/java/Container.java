import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Container {

    Map<String, Class<?>> componentsMap = new HashMap<>();

    public void addComponent(Class<?> componentClass) {
        String className = componentClass.getName();
        componentsMap.put(className, componentClass);
    }

    public Collection<Class<?>> getComponents() {
        return componentsMap.values();
    }

    public Object getComponent(Class<?> componentClass) {
        String className = componentClass.getName();
        try {
            Class<?> cls = componentsMap.get(className);
            return cls.getDeclaredConstructor().newInstance();
        } catch (NullPointerException | NoSuchMethodException exception) {
            throw new BeanNoFoundException(className);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new BeanInstantiationException();
        }
    }
}
