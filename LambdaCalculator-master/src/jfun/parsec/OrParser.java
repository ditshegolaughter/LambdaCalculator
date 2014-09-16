package jfun.parsec;

final class OrParser extends Parser {
  private final Parser[] alternatives;

  OrParser(String n, Parser[] alternatives) {
    super(n);
    this.alternatives = alternatives;
  }

  boolean apply(ParseContext ctxt) {
    final Object ustate = ctxt.getUserState();
    final Object ret = ctxt.getReturn();
    final int at = ctxt.getAt();
    final int step = ctxt.getStep();
    final AbstractParsecError error = ctxt.getError();
    AbstractParsecError err = error;
    int err_at = -1;
    int err_pos = -1;
    int err_step = -1;
    for(int i=0; i<alternatives.length; i++){
      final Parser p1 = alternatives[i];
      if(p1.parse(ctxt)) return true;
      if(ctxt.hasException()) return false;
      if(ctxt.getError().getIndex() > err_pos){
        err_at = ctxt.getAt();
        err_pos = ctxt.getError().getIndex();
        err_step = ctxt.getStep();
        err = AbstractParsecError.mergeError(err, ctxt.getError());
      }
      ctxt.set(step, at, ret, ustate, error);
    }
    ctxt.setAt(err_step, err_at);
    ctxt.setError(err);
    return false;
  }
  
}
