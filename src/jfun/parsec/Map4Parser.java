/**
 * 
 */
package jfun.parsec;

final class Map4Parser<R, A, B, C, D> extends Parser<R> {
  private final Map4<? super A, ? super B, ? super C, ? super D, R> m4;

  private final Parser<C> p3;

  private final Parser<B> p2;

  private final Parser<A> p1;

  private final Parser<D> p4;

  Map4Parser(String n, Map4<? super A, ? super B, ? super C, ? super D, R> m4, Parser<C> p3, Parser<B> p2, Parser<A> p1, Parser<D> p4) {
    super(n);
    this.m4 = m4;
    this.p3 = p3;
    this.p2 = p2;
    this.p1 = p1;
    this.p4 = p4;
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
    final boolean r4 = p4.parse(ctxt);
    if (!r4)
      return false;
    final D o4 = p4.getReturn(ctxt);
    ctxt.setReturn(m4.map(o1, o2, o3, o4));
    return true;
  }
}