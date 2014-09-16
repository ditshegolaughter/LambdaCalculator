package lambda.parser;

import jfun.parsec.*;
import lambda.LambdaTerm;
import lambda.Variable;
import lambda.Lambda;
import lambda.Application;

/**
 * Lambda term parser.
 */
public final class LambdaTermParser {
  private static final String[] operators = new String[] {"\\", ".", "(", ")", "=", ";"};
  private static final Terms words = Terms.getOperatorsInstance(operators);

  public static Definitions parse(String string) {
    Definition[] definitions = (Definition[])
            Parsers.runParser(string,
                    Parsers.parseTokens(getLexer(), getParser(), "LambdaTermParser"),
                    new DefaultPositionMap(string, 1, 1), "LambdaTermParser");

    return new Definitions(definitions);
  }

  private static Parser getLexer() {
    return Lexers.lexeme(Scanners.javaDelimiter(), Parsers.alt(words.getLexer(), Lexers.word())).followedBy(Parsers.eof());
  }

  private static Parser<Definition[]> getParser() {
    return definition().many1(Definition.class).followedBy(Parsers.eof());
  }

  private static Parser<Definition> definition() {
    return Parsers.token(stringMap).followedBy(words.getParser("=")).and(term().followedBy(words.getParser(";")),definitionMap);
  }

  private static final Map2<String,LambdaTerm,Definition> definitionMap = new Map2<String,LambdaTerm,Definition>() {
    public Definition map(String variableName, LambdaTerm term) {
      return new Definition(variableName, term);
    }
  };

  // Lambda term parser
  private static final Parser<LambdaTerm> lazyTerm = Parsers.lazy("lazyTerm", new ParserEval<LambdaTerm>() {
    public Parser<LambdaTerm> eval() { return term(); }
  });

  private static Parser<LambdaTerm> term() {
    return Parsers.alt(application(), termNoApplication());
  }

  private static Parser<LambdaTerm> termNoApplication() {
    return Parsers.alt(
            Parsers.between(
                    words.getParser("("),
                    words.getParser(")"),
                    lazyTerm
            ),
            lambda(),
            variable()
    );
  }

  private static Parser<LambdaTerm> variable() {
    return Parsers.token(variableMap);
  }

  private static Parser<LambdaTerm> lambda() {
    return words.getParser("\\").seq(Parsers.token(stringMap)).followedBy(words.getParser(".")).and(lazyTerm, lambdaMap);
  }

  private static Parser<LambdaTerm> application() {
    return termNoApplication().many1(LambdaTerm.class).map(applicationMap);
  }

  private static FromToken<LambdaTerm> variableMap = new FromToken<LambdaTerm>() {
    public LambdaTerm fromToken(Tok tok) {
      String string = tok.toString();
      for(String operator : operators) if(string.equals(operator)) return null;
      return new Variable(tok.toString());
    }
  };

  private static final Map2<String,LambdaTerm,LambdaTerm> lambdaMap = new Map2<String,LambdaTerm,LambdaTerm>() {
    public Lambda map(String variableName, LambdaTerm term) {
      return new Lambda(variableName, term);
    }
  };

  private static final Map<LambdaTerm[],LambdaTerm> applicationMap = new Map<LambdaTerm[],LambdaTerm>() {
    public LambdaTerm map(LambdaTerm[] arguments) {
      if(arguments.length == 1) return arguments[0];

      LambdaTerm[] rest = new LambdaTerm[arguments.length-1];
      System.arraycopy(arguments, 0, rest, 0, arguments.length-1);
      
      return new Application(map(rest), arguments[arguments.length-1]);
    }
  };

  private static FromToken<String> stringMap = new FromToken<String>() {
    public String fromToken(Tok tok) { return tok.toString(); }
  };

  public static void main(String[] args) {
    System.out.println(parse("x"));
    System.out.println(parse("\\ x . x"));
    System.out.println(parse("x y"));
    System.out.println(parse("\\z.x z"));
    System.out.println(parse("\\x.(y)y(\\z.x z)"));
  }
}
