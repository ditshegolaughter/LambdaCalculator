/**
 * 
 */
package jfun.parsec;

final class Map5Parser<R, A, B, C, D, E> extends Parser<R> {
  private final Parser<A> p1;

  private final Parser<C> p3;

  private final Parser<D> p4;

  private final Parser<B> p2;

  private final Map5<? super A, ? super B, ? super C, ? super D, ? super E, R> m5;

  private final Parser<E> p5;

  Map5Parser(String n, Parser<A> p1, Parser<C> p3, Parser<D> p4, Parser<B> p2, Map5<? super A, ? super B, ? super C, ? super D, ? super E, R> m5, Parser<E> p5) {
    super(n);
    this.p1 = p1;
    this.p3 = p3;
    this.p4 = p4;
    this.p2 = p2;
    this.m5 = m5;
    this.p5 = p5;
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
    final boolean r5 = p5.parse(ctxt);
    if (!r5)
      return false;
    final E o5 = p5.getReturn(ctxt);
    ctxt.setReturn(m5.map(o1, o2, o3, o4, o5));
    return true;
  }
}