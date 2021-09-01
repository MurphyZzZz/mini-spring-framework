package container;

import fixture.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
}
