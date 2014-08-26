/**
 * 
 */
package jfun.parsec;

final class ReturnParser<R> extends Parser<R> {
  private final R r;

  ReturnParser(String n, R r) {
    super(n);
    this.r = r;
  }

  boolean apply(final ParseContext ctxt) {
    ctxt.setReturn(r);
    return true;
  }
}