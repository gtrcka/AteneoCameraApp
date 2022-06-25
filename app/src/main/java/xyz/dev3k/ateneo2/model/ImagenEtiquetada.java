package xyz.dev3k.ateneo2.model;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;

import java.util.List;

public class ImagenEtiquetada {
    private String id;
    private InputImage image;
    private List<ImageLabel> labels;

    public ImagenEtiquetada(String id, InputImage image, List<ImageLabel> labels) {
        this.id = id;
        this.image = image;
        this.labels = labels;
    }

    public ImagenEtiquetada(String id, List<ImageLabel> labels) {
        this.id = id;
        this.labels = labels;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public InputImage getImage() {
        return image;
    }

    public void setImage(InputImage image) {
        this.image = image;
    }

    public List<ImageLabel> getLabel() {
        return labels;
    }

    public void setLabel(List<ImageLabel> labels) {
        this.labels = labels;
    }
}
