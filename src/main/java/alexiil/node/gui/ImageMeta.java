package alexiil.node.gui;

import javax.vecmath.Point2i;

public class ImageMeta {
    public final Point2i origin;
    public final Point2i size;

    public ImageMeta(Point2i origin, Point2i size) {
        this.origin = origin;
        this.size = size;
    }
}
