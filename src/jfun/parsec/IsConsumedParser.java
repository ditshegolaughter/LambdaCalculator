/**
 * 
 */
package jfun.parsec;

final class IsConsumedParser<R> extends Parser<R> {
  private final Parser<R> p;

  private final String err;

  IsConsumedParser(String n, Parser<R> p, String err) {
    super(n);
    this.p = p;
    this.err = err;
  }

  boolean apply(final ParseContext ctxt) {
    final int at = ctxt.getAt();
    if (!p.parse(ctxt))
      return false;
    if (ctxt.getAt() != at)
      return true;
    return ParserInternals.raiseRaw(err, ctxt);
  }
}