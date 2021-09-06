package container;

import exception.BeanHasCirCleDependency;
import fixture.Apple;
import fixture.AsynchronousPaymentProcessor;
import fixture.CircleDependencyA;
import fixture.CircleDependencyB;
import fixture.Fruit;
import fixture.GifFileEditor;
import fixture.ImageFileProcessor;
import fixture.PngFileEditor;
import fixture.Product;
import fixture.SynchronousPaymentProcessor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ContainerTest {
    @Test
    void should_add_component_to_container() {
        // given
        Container container = new Container();

        // when
        container.addComponent(Apple.class);

        // then
        assertEquals(1, container.getComponents().size());
    }

    @Test
    void should_get_component_from_container() {
        // given
        Container container = new Container();
        container.addComponent(Apple.class);
        container.lunch();

        // when
        Apple result = (Apple) container.getComponent(Apple.class);

        // then
        assertEquals("fruit", result.fruit());
    }

    @Test
    void should_automatically_inject_dependency_when_get_a_component() {
        // given
        Container container = new Container();
        container.addComponent(Apple.class);
        container.addComponent(Fruit.class);
        container.lunch();

        // when
        Fruit result = (Fruit) container.getComponent(Fruit.class);

        // then
        assertNotNull(result);
    }

    @Test
    public void should_get_bean_by_name_when_there_are_more_than_one_bean_implement_same_interface() {
        // given
        Container container = new Container();
        container.addComponent(GifFileEditor.class);
        container.addComponent(PngFileEditor.class);
        container.addComponent(ImageFileProcessor.class);
        container.lunch();

        // when
        ImageFileProcessor imageFileProcessor = (ImageFileProcessor) container.getComponent(ImageFileProcessor.class);

        // then
        assertEquals(imageFileProcessor.imageFileEditor.edit(), "PngFileEditor");
    }

    @Test
    void should_inject_synchronous_bean_given_inject_point_qualifier_is_annotation_Synchronous() {
        // given
        Container container = new Container();
        container.addComponent(AsynchronousPaymentProcessor.class);
        container.addComponent(SynchronousPaymentProcessor.class);
        container.addComponent(Product.class);
        container.lunch();

        // when
        Product product = (Product) container.getComponent(Product.class);

        // then
        SynchronousPaymentProcessor synchronousPaymentProcessor = new SynchronousPaymentProcessor();
        assertEquals(product.paymentProcessor.pay(), synchronousPaymentProcessor.pay());
    }

    @Test
    void should_get_component_successfully_even_there_is_a_cycle_dependency() {
        // given
        Container container = new Container();
        container.addComponent(CircleDependencyA.class);
        container.addComponent(CircleDependencyB.class);
        assertThrows(BeanHasCirCleDependency.class, container::lunch);
    }
}
