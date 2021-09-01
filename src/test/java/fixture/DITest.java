package fixture;

import com.thoughtworks.fusheng.integration.junit5.FuShengTest;
import container.Container;

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
}
