package lambda;

import lambda.parser.Definition;
import lambda.parser.Definitions;
import lambda.utils.LambdaTermVisitor;
import lambda.utils.LambdaTermVisitorVoid;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;

/**
 * Lambda abstraction.
 */
public class Lambda extends LambdaTerm {
  private String name;
  private LambdaTerm term;

  public Lambda(String variable, LambdaTerm term) {
    this.name = variable;
    this.term = term;
    updateHash();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LambdaTerm getTerm() {
    return term;
  }

  public void setTerm(LambdaTerm term) {
    this.term = term;
    updateHash();
  }

  public String toString() {
    return "\\" + name + "." + term.toString();
  }

  public String toString(Definitions definitions) {
    Definition defined = isDefined(definitions);
    if(defined != null) return defined.getName();
    return "\\" + name + "." + term.toString(definitions);
  }

  /**
   * LambdaTerm.
   */
  
  public LambdaTerm copy() {
    Lambda lambda = new Lambda(name, term.copy());
    copyTo(lambda);
    return lambda;
  }

  public boolean isRedex() {
    return false;
  }

  public boolean hasRedexDeep() {
    return term.hasRedexDeep();
  }

  public void getFreeVariables(HashSet<String> freeVariables) {
    boolean contains = freeVariables.contains(name);
    term.getFreeVariables(freeVariables);
    if(!contains) freeVariables.remove(name);
  }

  protected LambdaTerm substitute(String variable, LambdaTerm term, HashSet<String> freeVars, HashMap<String,String> renaming) {
    // we need alpha renaming ?
    boolean renamed = false;
    if(freeVars.contains(name) && !renaming.containsKey(name)) {
      String renamedName = rename(name, freeVars);
      renaming.put(name, renamedName);
      freeVars.add(renamedName);
      renamed = true;
    }

    // apply substitution to subterm
    this.term = this.term.substitute(variable, variable.equals(name) ? null : term, freeVars, renaming);
    updateHash();

    String renamedName = renaming.get(name);
    name = renamedName != null ? renamedName : name;

    if(renamed) {
      renaming.remove(this.name);
      freeVars.remove(renamedName);
    }

    return this;
  }

  public LambdaTerm replace(String variable, LambdaTerm term) {
    this.term = this.term.replace(variable, term);
    updateHash();
    return this;
  }

  public <T,S> T visit(LambdaTermVisitor<T,S> visitor, S s) {
    return visitor.visit(this, s);
  }

  public void visit(LambdaTermVisitorVoid visitor) {
    visitor.visit(this);
  }

  // Hash
  public void updateHash() {
    hash = 13*term.hash + 5;
  }

  protected boolean equals(ArrayList<String> binders, LambdaTerm term, ArrayList<String> termBinders) {
    if(hash != term.hash) return false;

    if(!(term instanceof Lambda)) return false;
    Lambda lambda = (Lambda) term;

    binders.add(name);
    termBinders.add(lambda.name);
    if(!this.term.equals(binders, lambda.getTerm(), termBinders)) return false;
    binders.remove(binders.size()-1);
    termBinders.remove(termBinders.size()-1);

    return true;
  }

  private static final char[] digits = {'0','1','2','3','4','5','6','7','8','9'};
  private String rename(String variable, HashSet<String> avoid) {
    // cut off number suffix
    int index = variable.length() - 1;
    while(index >= 0) {
      char c = variable.charAt(index);
      boolean isDigit = false;
      for(char digit : digits) isDigit = isDigit || (c == digit);
      if(!isDigit) break; else index--;
    }
    variable = variable.substring(0, index+1);

    // renaming
    int suffix = 0;
    while(avoid.contains(variable + suffix)) suffix++;

    return variable + suffix;
  }
}
