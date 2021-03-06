package minidi.container;

import minidi.container.application.MyApplication;
import minidi.container.application.MySubComponent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ContainerSupplierTest {

    @Test
    void should_return_container_when_get_container_given_an_application_class() {
        ContainerSupplier containerSupplier = new ContainerSupplier(MyApplication.class);
        containerSupplier.bootStrap();
        Container container = containerSupplier.getContainer();
        MySubComponent subComponent = (MySubComponent) container.getBeanInstance(MySubComponent.class);

        assertEquals("MySubComponent", subComponent.getMyClassName());
    }
}
