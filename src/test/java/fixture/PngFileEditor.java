package fixture;

import javax.inject.Named;

@Named("PngFileEditor")
public class PngFileEditor implements ImageFileEditor{
    @Override
    public String edit() {
        return "PngFileEditor";
    }
}
