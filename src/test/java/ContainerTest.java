import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ContainerTest {

    private class Apple {
        public void fruit () {}
    }
    @Test
    void should_add_component_to_container() {
        // given
        Container container = new Container();

        // when
        container.addComponent(Apple.class);

        // then
        assertEquals(1, container.getComponents().size());
    }
}
