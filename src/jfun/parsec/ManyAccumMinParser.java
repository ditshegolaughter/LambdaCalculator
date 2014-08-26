/**
 * 
 */
package jfun.parsec;

final class ManyAccumMinParser<R, From, To, A extends From> extends Parser<R> {
  private final Accumulatable<From, To> accm;

  private final int min;

  private final Parser<A> p;

  ManyAccumMinParser(String n, Accumulatable<From, To> accm, int min, Parser<A> p) {
    super(n);
    this.accm = accm;
    this.min = min;
    this.p = p;
  }

  boolean apply(final ParseContext ctxt) {
    final Accumulator<From, To> acc = accm.getAccumulator();
    if (!ParserInternals.accm_repeat(acc, min, p, ctxt))
      return false;
    if (ParserInternals.accm_many(acc, p, ctxt))
      return ParserInternals.returnValue(acc.getResult(), ctxt);
    else
      return false;
  }
}