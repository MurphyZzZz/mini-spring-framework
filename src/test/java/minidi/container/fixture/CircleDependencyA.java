package minidi.container.fixture;

import javax.inject.Inject;

//@MiniDi
public class CircleDependencyA {
    CircleDependencyB circleDependencyB;

    @Inject
    public CircleDependencyA(CircleDependencyB circleDependencyB) {
        this.circleDependencyB = circleDependencyB;
    }
}
