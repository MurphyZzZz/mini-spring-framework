package minidi.container;

import com.thoughtworks.fusheng.integration.junit5.FuShengTest;
import minidi.exception.BeanHasCirCleDependency;
import minidi.container.fixture.Fruit;
import minidi.container.fixture.ImageFileProcessor;
import minidi.container.fixture.Product;
import minidi.container.fixture.SynchronousPaymentProcessor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@FuShengTest
public class DITest {

    private final String packageName = this.getClass().getPackageName();

    public String getComponent() {
        // given
        Container container = new Container(packageName);
        container.lunch();

        // when
        Fruit result = (Fruit) container.getBeanInstance(Fruit.class);

        // then
        return "你可以从Container中取出实例化的组件。";
    }

    public String getComponentAccordingToName() {
        // given
        Container container = new Container(packageName);
        container.lunch();

        // when
        ImageFileProcessor imageFileProcessor = (ImageFileProcessor) container.getBeanInstance(ImageFileProcessor.class);

        // then
        assertEquals(imageFileProcessor.imageFileEditor.edit(), "PngFileEditor");
        return imageFileProcessor.imageFileEditor.edit();
    }

    public String getComponentAccordingToQualifier() {
        // given
        Container container = new Container(packageName);
        container.lunch();

        // when
        Product product = (Product) container.getBeanInstance(Product.class);

        // then
        SynchronousPaymentProcessor synchronousPaymentProcessor = new SynchronousPaymentProcessor();
        assertEquals(product.paymentProcessor.pay(), synchronousPaymentProcessor.pay());
        return SynchronousPaymentProcessor.class.getSimpleName();
    }

    public String circularDependency() {
        // given
        Container container = new Container(packageName);
        BeanHasCirCleDependency exception = assertThrows(BeanHasCirCleDependency.class, container::lunch);
        return exception.getMessage();
    }
}
