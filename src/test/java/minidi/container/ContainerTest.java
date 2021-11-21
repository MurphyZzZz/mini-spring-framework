package minidi.container;

import minidi.container.fixture.Apple;
import minidi.container.fixture.Fruit;
import minidi.container.fixture.ImageFileProcessor;
import minidi.container.fixture.ManuallyBean;
import minidi.container.fixture.Product;
import minidi.container.fixture.SynchronousPaymentProcessor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ContainerTest {

    private final String packageName = this.getClass().getPackageName();

    @Test
    void should_get_component_from_container() {
        // given
        Container container = new Container(packageName);
        container.lunch();

        // when
        Apple result = (Apple) container.getBeanInstance(Apple.class);

        // then
        assertEquals("fruit", result.fruit());
    }

    @Test
    void should_automatically_inject_dependency_when_get_a_component() {
        // given
        Container container = new Container(packageName);
        container.lunch();

        // when
        Fruit result = (Fruit) container.getBeanInstance(Fruit.class);

        // then
        assertNotNull(result);
    }

    @Test
    public void should_get_bean_by_name_when_there_are_more_than_one_bean_implement_same_interface() {
        // given
        Container container = new Container(packageName);
        container.lunch();

        // when
        ImageFileProcessor imageFileProcessor = (ImageFileProcessor) container.getBeanInstance(ImageFileProcessor.class);

        // then
        assertEquals(imageFileProcessor.imageFileEditor.edit(), "PngFileEditor");
    }

    @Test
    void should_inject_synchronous_bean_given_inject_point_qualifier_is_annotation_Synchronous() {
        // given
        Container container = new Container(packageName);
        container.lunch();

        // when
        Product product = (Product) container.getBeanInstance(Product.class);

        // then
        SynchronousPaymentProcessor synchronousPaymentProcessor = new SynchronousPaymentProcessor();
        assertEquals(product.paymentProcessor.pay(), synchronousPaymentProcessor.pay());
    }

    @Test
    void should_get_component_successfully_even_there_is_a_cycle_dependency() {
        // given
        Container container = new Container(packageName);
        assertDoesNotThrow(container::lunch);
    }

    @Test
    void should_be_able_to_manually_register_bean() {
        // given
        Container container = new Container(packageName);

        // when
        container.registerBean(ManuallyBean.class);
        container.lunch();

        // then
        ManuallyBean manuallyBean = (ManuallyBean) container.getBeanInstance(ManuallyBean.class);
    }
}
