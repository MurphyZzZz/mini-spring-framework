package diFixture;

import com.thoughtworks.fusheng.integration.junit5.FuShengTest;
import container.Container;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@FuShengTest
public class DITest {

    public String getComponent() {
        // given
        Container container = new Container();
        container.addComponent(Apple.class);
        container.addComponent(Fruit.class);
        container.lunch();

        // when
        Fruit result = (Fruit) container.getComponent(Fruit.class);

        // then
        return "你可以从Container中取出实例化的组件。";
    }

    public String getComponentAccordingToName() {
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
        return imageFileProcessor.imageFileEditor.edit();
    }

    public String getComponentAccordingToQualifier() {
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
        return SynchronousPaymentProcessor.class.getSimpleName();
    }

    public String circularDependency() {
        // given
        Container container = new Container();
        container.addComponent(CircleDependencyA.class);
        container.addComponent(CircleDependencyB.class);
        BeanHasCirCleDependency exception = assertThrows(BeanHasCirCleDependency.class, container::lunch);
        return exception.getMessage();
    }
}
