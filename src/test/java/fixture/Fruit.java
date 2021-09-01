package fixture;

import javax.inject.Inject;

public class Fruit {
    private Apple apple;

    @Inject
    public Fruit(Apple apple){
        this.apple = apple;
    }
}
