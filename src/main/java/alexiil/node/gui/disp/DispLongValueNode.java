package alexiil.node.gui.disp;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import alexiil.node.gui.state.EnumAttached;
import alexiil.node.gui.state.ImageState;

public class DispLongValueNode extends DisplayableNode {
    private static final ImageState START_STATE;
    private static final int TEXT_SIZE = 20, TEXT_OFFSET_X = 20, TEXT_OFFSET_Y = 47;

    static {
        String start = "/alexiil/node/gui/img/value_long";
        START_STATE = new ImageState(start + ".png", EnumAttached.DETACHED, EnumSelected.NOT_SELECTED);
        START_STATE.addState(start + "_attached.png", EnumAttached.ATTACHED, EnumSelected.NOT_SELECTED);
        START_STATE.addState(modifyForSelection(start + ".png"), EnumAttached.DETACHED, EnumSelected.SELECTED);
        START_STATE.addState(modifyForSelection(start + "_attached.png"), EnumAttached.ATTACHED, EnumSelected.SELECTED);
    }

    public DispLongValueNode() {
        NodeConnector out = new NodeConnector(this, Color.ORANGE);
        out.xOffset = 180;
        out.yOffset = 20;
        outputs.add(out);
    }

    @Override
    protected ImageState getStartState() {
        return START_STATE;
    }

    @Override
    public void render(Graphics g, boolean selected) {
        super.render(g, selected);
        g.setFont(g.getFont().deriveFont(Font.PLAIN, TEXT_SIZE));
        g.drawString("10", pos.x + TEXT_OFFSET_X, pos.y + TEXT_OFFSET_Y);
    }
}
