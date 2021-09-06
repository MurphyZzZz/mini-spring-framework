package fixture;

import javax.inject.Inject;

public class CircleDependencyB {
    CircleDependencyA circleDependencyA;

    @Inject
    public CircleDependencyB(CircleDependencyA circleDependencyA){
        this.circleDependencyA = circleDependencyA;
    }
}
