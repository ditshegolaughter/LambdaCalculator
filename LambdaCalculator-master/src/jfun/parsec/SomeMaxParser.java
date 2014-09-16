/**
 * 
 */
package jfun.parsec;

final class SomeMaxParser extends Parser<_> {
  private final int max;

  private final Parser<?> p;

  SomeMaxParser(String n, int max, Parser<?> p) {
    super(n);
    this.max = max;
    this.p = p;
  }

  boolean apply(final ParseContext ctxt) {
    if (ParserInternals.run_some(max, p, ctxt))
      return ParserInternals.returnValue(null, ctxt);
    else
      return false;
  }
}