package lambda;

import lambda.parser.Definition;
import lambda.parser.Definitions;
import lambda.utils.LambdaTermVisitorVoid;
import lambda.utils.LambdaTermVisitor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;

/**
 * Variable.
 */
public class Variable extends LambdaTerm {
  private String name;

  public Variable(String name) {
    this.name = name;
    updateHash();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String toString() {
    return name;
  }

  public String toString(Definitions definitions) {
    return name;
  }

  /**
   * LambdaTerm.
   */

  public LambdaTerm copy() {
    Variable variable = new Variable(name);
    copyTo(variable);
    return variable;
  }

  public boolean isRedex() {
    return false;
  }

  public boolean hasRedexDeep() {
    return false;
  }

  public void getFreeVariables(HashSet<String> freeVariables) {
    freeVariables.add(name);
  }

  protected LambdaTerm substitute(String variable, LambdaTerm term, HashSet<String> freeVars, HashMap<String,String> renaming) {
    if(name.equals(variable) && term != null) return term.copy();

    String renamedName = renaming.get(name);
    return new Variable(renamedName != null ? renamedName : name);
  }

  public LambdaTerm replace(String variable, LambdaTerm term) {
    if(name.equals(variable)) return term.copy(); else return this;
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
    hash = 3;
  }

  protected boolean equals(ArrayList<String> binders, LambdaTerm term, ArrayList<String> termBinders) {
    if(!(term instanceof Variable)) return false;
    Variable variable = (Variable) term;

    return binders.lastIndexOf(name) == termBinders.lastIndexOf(variable.name);
  }
}
