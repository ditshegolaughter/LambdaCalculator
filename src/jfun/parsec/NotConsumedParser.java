/**
 * 
 */
package jfun.parsec;

final class NotConsumedParser<R> extends Parser<R> {
  private final Parser<R> p;

  private final String err;

  NotConsumedParser(String n, Parser<R> p, String err) {
    super(n);
    this.p = p;
    this.err = err;
  }

  boolean apply(final ParseContext ctxt) {
    final int step = ctxt.getStep();
    final int at = ctxt.getAt();
    if (!p.parse(ctxt))
      return false;
    if (ctxt.getAt() == at)
      return true;
    ctxt.setAt(step, at);
    return ParserInternals.raiseRaw(err, ctxt);
  }
}