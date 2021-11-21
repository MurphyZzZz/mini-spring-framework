package minidi.container.application;

import minidi.container.Container;
import minidi.container.MiniDi;

import javax.inject.Inject;

@MiniDi
public class MyApplication {

    Container container;

    @Inject
    public MyApplication(Container container) {
        this.container = container;
    }
}
