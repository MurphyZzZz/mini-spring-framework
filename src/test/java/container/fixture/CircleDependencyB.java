package container.fixture;

import container.MiniDi;

import javax.inject.Inject;

//@MiniDi
public class CircleDependencyB {
    CircleDependencyA circleDependencyA;

    @Inject
    public CircleDependencyB(CircleDependencyA circleDependencyA){
        this.circleDependencyA = circleDependencyA;
    }
}
