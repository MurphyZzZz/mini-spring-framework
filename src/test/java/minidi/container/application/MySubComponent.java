package minidi.container.application;

import minidi.container.MiniDi;
import lombok.NoArgsConstructor;

@MiniDi
@NoArgsConstructor
public class MySubComponent {

    public String getMyClassName() {
       return this.getClass().getSimpleName();
    }
}
