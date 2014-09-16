package jfun.parsec;

import jfun.parsec.trace.Trace;

final class TracedParser extends Parser {
  private final Parser fwd;
  private final Trace trace;
  
  TracedParser(String n, Parser fwd, Trace trace) {
    super(n);
    this.fwd = fwd;
    this.trace = trace;
  }
  boolean apply(ParseContext ctxt) {
    int original_step = ctxt.getStep();
    int original_at = ctxt.getAt();
    boolean ok = fwd.apply(ctxt);
    final CharSequence src = ctxt.getSource();
    final int ind = ctxt.getIndex();
    final int steps = ctxt.getStep()-original_step;
    final int offset = ctxt.getAt()-original_at;
    if(ok){
      trace.onSuccess(ctxt.getReturn(), src, ind, steps, offset);
    }
    else{
      trace.onError(getException(ctxt.getError()), src, ind, steps, offset);
    }
    return ok;
  }

  private static Object getException(AbstractParsecError err){
    return err==null?null:err.getException();
  }
}
