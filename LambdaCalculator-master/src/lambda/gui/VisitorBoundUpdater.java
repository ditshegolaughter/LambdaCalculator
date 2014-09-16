package lambda.gui;

import lambda.*;
import lambda.utils.Bound;
import lambda.utils.LambdaTermVisitorVoid;
import lambda.parser.Definitions;
import lambda.parser.Definition;

import java.awt.*;

/**
 * Update bounds of lambda terms.
 */
public class VisitorBoundUpdater implements LambdaTermVisitorVoid {
  private FontMetrics fontMetrics;
  private Definitions definitions;

  public VisitorBoundUpdater(FontMetrics fontMetrics, Definitions definitions) {
    this.fontMetrics = fontMetrics;
    this.definitions = definitions;
  }

  private boolean checkCollapsed(LambdaTerm term) {
    if(!term.isExpanded()) {
      Definition definition = term.isDefined(definitions);
      String name = definition != null ? definition.getName() : "...";
      setBound(term.getBound(), name);
      return true;
    }
    return false;
  }

  public void visit(LambdaTerm term) {
    throw new RuntimeException("Unkown LambdaTerm.");
  }

  public void visit(Lambda lambda) {
    if(checkCollapsed(lambda)) return;

    lambda.getTerm().visit(this);

    Bound bound = lambda.getBound();
    Bound innerBound = lambda.getInnerBound();

    setBound(bound, "[" + lambda.getName() + "]");
    innerBound.x = bound.x;
    innerBound.y = bound.y;
    innerBound.width = bound.width;
    innerBound.height = bound.height;
    bound.union(lambda.getTerm().getBound(), getTermX(lambda), getTermY(lambda));

    bound.x -= 2; bound.y -= 2; bound.width += 4; bound.height += 4;
  }

  protected static int getTermX(Lambda lambda) { return 0; }
  protected static int getTermY(Lambda lambda) { return lambda.getInnerBound().height + 13; }
  
  public void visit(Application application) {
    Bound bound = application.getBound();

    if(checkCollapsed(application)) {
      return;
    } else {
      bound.x = bound.y = bound.width = bound.height = 0;
    }

    LambdaTerm left = application.getLeft();
    left.visit(this);
    LambdaTerm right = application.getRight();
    right.visit(this);

    bound.union(left.getBound(), getLeftX(application), getLeftY(application));
    bound.union(right.getBound(), getRightX(application), getRightY(application));

    bound.x -= 2; bound.y -= 5; bound.width += 4; bound.height += 7;
  }

  protected static int getLeftX(Application application) {
    Bound leftBound = application.getLeft().getBound();
    return -leftBound.width - leftBound.x - 5;
  }
  protected static int getLeftY(Application application) {
    return 15 + application.getLeft().getBound().width/20;
  }
  protected static int getRightX(Application application) {
    Bound rightBound = application.getRight().getBound();
    return -rightBound.x; 
  }
  protected static int getRightY(Application application) {
    return 15 + application.getRight().getBound().width/20;
  }

  public void visit(Variable variable) {
    if(checkCollapsed(variable)) return;

    setBound(variable.getBound(), variable.getName());
  }

  /**
   * Bound for a string.
   *
   * @param bound bound
   * @param string string
   */
  private void setBound(Bound bound, String string) {
    int width = fontMetrics.stringWidth(string) + 4;
    int height = fontMetrics.getHeight();

    bound.x = -width/2;
    bound.width = width;
    bound.y = 0;
    bound.height = height;
  }
}
