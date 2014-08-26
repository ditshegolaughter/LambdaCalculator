/**
 * 
 */
package jfun.parsec;

final class Seq3Parser<R> extends Parser<R> {
  private final Parser<R> p3;

  private final Parser<?> p1;

  private final Parser<?> p2;

  Seq3Parser(String n, Parser<R> p3, Parser<?> p1, Parser<?> p2) {
    super(n);
    this.p3 = p3;
    this.p1 = p1;
    this.p2 = p2;
  }

  boolean apply(final ParseContext ctxt) {
    if (!p1.parse(ctxt))
      return false;
    if (!p2.parse(ctxt))
      return false;
    return p3.parse(ctxt);
  }
}