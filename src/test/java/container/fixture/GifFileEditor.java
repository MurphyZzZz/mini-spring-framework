package container.fixture;

import container.MiniDi;

import javax.inject.Named;

@Named("GifFileEditor")
@MiniDi
public class GifFileEditor implements ImageFileEditor{
    @Override
    public String edit() {
        return "GifFileEditor";
    }
}
