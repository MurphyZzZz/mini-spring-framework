package diFixture;

import com.thoughtworks.fusheng.integration.junit5.FuShengTest;
import container.Container;
import exception.BeanHasCirCleDependency;
import container.fixture.Fruit;
import container.fixture.ImageFileProcessor;
import container.fixture.Product;
import container.fixture.SynchronousPaymentProcessor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@FuShengTest
public class DITest {

    public String getComponent() {
        // given
        Container container = new Container();
        container.lunch();

        // when
        Fruit result = (Fruit) container.getBean(Fruit.class);

        // then
        return "你可以从Container中取出实例化的组件。";
    }

    public String getComponentAccordingToName() {
        // given
        Container container = new Container();
        container.lunch();

        // when
        ImageFileProcessor imageFileProcessor = (ImageFileProcessor) container.getBean(ImageFileProcessor.class);

        // then
        assertEquals(imageFileProcessor.imageFileEditor.edit(), "PngFileEditor");
        return imageFileProcessor.imageFileEditor.edit();
    }

    public String getComponentAccordingToQualifier() {
        // given
        Container container = new Container();
        container.lunch();

        // when
        Product product = (Product) container.getBean(Product.class);

        // then
        SynchronousPaymentProcessor synchronousPaymentProcessor = new SynchronousPaymentProcessor();
        assertEquals(product.paymentProcessor.pay(), synchronousPaymentProcessor.pay());
        return SynchronousPaymentProcessor.class.getSimpleName();
    }

    public String circularDependency() {
        // given
        Container container = new Container();
        BeanHasCirCleDependency exception = assertThrows(BeanHasCirCleDependency.class, container::lunch);
        return exception.getMessage();
    }
}
