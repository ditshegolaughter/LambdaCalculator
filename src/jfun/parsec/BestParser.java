/**
 * 
 */
package jfun.parsec;

final class BestParser<R> extends Parser<R> {
  private final Parser<R>[] ps;

  private final IntOrder ord;

  BestParser(String n, Parser<R>[] ps, IntOrder ord) {
    super(n);
    this.ps = ps;
    this.ord = ord;
  }

  boolean apply(final ParseContext ctxt) {
    final Object ustate = ctxt.getUserState();
    final Object ret = ctxt.getReturn();
    final int step = ctxt.getStep();
    final int at = ctxt.getAt();
    final AbstractParsecError error = ctxt.getError();
    AbstractParsecError err = error;
    for (int i = 0; i < ps.length; i++) {
      final Parser p1 = ps[i];
      if (p1.parse(ctxt)) {
        return ParserInternals._most(ord, ps, i + 1, ctxt, ustate, ret, step, at, error);
      }
      if (ctxt.hasException())
        return false;
      // in alternate, we do not care partial match.
      err = AbstractParsecError.mergeError(err, ctxt.getError());
      ctxt.set(step, at, ret, ustate, error);
    }
    ctxt.setError(err);
    return false;
  }
}