package lambda.actions;

import lambda.LambdaTerm;
import lambda.Lambda;
import lambda.Application;
import lambda.Variable;
import lambda.utils.Position;
import lambda.utils.LambdaTermVisitor;

/**
 * Returns the term at the given position.
 */
public class TermAtPosition implements LambdaTermVisitor<LambdaTerm, Position> {
  public LambdaTerm visit(LambdaTerm term, Position position) {
    throw new RuntimeException("Unkown LambdaTerm.");
  }

  public LambdaTerm visit(Lambda lambda, Position position) {
    if(position.length() == 0) return lambda;

    position.remove();
    return  lambda.getTerm().visit(this, position);
  }

  public LambdaTerm visit(Application application, Position position) {
    LambdaTerm left = application.getLeft();
    LambdaTerm right = application.getRight();

    if(position.length() == 0) {
      return application;
    } else {
      return  position.remove() == 0 ? left.visit(this, position) : right.visit(this, position);
    }
  }

  public LambdaTerm visit(Variable variable, Position position) {
    return variable;
  }
}
