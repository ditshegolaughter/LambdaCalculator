/**
 * 
 */
package jfun.parsec;

final class TryParser<R> extends Parser<R> {
  private final Parser<R> p;

  private final Catch<? extends R> hdl;

  TryParser(String n, Parser<R> p, Catch<? extends R> hdl) {
    super(n);
    this.p = p;
    this.hdl = hdl;
  }

  boolean apply(final ParseContext ctxt) {
    final AbstractParsecError err0 = ctxt.getError();
    final boolean r = p.parse(ctxt);
    if (!ctxt.hasException())
      return r;
    final Parser h = hdl.catchException(ctxt.getReturn(), ctxt.getError()
        .getException());
    ctxt.setError(err0);
    return h.parse(ctxt);
  }
}