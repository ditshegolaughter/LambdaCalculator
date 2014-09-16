/**
 * 
 */
package jfun.parsec;

final class FollowedByParser<R> extends Parser<R> {
  private final Parser<?> sep;

  private final Parser<R> p;

  FollowedByParser(String n, Parser<?> sep, Parser<R> p) {
    super(n);
    this.sep = sep;
    this.p = p;
  }

  boolean apply(final ParseContext ctxt) {
    final boolean r1 = p.parse(ctxt);
    if (!r1)
      return r1;
    final R ret = p.getReturn(ctxt);
    if (!sep.parse(ctxt))
      return false;
    ctxt.setReturn(ret);
    return true;
  }
}