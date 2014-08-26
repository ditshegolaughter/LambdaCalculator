package lambda;

import lambda.parser.Definition;
import lambda.parser.Definitions;
import lambda.utils.LambdaTermVisitor;
import lambda.utils.LambdaTermVisitorVoid;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;

/**
 * Application.
 */
public class Application extends LambdaTerm {
  private LambdaTerm left, right;

  public Application(LambdaTerm left, LambdaTerm right) {
    this.left = left;
    this.right = right;
    updateHash();
  }

  public LambdaTerm getLeft() {
    return left;
  }

  public void setLeft(LambdaTerm left) {
    this.left = left;
    updateHash();
  }

  public LambdaTerm getRight() {
    return right;
  }

  public void setRight(LambdaTerm right) {
    this.right = right;
    updateHash();
  }

  public String toString() {
    return "(" + left.toString() + ")(" + right.toString() + ")";
  }

  public String toString(Definitions definitions) {
    Definition defined = isDefined(definitions);
    if(defined != null) return defined.getName();
    return "(" + left.toString(definitions) + ")(" + right.toString(definitions) + ")";
  }

  /**
   * LambdaTerm.
   */
  
  public LambdaTerm copy() {
    Application application = new Application(left.copy(), right.copy());
    copyTo(application);
    return application;
  }

  public boolean isRedex() {
    return left instanceof Lambda;
  }

  public boolean hasRedexDeep() {
    return isRedex() || left.hasRedexDeep() || right.hasRedexDeep();
  }

  public void getFreeVariables(HashSet<String> freeVariables) {
    left.getFreeVariables(freeVariables);
    right.getFreeVariables(freeVariables);
  }

  public LambdaTerm rewrite() {
    Lambda lambda = (Lambda) left;
    return lambda.getTerm().substitute(lambda.getName(), right);
  }

  protected LambdaTerm substitute(String variable, LambdaTerm term, HashSet<String> freeVars, HashMap<String,String> renaming) {
    left = left.substitute(variable, term, freeVars, renaming);
    right = right.substitute(variable, term, freeVars, renaming);
    updateHash();
    return this;
  }

  public LambdaTerm replace(String variable, LambdaTerm term) {
    left = left.replace(variable, term); 
    right = right.replace(variable, term);
    updateHash();
    return this;
  }

  // Visitors
  public <T,S> T visit(LambdaTermVisitor<T,S> visitor, S s) {
    return visitor.visit(this, s);
  }

  public void visit(LambdaTermVisitorVoid visitor) {
    visitor.visit(this);
  }

  // Hash
  public void updateHash() {
    hash = 7*left.hash + 21*right.hash + 31;
  }

  protected boolean equals(ArrayList<String> binders, LambdaTerm term, ArrayList<String> termBinders) {
    if(hash != term.hash) return false;
    
    if(!(term instanceof Application)) return false;
    Application application = (Application) term;

    return left.equals(binders, application.left, termBinders) && right.equals(binders, application.right, termBinders);
  }
}
