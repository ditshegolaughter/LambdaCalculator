package lambda.actions;

import lambda.utils.LambdaTermVisitor;
import lambda.utils.Position;
import lambda.LambdaTerm;
import lambda.Lambda;
import lambda.Application;
import lambda.Variable;

/**
 * Returns the head redex.
 */
public class HeadRedex implements LambdaTermVisitor<Position,Object> {
  public Position visit(LambdaTerm term, Object o) {
    throw new RuntimeException("Unkown LambdaTerm.");
  }

  public Position visit(Lambda lambda, Object o) {
    Position headRedex = lambda.getTerm().visit(this, o);
    if(headRedex != null) headRedex.add(0);
    return headRedex;
  }

  public Position visit(Application application, Object o) {
    LambdaTerm left = application.getLeft();
    LambdaTerm right = application.getRight();

    if(application.isRedex()) return new Position();
    
    Position headRedex = null;
    if(left.hasRedexDeep()) {
      headRedex = left.visit(this, o);
      headRedex.add(0);
      return headRedex;
    }
    if(right.hasRedexDeep()) {
      headRedex = right.visit(this, o);
      headRedex.add(1);
      return headRedex;
    }

    return null;
  }

  public Position visit(Variable variable, Object o) {
    return null;
  }
}
