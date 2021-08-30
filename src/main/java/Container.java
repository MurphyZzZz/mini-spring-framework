import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Container {

    Map<String, Class> componentsMap = new HashMap<>();

    public void addComponent(Class componentClass) {
        String name = componentClass.getName();
        componentsMap.put(name, componentClass);
    }

    public Collection<Class> getComponents() {
        return componentsMap.values();
    }
}
