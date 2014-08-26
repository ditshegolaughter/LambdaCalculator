/**
 * 
 */
package jfun.parsec;

final class Map3Parser<R, A, B, C> extends Parser<R> {
  private final Map3<? super A, ? super B, ? super C, R> m3;

  private final Parser<B> p2;

  private final Parser<A> p1;

  private final Parser<C> p3;

  Map3Parser(String n, Map3<? super A, ? super B, ? super C, R> m3, Parser<B> p2, Parser<A> p1, Parser<C> p3) {
    super(n);
    this.m3 = m3;
    this.p2 = p2;
    this.p1 = p1;
    this.p3 = p3;
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
    final boolean r3 = p3.parse(ctxt);
    if (!r3)
      return false;
    final C o3 = p3.getReturn(ctxt);
    ctxt.setReturn(m3.map(o1, o2, o3));
    return true;
  }
}