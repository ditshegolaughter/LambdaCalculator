/**
 * 
 */
package jfun.parsec;

final class AtomicParser<R> extends Parser<R> {
  private final Parser<R> p;

  AtomicParser(String n, Parser<R> p) {
    super(n);
    this.p = p;
  }

  boolean apply(final ParseContext ctxt) {
    final int at = ctxt.getAt();
    final int step = ctxt.getStep();
    final boolean r = p.parse(ctxt);
    if (!r)
      ctxt.setAt(step, at);
    else
      ctxt.setStep(step + 1);
    return r;
  }
}