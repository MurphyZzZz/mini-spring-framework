package fixture;

import javax.inject.Inject;
import javax.inject.Named;

public class ImageFileProcessor {
    public ImageFileEditor imageFileEditor;
    @Inject
    public ImageFileProcessor(@Named("PngFileEditor") ImageFileEditor imageFileEditor) {
        this.imageFileEditor = imageFileEditor;
    }
}
