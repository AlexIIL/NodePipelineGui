package alexiil.node.gui;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.vecmath.Point2i;

import com.google.gson.Gson;

public class ResourceManager {
    private static final Map<String, BufferedImage> images = new HashMap<>();
    private static final Map<String, ImageMeta> imageMetas = new HashMap<>();

    public static void setImage(String loc, BufferedImage image) {
        images.put(loc, image);
    }

    public static void setImageMeta(String loc, ImageMeta meta) {
        imageMetas.put(loc, meta);
    }

    public static BufferedImage loadImageCrashing(String loc) {
        try {
            return loadImage(loc);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load the image \"" + loc + "\"", e);
        }
    }

    public static BufferedImage loadImage(String loc) throws IOException {
        if (!images.containsKey(loc)) {
            InputStream stream = ResourceManager.class.getResourceAsStream(loc);
            if (stream == null)
                throw new IOException("Did not find the resource \"" + loc + "\"");
            images.put(loc, ImageIO.read(stream));
        }
        return images.get(loc);
    }

    public static ImageMeta loadImageMetaCrashing(String loc, BufferedImage fallback) {
        try {
            return loadImageMeta(loc, fallback);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load the image \"" + loc + "\"", e);
        }
    }

    public static ImageMeta loadImageMeta(String loc, BufferedImage fallback) throws IOException {
        if (imageMetas.containsKey(loc))
            return imageMetas.get(loc);
        InputStream stream = ResourceManager.class.getResourceAsStream(loc);
        ImageMeta meta;
        if (stream == null) {
            System.out.println("Could not find " + loc);
            meta = new ImageMeta(new Point2i(0, 0), new Point2i(fallback.getWidth(), fallback.getHeight()));
        } else {
            meta = new Gson().fromJson(new InputStreamReader(stream), ImageMeta.class);
        }
        imageMetas.put(loc, meta);
        return meta;
    }
}
