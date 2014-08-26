/**
 * 
 */
package jfun.parsec;

final class PeekParser<R> extends Parser<R> {
  private final Parser<R> p;

  PeekParser(String n, Parser<R> p) {
    super(n);
    this.p = p;
  }

  boolean apply(final ParseContext ctxt) {
    final int step = ctxt.getStep();
    final int at = ctxt.getAt();
    final boolean r = p.parse(ctxt);
    ctxt.setAt(step, at);
    return r;
  }
}