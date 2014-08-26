package lambda.parser;

import lambda.LambdaTerm;

/**
 * Definition name = term.
 */
public class Definition {
  private String name;
  private LambdaTerm term;

  public Definition(String name, LambdaTerm term) {
    this.name = name;
    this.term = term;
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
  }
}
