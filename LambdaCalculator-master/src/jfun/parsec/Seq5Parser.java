/**
 * 
 */
package jfun.parsec;

final class Seq5Parser<R> extends Parser<R> {
  private final Parser<?> p1;

  private final Parser<?> p3;

  private final Parser<R> p5;

  private final Parser<?> p4;

  private final Parser<?> p2;

  Seq5Parser(String n, Parser<?> p1, Parser<?> p3, Parser<R> p5, Parser<?> p4, Parser<?> p2) {
    super(n);
    this.p1 = p1;
    this.p3 = p3;
    this.p5 = p5;
    this.p4 = p4;
    this.p2 = p2;
  }

  boolean apply(final ParseContext ctxt) {
    if (!p1.parse(ctxt))
      return false;
    if (!p2.parse(ctxt))
      return false;
    if (!p3.parse(ctxt))
      return false;
    if (!p4.parse(ctxt))
      return false;
    return p5.parse(ctxt);
  }
}