/**
 * 
 */
package jfun.parsec;

final class SeqParser<R> extends Parser<R> {
  private final Parser<?> p1;

  private final Parser<R> p2;

  SeqParser(String n, Parser<?> p1, Parser<R> p2) {
    super(n);
    this.p1 = p1;
    this.p2 = p2;
  }

  boolean apply(final ParseContext ctxt) {
    if (!p1.parse(ctxt))
      return false;
    return p2.parse(ctxt);
  }
}