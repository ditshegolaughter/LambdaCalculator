/**
 * 
 */
package jfun.parsec;

final class MapnParser<R> extends Parser<R> {
  private final Mapn<R> mn;

  private final ArrayFactory<?> af;

  private final Parser<?>[] ps;

  MapnParser(String n, Mapn<R> mn, ArrayFactory<?> af, Parser<?>[] ps) {
    super(n);
    this.mn = mn;
    this.af = af;
    this.ps = ps;
  }

  boolean apply(final ParseContext ctxt) {
    final Object[] ret = af.createArray(ps.length);
    for (int i = 0; i < ps.length; i++) {
      final Parser<?> p = ps[i];
      if (!p.parse(ctxt))
        return false;
      ret[i] = p.getReturn(ctxt);
    }
    ctxt.setReturn(mn.map(ret));
    return true;
  }
}