package container.fixture;

import container.MiniDi;

import javax.inject.Inject;
import javax.inject.Named;

@MiniDi
public class ImageFileProcessor {
    public ImageFileEditor imageFileEditor;
    @Inject
    public ImageFileProcessor(@Named("PngFileEditor") ImageFileEditor imageFileEditor) {
        this.imageFileEditor = imageFileEditor;
    }
}
