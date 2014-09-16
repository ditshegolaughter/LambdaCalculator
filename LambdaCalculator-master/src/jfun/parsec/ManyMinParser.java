/**
 * 
 */
package jfun.parsec;

final class ManyMinParser extends Parser<_> {
  private final Parser<?> p;

  private final int min;

  ManyMinParser(String n, Parser<?> p, int min) {
    super(n);
    this.p = p;
    this.min = min;
  }

  boolean apply(final ParseContext ctxt) {
    if (!ParserInternals.run_repeat(min, p, ctxt))
      return false;
    if (ParserInternals.run_many(p, ctxt)) {
      return ParserInternals.returnValue(null, ctxt);
    } else
      return false;
  }
}