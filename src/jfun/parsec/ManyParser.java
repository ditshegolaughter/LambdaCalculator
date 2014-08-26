/**
 * 
 */
package jfun.parsec;

final class ManyParser extends Parser<_> {
  private final Parser<?> p;

  ManyParser(String n, Parser<?> p) {
    super(n);
    this.p = p;
  }

  boolean apply(final ParseContext ctxt) {
    if (ParserInternals.run_many(p, ctxt)) {
      return ParserInternals.returnValue(null, ctxt);
    } else
      return false;
  }
}