package main;

import lambda.LambdaTerm;
import lambda.Variable;
import lambda.actions.TermAtPosition;
import lambda.actions.Rewrite;
import lambda.actions.HeadRedex;
import lambda.gui.VisitorBoundUpdater;
import lambda.gui.VisitorTermRenderer;
import lambda.gui.VisitorCollapseDefined;
import lambda.parser.Definitions;
import lambda.parser.Definition;
import lambda.utils.Position;
import lambda.utils.Bound;

import javax.sound.sampled.Line;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;

/**
 * Panel showing lambda terms.
 */
public class LambdaPanel extends JPanel implements MouseMotionListener, MouseListener, MouseWheelListener {
  private Definitions definitions = new Definitions(new Definition[0]);

  private LambdaTerm term = new Variable("Lambda Calculator");

  private JTextArea log;

  public int x = Math.max(250,getWidth()/2), y = 25;

  private Point mousePosition = new Point();
  private Position mouseoverPosition = null;

  private float zoom = 1;

  private Color COLOR_GREEN = new Color(192, 239, 192);
  private Color COLOR_DARK_GREEN = new Color(0, 128, 0);

  private Polygon naviUp   = new Polygon(new int[] {38,45,52}, new int[] {33,26,33}, 3);
  private Polygon naviDown = new Polygon(new int[] {38,45,52}, new int[] {90-33,90-26,90-33}, 3);
  private Polygon naviLeft   = new Polygon(new int[] {33,26,33}, new int[] {38,45,52}, 3);
  private Polygon naviRight   = new Polygon(new int[] {90-33,90-26,90-33}, new int[] {38,45,52}, 3);

  private Rectangle zoomOut = new Rectangle(20,80,25,20);
  private Rectangle zoomIn = new Rectangle(45,80,25,20);

  public LambdaPanel(JTextArea log) {
    this.log = log;
    setDoubleBuffered(true);

    addMouseMotionListener(this);
    addMouseListener(this);
    addMouseWheelListener(this);

    setBackground(Color.WHITE);
    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
  }

  public Dimension getPreferredSize() {
    return new Dimension(1024,800);
  }

  public LambdaTerm getTerm() {
    return term;
  }

  public void setTerm(LambdaTerm term, Definitions definitions, boolean collapse) {
    this.term = term;
    this.definitions = definitions;

    if(collapse) term.visit(new VisitorCollapseDefined(definitions));

    repaint();
  }

  public void setZoom(float zoom, int centerX, int centerY) {
    double vx = (double) centerX / this.zoom;
    double vy = (double) centerY / this.zoom;

    x -= vx - centerX/zoom;
    y -= vy - centerY/zoom;

    this.zoom = zoom;
    repaint();
  }

  public void headStep() {
    Position position = term.visit(new HeadRedex(), null);
    if(position != null) {
      setTerm(term.visit(new Rewrite(), position.copy()), definitions, false);
      log.append(term.toString(definitions));
      log.append("\n");
    }
  }

  public void paint(Graphics graphics) {
    super.paint(graphics);

    Graphics2D g = (Graphics2D) graphics;
    AffineTransform transform = g.getTransform();

    g.scale(zoom, zoom);
    g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

    term.visit(new VisitorBoundUpdater(graphics.getFontMetrics(),  definitions));

    VisitorTermRenderer renderer = new VisitorTermRenderer(this, (Graphics2D) graphics, definitions);
    renderer.render(term, x, y);
    if(mouseoverPosition != null) renderer.highlight(term, mouseoverPosition.copy(), x, y, true);

    g.setTransform(transform);
    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.5f));
    g.setColor(COLOR_GREEN);
    g.fillOval(20,20,50,50);
    g.fillRoundRect(20,80,50,20,9,9);
    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP));
    g.setColor(COLOR_DARK_GREEN);
    g.setStroke(new BasicStroke(2));
    g.drawOval(20,20,50,50);
    g.drawRoundRect(20,80,50,20,9,9);

    g.fillPolygon(naviUp);
    g.fillPolygon(naviDown);
    g.fillPolygon(naviLeft);
    g.fillPolygon(naviRight);

    g.setStroke(new BasicStroke(2));
    g.drawLine(45,82,45,98);
    g.setStroke(new BasicStroke(3));
    g.drawLine(28,90,37,90);
    g.drawLine(53,90,63,90);
    g.drawLine(58,85,58,95);
  }

  Point drag = new Point();
  public void mouseDragged(MouseEvent mouseEvent) {
    x += (mouseEvent.getX() - drag.getX())/zoom;
    y += (mouseEvent.getY() - drag.getY())/zoom;
    drag = mouseEvent.getPoint();
    repaint();
  }

  private Rectangle extend(Rectangle rect, int size) {
    return new Rectangle(rect.x - size, rect.y - size, rect.width + 2*size, rect.height + 2*size);
  }

  public void mouseMoved(MouseEvent mouseEvent) {
    mousePosition = mouseEvent.getPoint();

    VisitorTermRenderer renderer = new VisitorTermRenderer(this, null, definitions);
    Position position = renderer.getMouseOverPosition(term, (int) (mousePosition.x/zoom), (int) (mousePosition.y/zoom), x, y);

    boolean changed = false;
    if(mouseoverPosition != null && position == null) changed = true;
    else if(mouseoverPosition == null && position != null) changed = true;
    else if(mouseoverPosition != null && position != null && !mouseoverPosition.equals(position)) changed = true;

    if(changed) {
      mouseoverPosition = position;
      setCursor(Cursor.getPredefinedCursor(mouseoverPosition != null ? Cursor.DEFAULT_CURSOR : Cursor.HAND_CURSOR));
      repaint();
    }
  }

  public void mouseClicked(MouseEvent mouseEvent) {
    if(extend(naviUp.getBounds(),5).contains(mouseEvent.getPoint())) { y -= 50; repaint(); return; }
    if(extend(naviDown.getBounds(),5).contains(mouseEvent.getPoint())) { y += 50; repaint(); return; }
    if(extend(naviLeft.getBounds(),5).contains(mouseEvent.getPoint())) { x -= 50; repaint(); return; }
    if(extend(naviRight.getBounds(),5).contains(mouseEvent.getPoint())) { x += 50; repaint(); return; } 

    if(zoomOut.contains(mouseEvent.getPoint())) {
      setZoom(Math.max(0.01f, Math.min(zoom * 0.8f, 1)), getWidth()/2, getHeight()/2);
      return;
    }
    if(zoomIn.contains(mouseEvent.getPoint())) {
      setZoom(Math.max(0.01f, Math.min(zoom * 1.2f, 1)), getWidth()/2, getHeight()/2);
      return;
    }

    if(mouseoverPosition == null) return;

    LambdaTerm termAtPosition = term.visit(new TermAtPosition(), mouseoverPosition.copy());
    if(mouseEvent.getButton() == MouseEvent.BUTTON1 && termAtPosition.isExpanded()) {
      if(!termAtPosition.isRedex()) {
        Position position = termAtPosition.visit(new HeadRedex(), null);
        if(position != null) mouseoverPosition.append(position);
      }
      setTerm(term.visit(new Rewrite(), mouseoverPosition.copy()), definitions, false);

      log.append(term.toString(definitions));
      log.append("\n");
    } else {
      termAtPosition.setExpanded(!termAtPosition.isExpanded());
    }

    mouseoverPosition = null;
    repaint();
  }

  public void mousePressed(MouseEvent mouseEvent) {
    drag = mouseEvent.getPoint();
  }

  public void mouseReleased(MouseEvent mouseEvent) {
  }

  public void mouseEntered(MouseEvent mouseEvent) {
  }

  public void mouseExited(MouseEvent mouseEvent) {
  }

  public void mouseWheelMoved(MouseWheelEvent e) {
    setZoom(Math.max(0.01f, Math.min((float) (zoom * (1 - e.getWheelRotation()/25.)), 1)), e.getX(), e.getY());
  }
}
