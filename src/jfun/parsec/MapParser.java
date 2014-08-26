/**
 * 
 */
package jfun.parsec;

final class MapParser<R, From> extends Parser<R> {
  private final Parser<From> p;

  private final Map<? super From, R> m;

  MapParser(String n, Parser<From> p, Map<? super From, R> m) {
    super(n);
    this.p = p;
    this.m = m;
  }

  boolean apply(final ParseContext ctxt) {
    final boolean r = p.parse(ctxt);
    if (r) {
      ctxt.setReturn(m.map(p.getReturn(ctxt)));
    }
    return r;
  }
}