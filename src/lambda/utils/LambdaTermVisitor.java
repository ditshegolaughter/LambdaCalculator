package lambda.utils;

import lambda.Lambda;
import lambda.Application;
import lambda.Variable;
import lambda.LambdaTerm;

/**
 * Visitor for LambtaTerm
 */
public interface LambdaTermVisitor<T,S> {
  public T visit(LambdaTerm term, S s);
  public T visit(Lambda lambda, S s);
  public T visit(Application application, S s);
  public T visit(Variable variable, S s);
}
