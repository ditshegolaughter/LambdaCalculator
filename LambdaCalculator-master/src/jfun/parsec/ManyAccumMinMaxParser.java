/**
 * 
 */
package jfun.parsec;

final class ManyAccumMinMaxParser<R, From, To, A extends From> extends Parser<R> {
  private final int max;

  private final Parser<A> p;

  private final int min;

  private final Accumulatable<From, To> accm;

  ManyAccumMinMaxParser(String n, int max, Parser<A> p, int min, Accumulatable<From, To> accm) {
    super(n);
    this.max = max;
    this.p = p;
    this.min = min;
    this.accm = accm;
  }

  boolean apply(final ParseContext ctxt) {
    final Accumulator<From, To> acc = accm.getAccumulator();
    if (!ParserInternals.accm_repeat(acc, min, p, ctxt))
      return false;
    if (ParserInternals.accm_some(acc, max - min, p, ctxt))
      return ParserInternals.returnValue(acc.getResult(), ctxt);
    else
      return false;
  }
}