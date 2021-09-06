package fixture;

import javax.inject.Inject;

public class CircleDependencyA {
    CircleDependencyB circleDependencyB;

    @Inject
    public CircleDependencyA(CircleDependencyB circleDependencyB) {
        this.circleDependencyB = circleDependencyB;
    }
}
