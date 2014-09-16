/**
 * 
 */
package jfun.parsec;

final class RepeatParser extends Parser<_> {
  private final Parser<?> p;

  private final int n;

  RepeatParser(String name, Parser<?> p, int n) {
    super(name);
    this.p = p;
    this.n = n;
  }

  boolean apply(final ParseContext ctxt) {
    if (ParserInternals.run_repeat(n, p, ctxt)) {
      ctxt.setReturn(null);
      return true;
    } else
      return false;
  }
}