package alexiil.node.gui;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import alexiil.node.gui.disp.DispLongValueNode;

/** Holds all the graphical components required for editing a graph.
 *
 * @author AlexIIL */
public class GraphEditor implements KeyListener, MouseListener, MouseMotionListener {
    private final EditiableNodeGraph graph;
    private JFrame frame;
    private JPanel outer, inner;
    private Graphics2D graphics;

    public GraphEditor(EditiableNodeGraph graph) {
        this.graph = graph;
        DispLongValueNode node = new DispLongValueNode();
        graph.nodes.add(node);
        try {
            EventQueue.invokeAndWait(() -> {
                frame = new JFrame();
                frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                frame.setBackground(Color.LIGHT_GRAY);
                frame.setTitle("Graph Editor");
                frame.addKeyListener(this);

                outer = new JPanel(new BorderLayout());
                outer.setBackground(Color.LIGHT_GRAY);
                frame.setContentPane(outer);

                inner = new GraphPanel(new BorderLayout(), graph);
                inner.setBackground(Color.LIGHT_GRAY);
                outer.add(inner, BorderLayout.CENTER);

                inner.addKeyListener(this);
                inner.addMouseListener(this);
                inner.addMouseMotionListener(this);

                frame.pack();
                frame.setVisible(true);
            });
        } catch (HeadlessException | InvocationTargetException | InterruptedException e) {
            throw new RuntimeException("Failed to open the window!", e);
        }
    }

    // @formatter:off
    @Override public void mouseDragged(MouseEvent e)  { processEvent(e); } 
    @Override public void mouseMoved(MouseEvent e)    { processEvent(e); } 
    @Override public void mouseClicked(MouseEvent e)  { processEvent(e); } 
    @Override public void mousePressed(MouseEvent e)  { processEvent(e); } 
    @Override public void mouseReleased(MouseEvent e) { processEvent(e); } 
    @Override public void mouseEntered(MouseEvent e)  { processEvent(e); } 
    @Override public void mouseExited(MouseEvent e)   { processEvent(e); } 
    @Override public void keyTyped(KeyEvent e)        { processEvent(e); } 
    @Override public void keyPressed(KeyEvent e)      { processEvent(e); } 
    @Override public void keyReleased(KeyEvent e)     { processEvent(e); } 
    // @formatter:on

    private void processEvent(InputEvent event) {
        if (graph.processEvent(event)) {
            inner.repaint();
        }
    }
}
