/**
 * 
 */
package jfun.parsec;

final class LookaheadParser<R> extends Parser<R> {
  private final Parser<R> p;

  private final int toknum;

  LookaheadParser(String n, Parser<R> p, int toknum) {
    super(n);
    this.p = p;
    this.toknum = toknum;
  }

  boolean apply(final ParseContext ctxt) {
    return p.parse(ctxt, toknum);
  }

  boolean apply(final ParseContext ctxt, final int la) {
    return p.parse(ctxt, la);
  }
}