package container.fixture;

import container.MiniDi;

import javax.inject.Inject;

//@MiniDi
public class CircleDependencyA {
    CircleDependencyB circleDependencyB;

    @Inject
    public CircleDependencyA(CircleDependencyB circleDependencyB) {
        this.circleDependencyB = circleDependencyB;
    }
}
