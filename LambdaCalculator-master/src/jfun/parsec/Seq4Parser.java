/**
 * 
 */
package jfun.parsec;

final class Seq4Parser<R> extends Parser<R> {
  private final Parser<R> p4;

  private final Parser<?> p1;

  private final Parser<?> p2;

  private final Parser<?> p3;

  Seq4Parser(String n, Parser<R> p4, Parser<?> p1, Parser<?> p2, Parser<?> p3) {
    super(n);
    this.p4 = p4;
    this.p1 = p1;
    this.p2 = p2;
    this.p3 = p3;
  }

  boolean apply(final ParseContext ctxt) {
    if (!p1.parse(ctxt))
      return false;
    if (!p2.parse(ctxt))
      return false;
    if (!p3.parse(ctxt))
      return false;
    return p4.parse(ctxt);
  }
}