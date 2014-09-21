package lambda.gui;

import lambda.utils.LambdaTermVisitorVoid;
import lambda.Lambda;
import lambda.Application;
import lambda.Variable;
import lambda.LambdaTerm;
import lambda.parser.Definitions;

/**
 * Collapse defined subterms.
 */
public class VisitorCollapseDefined implements LambdaTermVisitorVoid {
  private Definitions definitions;

  public VisitorCollapseDefined(Definitions definitions) {
    this.definitions = definitions;
  }

  public void visit(LambdaTerm term) {
    throw new RuntimeException("Unkown LambdaTerm.");
  }

  public void visit(Lambda lambda) {
    if(lambda.isDefined(definitions) != null) lambda.setExpanded(false);
    lambda.getTerm().visit(this);
  }

  public void visit(Application application) {
    if(application.isDefined(definitions) != null) application.setExpanded(false);
    application.getLeft().visit(this);
    application.getRight().visit(this);
  }

  public void visit(Variable variable) {
  }
}
