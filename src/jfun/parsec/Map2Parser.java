/**
 * 
 */
package jfun.parsec;

final class Map2Parser<R, A, B> extends Parser<R> {
  private final Map2<? super A, ? super B, R> m2;

  private final Parser<A> p1;

  private final Parser<B> p2;

  Map2Parser(String n, Map2<? super A, ? super B, R> m2, Parser<A> p1, Parser<B> p2) {
    super(n);
    this.m2 = m2;
    this.p1 = p1;
    this.p2 = p2;
  }

  boolean apply(final ParseContext ctxt) {
    final boolean r1 = p1.parse(ctxt);
    if (!r1)
      return false;
    final A o1 = p1.getReturn(ctxt);
    final boolean r2 = p2.parse(ctxt);
    if (!r2)
      return false;
    final B o2 = p2.getReturn(ctxt);
    ctxt.setReturn(m2.map(o1, o2));
    return true;
  }
}