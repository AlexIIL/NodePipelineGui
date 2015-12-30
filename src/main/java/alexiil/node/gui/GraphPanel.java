package alexiil.node.gui;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.vecmath.Point2d;
import javax.vecmath.Point2i;

import alexiil.node.gui.disp.DisplayableNode;
import alexiil.node.gui.disp.NodeConnector;

@SuppressWarnings("serial")
class GraphPanel extends JPanel {
    private static final BufferedImage background;

    static {
        String loc = "/alexiil/node/gui/img/background.png";
        InputStream stream = DisplayableNode.class.getResourceAsStream(loc);
        try {
            background = ImageIO.read(stream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load the image from " + loc, e);
        }
    }

    private final EditiableNodeGraph graph;

    GraphPanel(LayoutManager layout, EditiableNodeGraph graph) {
        super(layout);
        this.graph = graph;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(250, 200);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, getWidth(), getHeight(), 0, 0, 200, 200, null);

        drawConnections(g, 0x9D / 256f, 16);

        for (DisplayableNode node : graph.nodes) {
            node.render(g, node == graph.getSelected());
        }

        drawConnections(g, 1, 6);
    }

    private void drawConnections(Graphics g, float colourMultiplier, float width) {
        colourMultiplier /= 255;
        // Draw the already connected connections

        // Draw the dragged connection

        if (graph.ioSelected != null) {
            NodeConnector conn;
            if (graph.selectedInput != -1) {
                conn = graph.ioSelected.inputs.get(graph.selectedInput);
            } else if (graph.selectedOutput != -1) {
                conn = graph.ioSelected.outputs.get(graph.selectedOutput);
            } else {
                System.out.println("IO SELECTED BUT NO I OR O");
                return;
            }
            Point2i from = new Point2i();
            from.set(conn.xOffset + 20, conn.yOffset + 20);
            from.add(conn.node.pos);
            Color c = conn.color;

            c = new Color(c.getRed() * colourMultiplier, c.getGreen() * colourMultiplier, c.getBlue() * colourMultiplier);

            drawConnection(g, c, width, from, new Point2i(getMousePosition().x, getMousePosition().y));
            System.out.println("IO selected @ " + getMousePosition());
        } else {
            System.out.println("NO IO SELECTED");
        }

    }

    private void drawConnection(Graphics g, Color colour, float width, Point2i from, Point2i to) {
        Path2D.Double path = new Path2D.Double();
        path.moveTo(from.x, from.y);

        Point2d bezOne = new Point2d((from.x * 2 + to.x) / 3d, from.y);
        Point2d bezTwo = new Point2d((from.x + 2 * to.x) / 3d, to.y);
        path.curveTo(bezOne.x, bezOne.y, bezTwo.x, bezTwo.y, to.x, to.y);

        Graphics2D g2d = (Graphics2D) g;
        g.setColor(colour);
        g2d.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.draw(path);

        g.setColor(Color.WHITE);
    }
}
