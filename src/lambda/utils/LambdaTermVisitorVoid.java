package lambda.utils;

import lambda.Lambda;
import lambda.Application;
import lambda.Variable;
import lambda.LambdaTerm;

/**
 * Visitor for LambtaTerm with void return.
 */
public interface LambdaTermVisitorVoid {
  public void visit(LambdaTerm term);
  public void visit(Lambda lambda);
  public void visit(Application application);
  public void visit(Variable variable);
}
