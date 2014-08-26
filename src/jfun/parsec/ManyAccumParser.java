/**
 * 
 */
package jfun.parsec;

final class ManyAccumParser<R, From, To, A extends From> extends Parser<R> {
  private final Accumulatable<From, To> accm;

  private final Parser<A> p;

  ManyAccumParser(String n, Accumulatable<From, To> accm, Parser<A> p) {
    super(n);
    this.accm = accm;
    this.p = p;
  }

  boolean apply(final ParseContext ctxt) {
    final Accumulator<From, To> acc = accm.getAccumulator();
    if (ParserInternals.accm_many(acc, p, ctxt))
      return ParserInternals.returnValue(acc.getResult(), ctxt);
    else
      return false;
  }
}