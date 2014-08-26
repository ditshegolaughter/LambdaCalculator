/**
 * 
 */
package jfun.parsec;

final class StepParser<R> extends Parser<R> {
  private final Parser<R> p;

  private final int n;

  StepParser(String name, Parser<R> p, int n) {
    super(name);
    this.p = p;
    this.n = n;
  }

  boolean apply(final ParseContext ctxt) {
    final int step = ctxt.getStep();
    if (!p.parse(ctxt))
      return false;
    ctxt.setStep(step + n);
    return true;
  }
}