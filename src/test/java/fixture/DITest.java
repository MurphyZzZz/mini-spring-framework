package fixture;

import com.thoughtworks.fusheng.integration.junit5.FuShengTest;
import container.Container;
import container.ContainerTest;
import lombok.val;

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
        val containerTest = new ContainerTest();
        containerTest.should_get_bean_by_name_when_there_are_more_than_one_bean_implement_same_interface();
        return "PngFileEditor";
    }
}
