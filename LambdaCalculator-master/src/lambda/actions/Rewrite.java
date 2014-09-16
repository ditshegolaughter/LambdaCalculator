package lambda.actions;

import lambda.LambdaTerm;
import lambda.Lambda;
import lambda.Application;
import lambda.Variable;
import lambda.utils.Position;
import lambda.utils.LambdaTermVisitor;

/**
 * Rewrite a given position of a term.
 */
public class Rewrite implements LambdaTermVisitor<LambdaTerm, Position> {
  public LambdaTerm visit(LambdaTerm term, Position position) {
    throw new RuntimeException("Unkown LambdaTerm.");
  }

  public LambdaTerm visit(Lambda lambda, Position position) {
    lambda.setExpanded(true);

    if(position.length() == 0) return lambda;

    position.remove();
    lambda.setTerm(lambda.getTerm().visit(this, position));

    lambda.updateHash();
    return lambda;
  }

  public LambdaTerm visit(Application application, Position position) {
    application.setExpanded(true);

    LambdaTerm left = application.getLeft();
    LambdaTerm right = application.getRight();

    if(position.length() == 0) {
      if(application.isRedex()) return application.rewrite(); else return application; 
    } else {
      if(position.remove() == 0) {
        application.setLeft(left.visit(this, position));
      } else {
        application.setRight(right.visit(this, position));
      }

      application.updateHash();
      return application;
    }
  }

  public LambdaTerm visit(Variable variable, Position position) {
    variable.setExpanded(true);
    
    return variable;
  }
}
