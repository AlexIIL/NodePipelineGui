package alexiil.node.gui;

import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point2i;

import alexiil.node.gui.disp.DisplayableNode;
import alexiil.node.gui.disp.NodeConnector;

public class EditiableNodeGraph {
    protected final List<DisplayableNode> nodes = new ArrayList<>();
    public DisplayableNode selected = null, ioSelected = null;
    public int selectedInput = -1, selectedOutput = -1;
    private Point2i draggingDiff = null;

    public DisplayableNode getSelected() {
        return selected;
    }

    public boolean processEvent(InputEvent event) {
        if (event instanceof MouseEvent) {
            MouseEvent mouse = (MouseEvent) event;
            if (mouse.getID() == MouseEvent.MOUSE_PRESSED) {
                DisplayableNode under = getUnder(mouse.getPoint());
                selected = under;
                for (DisplayableNode node : nodes) {
                    int i = 0;
                    for (NodeConnector conn : node.inputs) {
                        if (conn.isPointInside(mouse.getPoint())) {
                            selectedInput = i;
                            conn.selected = true;
                            ioSelected = node;
                            selected = null;
                            return true;
                        }
                        i++;
                    }
                    i = 0;
                    for (NodeConnector conn : node.outputs) {
                        if (conn.isPointInside(mouse.getPoint())) {
                            selectedOutput = i;
                            conn.selected = true;
                            ioSelected = node;
                            selected = null;
                            return true;
                        }
                        i++;
                    }
                }
                return true;
            } else if (mouse.getID() == MouseEvent.MOUSE_DRAGGED) {
                if (selected != null) {
                    Point2i mousePos = new Point2i(mouse.getX(), mouse.getY());
                    if (draggingDiff == null) {
                        draggingDiff = new Point2i(mouse.getX() - selected.pos.x, mouse.getY() - selected.pos.y);
                    }
                    Point2i newPos = new Point2i();
                    newPos.add(mousePos);
                    newPos.sub(draggingDiff);
                    if (!newPos.equals(selected.pos)) {
                        selected.pos.set(newPos);
                        return true;
                    }
                } else if (ioSelected != null) {
                    if (selectedInput != -1) {

                        return true;
                    } else if (selectedOutput != -1) {

                        return true;
                    }
                }
            } else if (mouse.getID() == MouseEvent.MOUSE_RELEASED) {
                System.out.println(ioSelected);
                if (ioSelected != null) {
                    if (selectedInput != -1) {
                        ioSelected.inputs.get(selectedInput).selected = false;
                        selectedInput = -1;
                    }
                    if (selectedOutput != -1) {
                        ioSelected.outputs.get(selectedOutput).selected = false;
                        selectedOutput = -1;
                    }
                    ioSelected = null;
                    return true;
                }
                if (selected != null) {
                    selected = null;
                    draggingDiff = null;
                    return true;
                }
            }
        }

        return false;
    }

    private DisplayableNode getUnder(Point mouse) {
        for (DisplayableNode node : nodes) {
            if (node.isPointInside(mouse))
                return node;
        }
        return null;
    }
}
