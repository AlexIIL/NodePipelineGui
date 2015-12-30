package alexiil.node.gui.disp;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Map.Entry;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import org.apache.commons.lang3.tuple.ImmutablePair;

import alexiil.node.gui.disp.DisplayableNode.EnumSelected;
import alexiil.node.gui.state.EnumAttached;
import alexiil.node.gui.state.ImageState;

public class NodeConnector {
    private static final ImageState BASE_STATE;
    private static LoadingCache<Entry<ImageState, Color>, BufferedImage> colourImages = CacheBuilder.newBuilder().build(CacheLoader.from(
        NodeConnector::create));

    static {
        String start = "/alexiil/node/gui/img/connection_node";
        BASE_STATE = new ImageState(start + ".png", EnumAttached.DETACHED, EnumSelected.NOT_SELECTED);
        BASE_STATE.addState(start + "_attached.png", EnumAttached.ATTACHED, EnumSelected.NOT_SELECTED);
        BASE_STATE.addState(DisplayableNode.modifyForSelection(start + ".png"), EnumAttached.DETACHED, EnumSelected.SELECTED);
        BASE_STATE.addState(DisplayableNode.modifyForSelection(start + "_attached.png"), EnumAttached.ATTACHED, EnumSelected.SELECTED);
    }

    public static BufferedImage create(Entry<ImageState, Color> key) {
        ImageState state = key.getKey();
        Color color = key.getValue();
        float r = color.getRed() / 255f;
        float g = color.getGreen() / 255f;
        float b = color.getBlue() / 255f;

        BufferedImage unmodified = state.image;
        BufferedImage modified = new BufferedImage(unmodified.getWidth(), unmodified.getHeight(), unmodified.getType());

        for (int x = 0; x < unmodified.getWidth(); x++) {
            for (int y = 0; y < unmodified.getHeight(); y++) {
                int from = unmodified.getRGB(x, y);
                // @formatter:off
                int to = ((int) (b * (from & 0x0000FF)) & 0x000000FF)
                       + ((int) (g * (from & 0x00FF00)) & 0x0000FF00) 
                       + ((int) (r * (from & 0xFF0000)) & 0x00FF0000) 
                       + (            from              & 0xFF000000);
                // @formatter:on
                modified.setRGB(x, y, to);
            }
        }
        return modified;
    }

    public boolean connected = false, selected = false;
    public final DisplayableNode node;
    public int xOffset, yOffset;
    public final Color color;

    public NodeConnector(DisplayableNode node, Color color) {
        this.node = node;
        this.color = color;
    }

    public boolean isPointInside(Point point) {
        ImageState state = BASE_STATE.getState(connected ? EnumAttached.ATTACHED : EnumAttached.DETACHED);
        int minX = node.pos.x - state.meta.origin.x + xOffset;
        int maxX = minX + state.meta.size.x;
        if (minX > point.x || maxX < point.x)
            return false;

        int minY = node.pos.y - state.meta.origin.y + yOffset;
        int maxY = minY + state.meta.size.y;
        if (minY > point.y || maxY < point.y)
            return false;

        return true;
    }

    public void render(Graphics g) {
        ImageState state = BASE_STATE.getState(selected ? EnumAttached.ATTACHED : EnumAttached.DETACHED);
        state = state.getState(connected ? EnumSelected.SELECTED : EnumSelected.NOT_SELECTED);

        BufferedImage image = colourImages.getUnchecked(ImmutablePair.of(state, color));
        g.drawImage(image, node.pos.x + xOffset, node.pos.y + yOffset, null);
    }
}
