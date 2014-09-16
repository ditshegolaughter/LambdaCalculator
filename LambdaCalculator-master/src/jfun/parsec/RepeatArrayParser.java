/**
 * 
 */
package jfun.parsec;

final class RepeatArrayParser<R> extends Parser<R[]> {
  private final ArrayFactory<R> af;

  private final int n;

  private final Parser<? extends R> p;

  RepeatArrayParser(String name, ArrayFactory<R> af, int n, Parser<? extends R> p) {
    super(name);
    this.af = af;
    this.n = n;
    this.p = p;
  }

  boolean apply(final ParseContext ctxt) {
    final R[] ret = af.createArray(n);
    for (int i = 0; i < n; i++) {
      if (!p.parse(ctxt))
        return false;
      ret[i] = p.getReturn(ctxt);
    }
    ctxt.setReturn(ret);
    return true;
  }
}