/**
 * 
 */
package jfun.parsec;

final class IsTokenParser<R> extends Parser<R> {
  private final FromToken<R> ft;

  IsTokenParser(String n, FromToken<R> ft) {
    super(n);
    this.ft = ft;
  }

  boolean apply(final ParseContext ctxt) {
    if (ctxt.isEof()) {
      ctxt.setError(ctxt.getSysUnexpected());
      return false;
    }
    final Tok ptok = ctxt.getToken();
    final Object v = ft.fromToken(ptok);
    if (v == null) {
      ctxt.setError(ctxt.getSysUnexpected());
      return false;
    }
    ctxt.setReturn(v);
    ctxt.next();
    return true;
  }
}