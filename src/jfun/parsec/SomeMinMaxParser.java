/**
 * 
 */
package jfun.parsec;

final class SomeMinMaxParser extends Parser<_> {
  private final int max;

  private final Parser<?> p;

  private final int min;

  SomeMinMaxParser(String n, int max, Parser<?> p, int min) {
    super(n);
    this.max = max;
    this.p = p;
    this.min = min;
  }

  boolean apply(final ParseContext ctxt) {
    if (!ParserInternals.run_repeat(min, p, ctxt))
      return false;
    if (ParserInternals.run_some(max - min, p, ctxt))
      return ParserInternals.returnValue(null, ctxt);
    else
      return false;
  }
}