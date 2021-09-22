package container.fixture;

import container.MiniDi;

import javax.inject.Inject;

@MiniDi
public class Fruit {
    private Apple apple;

    @Inject
    public Fruit(Apple apple){
        this.apple = apple;
    }
}
