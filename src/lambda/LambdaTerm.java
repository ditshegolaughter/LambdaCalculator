package lambda;

import lambda.parser.Definition;
import lambda.parser.Definitions;
import lambda.utils.Bound;
import lambda.utils.LambdaTermVisitorVoid;
import lambda.utils.LambdaTermVisitor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;

public abstract class LambdaTerm {

  public abstract LambdaTerm copy();

  public void copyTo(LambdaTerm term) {
    term.setExpanded(isExpanded);
  }

  public abstract boolean isRedex();
  public abstract boolean hasRedexDeep();

  public abstract void getFreeVariables(HashSet<String> freeVariables);

  public LambdaTerm substitute(String variable, LambdaTerm term) {
    HashSet<String> freeVariables = new HashSet<String>();
    term.getFreeVariables(freeVariables);

    return substitute(variable, term, freeVariables, new HashMap<String, String>());
  }

  protected abstract LambdaTerm substitute(String variable, LambdaTerm term, HashSet<String> freeVars, HashMap<String,String> renaming);

  public abstract LambdaTerm replace(String variable, LambdaTerm term);

  /**
   * Visitors.
   */
  public abstract <T,S> T visit(LambdaTermVisitor<T,S> visitor, S s);
  public abstract void visit(LambdaTermVisitorVoid visitor);

  /**
   * Hash and equality.
   */
  protected int hash = 0;

  public abstract void updateHash();

  public int hashCode() {
    return hash;
  }

  public boolean equals(Object obj) {
    if(!(obj instanceof LambdaTerm)) return false;

    LambdaTerm lambdaTerm = (LambdaTerm) obj;
    return lambdaTerm.hash == hash && equals(new ArrayList<String>(), lambdaTerm, new ArrayList<String>());
  }

  protected abstract boolean equals(ArrayList<String> binders, LambdaTerm term, ArrayList<String> termBinders);

  /**
   * GUI.
   */
  private Bound bound = new Bound(0,0,0,0);
  private Bound innerBound = new Bound(0,0,0,0);
  private boolean isExpanded = true;

  public Bound getBound() {
    return bound;
  }

  public void setBound(Bound bound) {
    this.bound = bound;
  }

  public Bound getInnerBound() {
    return innerBound;
  }

  public void setInnerBound(Bound innerBound) {
    this.innerBound = innerBound;
  }

  public boolean isExpanded() {
    return isExpanded;
  }

  public void setExpanded(boolean expanded) {
    isExpanded = expanded;
  }

  /**
   * Checks whether the lambda term is a defined term.

   * @param definitions list of definitions
   * @return the corresponding definition or null
   */
  public Definition isDefined(Definitions definitions) {
    for(Definition definition : definitions.getDefinitions()) {
      if(definition.getName().equals("Init")) continue;
      if(equals(definition.getTerm())) return definition;
    }
    return null;
  }

  public abstract String toString(Definitions definitions);
}
