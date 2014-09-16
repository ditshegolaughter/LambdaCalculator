/**
 * 
 */
package jfun.parsec;

final class SomeAccumMaxParser<R, From, To, A extends From> extends Parser<R> {
  private final int max;

  private final Accumulatable<From, To> accm;

  private final Parser<A> p;

  SomeAccumMaxParser(String n, int max, Accumulatable<From, To> accm, Parser<A> p) {
    super(n);
    this.max = max;
    this.accm = accm;
    this.p = p;
  }

  boolean apply(final ParseContext ctxt) {
    final Accumulator<From, To> acc = accm.getAccumulator();
    if (ParserInternals.accm_some(acc, max, p, ctxt))
      return ParserInternals.returnValue(acc.getResult(), ctxt);
    else
      return false;
  }
}