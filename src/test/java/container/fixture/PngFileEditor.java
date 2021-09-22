package container.fixture;

import container.MiniDi;

import javax.inject.Named;

@Named("PngFileEditor")
@MiniDi
public class PngFileEditor implements ImageFileEditor{
    @Override
    public String edit() {
        return "PngFileEditor";
    }
}
