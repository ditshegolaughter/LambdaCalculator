/**
 * 
 */
package jfun.parsec;

final class SumParser extends Parser<Object> {
  private final Parser<?>[] ps;

  SumParser(String n, Parser<?>[] ps) {
    super(n);
    this.ps = ps;
  }

  boolean apply(final ParseContext ctxt) {
    return apply(ctxt, 1);
  }

  boolean apply(final ParseContext ctxt, final int look_ahead) {
    final Object ustate = ctxt.getUserState();
    final Object ret = ctxt.getReturn();
    final int at = ctxt.getAt();
    final int step = ctxt.getStep();
    final AbstractParsecError error = ctxt.getError();
    AbstractParsecError err = error;
    for (int i = 0; i < ps.length; i++) {
      final Parser p1 = ps[i];
      if (p1.parse(ctxt))
        return true;
      if (ctxt.hasException())
        return false;
      if (ctxt.getAt() != at && ctxt.getStep() - step >= look_ahead)
        return false;
      // if(state.getAt() - at >= look_ahead) return false;
      err = AbstractParsecError.mergeError(err, ctxt.getError());
      ctxt.set(step, at, ret, ustate, error);
    }
    ctxt.setError(err);
    return false;
  }
}