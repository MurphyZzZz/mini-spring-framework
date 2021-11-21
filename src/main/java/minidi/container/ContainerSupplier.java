package minidi.container;

public class ContainerSupplier {

    private final Container container;

    public ContainerSupplier(Class<?> applicationClz) {
        this.container = new Container(applicationClz.getPackageName());
    }

    public void bootStrap() {
        container.lunch();
    }

    public Container getContainer() {
        return this.container;
    }
}
