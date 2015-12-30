package alexiil.node.gui.disp;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point2i;

import alexiil.node.gui.ResourceManager;
import alexiil.node.gui.state.ImageState;
import alexiil.node.gui.state.ImageState.IState;

public abstract class DisplayableNode {
    public enum EnumSelected implements IState {
        SELECTED,
        NOT_SELECTED;

        @Override
        public boolean replaces(IState state) {
            return state instanceof EnumSelected;
        }
    }

    protected ImageState currentState = getStartState();
    /** The position of the actual node, independent of the actual graphical icon. */
    public Point2i pos = new Point2i();
    public List<NodeConnector> inputs = new ArrayList<>(), outputs = new ArrayList<>();

    public void render(Graphics g, boolean selected) {
        ImageState state = currentState;
        if (selected) {
            state = state.getState(EnumSelected.SELECTED);
        } else {
            state = state.getState(EnumSelected.NOT_SELECTED);
        }
        g.drawImage(state.image, (int) pos.x - state.meta.origin.x, (int) pos.y - state.meta.origin.y, null);

        inputs.forEach(n -> n.render(g));
        outputs.forEach(n -> n.render(g));
    }

    protected abstract ImageState getStartState();

    public static String modifyForSelection(String loc) {
        BufferedImage unmodified = ResourceManager.loadImageCrashing(loc);
        String modifiedSelection = "$GENERATED$" + loc;
        BufferedImage modified = new BufferedImage(unmodified.getWidth(), unmodified.getHeight(), unmodified.getType());

        for (int x = 0; x < unmodified.getWidth(); x++) {
            for (int y = 0; y < unmodified.getHeight(); y++) {
                int from = unmodified.getRGB(x, y);
                // Temporary selection changer
                // TODO: Change this to be a better selection: like an inverted outline or something
                // @formatter:off
                int to = ((0x00000F + from & 0x000000FF) & 0x0000FF)
                       + ((0x000F00 + from & 0x0000FF00) & 0x00FF00) 
                       + ((0x0F0000 + from & 0x00FF0000) & 0xFF0000) 
                       +  (           from & 0xFF000000);
                // @formatter:on
                modified.setRGB(x, y, to);
            }
        }
        ResourceManager.setImage(modifiedSelection, modified);
        return modifiedSelection;
    }

    public void mouseEvent(MouseEvent event) {

    }

    public void keyEvent(KeyEvent event) {

    }

    public boolean isPointInside(Point point) {
        int minX = pos.x - currentState.meta.origin.x;
        int maxX = minX + currentState.meta.size.x;
        if (minX > point.x || maxX < point.x)
            return false;

        int minY = pos.y - currentState.meta.origin.y;
        int maxY = minY + currentState.meta.size.y;
        if (minY > point.y || maxY < point.y)
            return false;

        return true;
    }
}
