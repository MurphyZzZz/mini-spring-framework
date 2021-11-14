package container.application;

import container.Container;
import container.MiniDi;

import javax.inject.Inject;

@MiniDi
public class MyApplication {

    Container container;

    @Inject
    public MyApplication(Container container) {
        this.container = container;
    }
}
