package fixture;

import javax.inject.Named;

@Named("GifFileEditor")
public class GifFileEditor implements ImageFileEditor{
    @Override
    public String edit() {
        return "GifFileEditor";
    }
}
