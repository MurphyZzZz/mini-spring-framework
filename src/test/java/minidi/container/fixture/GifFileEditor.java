package minidi.container.fixture;

import minidi.container.MiniDi;

import javax.inject.Named;

@Named("GifFileEditor")
@MiniDi
public class GifFileEditor implements ImageFileEditor{
    @Override
    public String edit() {
        return "GifFileEditor";
    }
}
