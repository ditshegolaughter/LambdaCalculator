package lambda.parser;

import lambda.LambdaTerm;

import java.util.HashMap;

/**
 * List of definitions.
 */
public class Definitions {
  private Definition[] definitions;
  private HashMap<String, LambdaTerm> map = new HashMap<String, LambdaTerm>();

  public Definitions(Definition[] definitions) {
    this.definitions = definitions;

    // unfold definitions
    for(Definition definition : definitions) {
      while(true) {
        LambdaTerm last = definition.getTerm().copy();
        for(Definition unfold : definitions) {
          definition.setTerm(definition.getTerm().replace(unfold.getName(), unfold.getTerm()));
        }
        if(definition.getTerm().equals(last)) break;
      }
    }

    for(Definition definition : definitions) {
      map.put(definition.getName(), definition.getTerm());
    }
  }

  public LambdaTerm get(String name) {
    return map.get(name);
  }

  public Definition[] getDefinitions() {
    return definitions;
  }
}
