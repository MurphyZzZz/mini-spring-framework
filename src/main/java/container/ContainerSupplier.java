package container;

public class ContainerSupplier {

    Class<?> applicationClz;
    Container container;

    public ContainerSupplier(Class<?> applicationClz) {
        this.applicationClz = applicationClz;
        this.container = new Container(applicationClz.getPackageName());
    }

    public void bootStrap() {
        container.lunch();
    }
}
