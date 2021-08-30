import fixture.Apple;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ContainerTest {
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

        // when
        Apple result = (Apple) container.getComponent(Apple.class);

        // then
        assertEquals("fruit", result.fruit());
    }
}
