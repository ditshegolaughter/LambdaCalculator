package lambda.gui;

import lambda.*;
import lambda.utils.Bound;
import lambda.utils.Position;
import lambda.parser.Definitions;
import lambda.parser.Definition;

import java.awt.*;

/**
 * Rendering lambda terms.
 */
public class VisitorTermRenderer {
  private Component component;
  private Graphics2D g;
  private Definitions definitions;

  public VisitorTermRenderer(Component component, Graphics2D g, Definitions definitions) {
    this.component = component;
    this.g = g;
    this.definitions = definitions;
  }

  /**
   * Rendering.
   */

  public void render(LambdaTerm term, int x, int y) {
    if(renderCollapsed(term, x, y, Color.GRAY, Color.RED, true)) return;

    if(term instanceof Lambda) renderLambda((Lambda) term, x, y);
    else if(term instanceof Application) renderApplication((Application) term, x, y);
    else if(term instanceof Variable) renderVariable((Variable) term, x, y);
  }

  private boolean renderCollapsed(LambdaTerm term, int x, int y, Color color, Color colorCollapsedRedex, boolean drawFrame) {
    Definition definition = term.isDefined(definitions);
    if(definition == null && term.isExpanded()) return false;

    String name = definition != null ? definition.getName() : "...";

    FontMetrics fontMetrics = g.getFontMetrics();
    Bound bound = term.getBound();

    g.setColor(term.isExpanded() || !term.hasRedexDeep() ? color : colorCollapsedRedex);
    if(drawFrame) g.drawRect(x + bound.x, y + bound.y, bound.width, bound.height);
    int width = fontMetrics.stringWidth(name);
    int height = fontMetrics.getHeight();
    g.drawString(name, x + bound.x + bound.width - width - 2, y + height - 2);
    g.setColor(Color.BLACK);

    return !term.isExpanded();
  }

  public void renderLambda(Lambda lambda, int x, int y) {
    Bound innerBound = lambda.getInnerBound();
    drawName(lambda.getName(), x + innerBound.x + 2, y + innerBound.height - 4);

    int tx = x + VisitorBoundUpdater.getTermX(lambda);
    int ty = y + VisitorBoundUpdater.getTermY(lambda);

    g.drawLine(x, y + lambda.getInnerBound().height + 1, tx, ty);

    render(lambda.getTerm(), tx, ty);
  }

  private void drawName(String name, int x, int y) {
    g.drawString("[" + name + "]", x, y);
  }

  public void renderApplication(Application application, int x, int y) {
    int lx = x + VisitorBoundUpdater.getLeftX(application);
    int ly = y + VisitorBoundUpdater.getLeftY(application);
    int rx = x + VisitorBoundUpdater.getRightX(application);
    int ry = y + VisitorBoundUpdater.getRightY(application);

    g.drawLine(x, y, lx, ly);
    g.drawLine(x, y, rx, ry);

    drawDot(application, x, y, application.isRedex() ? Color.RED : Color.BLACK, Color.BLACK);

    render(application.getLeft(), lx, ly);
    render(application.getRight(), rx, ry);
  }

  private void drawDot(LambdaTerm term, int x, int y, Color colorRedex, Color colorDot) {
    if(term.isRedex()) {
      g.setColor(colorRedex);
      g.fillOval(x-4, y-4, 9, 9);
    }
    g.setColor(colorDot);
    g.fillOval(x-2, y-2, 5, 5);
    g.setColor(Color.BLACK);
  }

  public void renderVariable(Variable variable, int x, int y) {
    Bound bound = variable.getBound();
    g.drawString(variable.getName(), x + bound.x + 2, y + bound.height - 4);
  }

  /**
   * Mouseover position.
   */
  
  public Position getMouseOverPosition(LambdaTerm term, int mx, int my, int x, int y) {
    if(!term.isExpanded()) {
      if(term.getBound().contains(mx - x,my - y)) return new Position();
      return null;
    }

    if(term instanceof Lambda) return getMouseOverPositionLambda((Lambda) term, mx, my, x, y);
    else if(term instanceof Application) return getMouseOverPositionApplication((Application) term, mx, my, x, y);
    else if(term instanceof Variable) return getMouseOverPositionVariable((Variable) term, mx, my, x, y);

    return null;
  }

  public Position getMouseOverPositionLambda(Lambda lambda, int mx, int my, int x, int y) {
    if(!lambda.getBound().contains(mx - x,my - y)) return null;

    if(lambda.getInnerBound().contains(mx - x,my - y)) return new Position();

    int tx = x + VisitorBoundUpdater.getTermX(lambda);
    int ty = y + VisitorBoundUpdater.getTermY(lambda);
    Position position = getMouseOverPosition(lambda.getTerm(), mx, my, tx, ty);
    if(position != null) position.add(0);

    return position;
  }

  public Position getMouseOverPositionApplication(Application application, int mx, int my, int x, int y) {
    int dx = mx - x;
    int dy = my - y;

    if(Math.sqrt(dx*dx + dy*dy) < 7) return new Position();

    if(!application.getBound().contains(mx - x,my - y)) return null;

    int lx = x + VisitorBoundUpdater.getLeftX(application);
    int ly = y + VisitorBoundUpdater.getLeftY(application);
    int rx = x + VisitorBoundUpdater.getRightX(application);
    int ry = y + VisitorBoundUpdater.getRightY(application);

    Position positionLeft = getMouseOverPosition(application.getLeft(), mx, my, lx, ly);
    if(positionLeft != null) positionLeft.add(0);
    Position positionRight = getMouseOverPosition(application.getRight(), mx, my, rx, ry);
    if(positionRight != null) positionRight.add(1);

    return positionLeft != null ? positionLeft : positionRight;
  }

  public Position getMouseOverPositionVariable(Variable variable, int mx, int my, int x, int y) {
    if(variable.getBound().contains(mx - x,my - y)) return new Position(); else return null;
  }

  /**
   * Highlighting.
   */
  public void highlight(LambdaTerm term, Position position, int x, int y, boolean onOff) {
    if(!term.isExpanded()) {
      if(position.length() != 0) return;
      Bound bound = term.getBound();
      drawHighlight(x + bound.x, y + bound.y, bound.width, bound.height, onOff);
      renderCollapsed(term, x, y, Color.BLACK, Color.RED, !onOff);
      return;
    }

    if(term instanceof Lambda) highlight((Lambda) term, position, x, y, onOff);
    else if(term instanceof Application) highlight((Application) term, position, x, y, onOff);
    else if(term instanceof Variable) highlight((Variable) term, position, x, y, onOff);
  }

  public void highlight(Lambda lambda, Position position, int x, int y, boolean onOff) {
    if(position.length() > 0) {
      int tx = x + VisitorBoundUpdater.getTermX(lambda);
      int ty = y + VisitorBoundUpdater.getTermY(lambda);

      position.remove();
      highlight(lambda.getTerm(), position, tx, ty, onOff);
    } else {
      Bound bound = lambda.getInnerBound();
      drawHighlight(x + bound.x, y + bound.y, bound.width, bound.height, onOff);
      g.setColor(Color.BLACK);
      drawName(lambda.getName(), x + bound.x + 2, y + bound.height - 4);
    }
  }

  public void highlight(Application application, Position position, int x, int y, boolean onOff) {
    LambdaTerm left = application.getLeft();
    LambdaTerm right = application.getRight();

    int lx = x + VisitorBoundUpdater.getLeftX(application);
    int ly = y + VisitorBoundUpdater.getLeftY(application);
    int rx = x + VisitorBoundUpdater.getRightX(application);
    int ry = y + VisitorBoundUpdater.getRightY(application);

    if(position.length() > 0) {
      if(position.remove() == 0) {
        highlight(left, position, lx, ly, onOff);
      } else {
        highlight(right, position, rx, ry, onOff);
      }
    } else {
      drawHighlight(x-5, y-5, 11, 11, onOff);
      drawDot(application, x, y, onOff ? new Color(192, 239, 192) : application.isRedex() ? Color.RED : Color.BLACK, Color.BLACK);
    }
  }

  public void highlight(Variable variable, Position position, int x, int y, boolean onOff) {
    if(position.length() != 0) return;

    Bound bound = variable.getBound();
    drawHighlight(x + bound.x, y + bound.y, bound.width, bound.height, onOff);
    g.setColor(Color.BLACK);
    g.drawString(variable.getName(), x + bound.x + 2, y + bound.height - 4);
  }

  public void drawHighlight(int x, int y, int width, int height, boolean onOff) {
    g.clearRect(x-1, y-1, width+2, height+2);
    if(onOff) {
      g.setColor(new Color(192, 239, 192));
      g.fillRoundRect(x-1, y-1, width+1, height+1,15,15);
      g.setColor(new Color(0, 128, 0));
      g.drawRoundRect(x-1, y-1, width+1, height+1,15,15);
    } else {
      component.repaint(x-6, y-6, width+12, height+12);
    }
  }
}
