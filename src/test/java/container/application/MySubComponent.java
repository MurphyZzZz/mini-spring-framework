package container.application;

import container.MiniDi;
import lombok.NoArgsConstructor;

@MiniDi
@NoArgsConstructor
public class MySubComponent {

    public String getMyClassName() {
       return this.getClass().getSimpleName();
    }
}
