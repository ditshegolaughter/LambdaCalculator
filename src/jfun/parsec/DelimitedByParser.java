/**
 * 
 */
package jfun.parsec;

final class DelimitedByParser<R, E1, R1, E extends E1> extends Parser<R> {
  private final Parser<E> p;

  private final Accumulatable<E1, R1> accm;

  private final Parser<?> delim;

  DelimitedByParser(String n, Parser<E> p, Accumulatable<E1, R1> accm, Parser<?> delim) {
    super(n);
    this.p = p;
    this.accm = accm;
    this.delim = delim;
  }

  boolean apply(final ParseContext ctxt) {
    final Accumulator<E1, R1> acc = accm.getAccumulator();
    for (;;) {
      final int at0 = ctxt.getAt();
      boolean r = delim.parse(ctxt);
      final int at1 = ctxt.getAt();
      if (!r) {
        if (at0 != at1) {
          return false;
        }
        return ParserInternals.returnValue(acc.getResult(), ctxt);
      }
      r = p.parse(ctxt);
      final int at2 = ctxt.getAt();
      if (!r) {
        if (at1 != at2) {
          return false;
        }
        return ParserInternals.returnValue(acc.getResult(), ctxt);
      }
      if (ParserInternals.isInfiniteLoop(at0, at2)) {
        return true;
      }
      acc.accumulate(p.getReturn(ctxt));
    }
  }
}