/**
 * 
 */
package jfun.parsec;

final class IfElseParser<R, C> extends Parser<R> {
  private final Parser<? extends R> no;

  private final Parser<C> p;

  private final ToParser<? super C, R> yes;

  IfElseParser(String n, Parser<? extends R> no, Parser<C> p, ToParser<? super C, R> yes) {
    super(n);
    this.no = no;
    this.p = p;
    this.yes = yes;
  }

  boolean apply(final ParseContext ctxt, final int look_ahead) {
    final Object ustate = ctxt.getUserState();
    final Object ret = ctxt.getReturn();
    final int step = ctxt.getStep();
    final int at = ctxt.getAt();
    final AbstractParsecError error = ctxt.getError();
    if (p.parse(ctxt))
      return yes.toParser(p.getReturn(ctxt)).parse(ctxt);
    return ParserInternals.recover(look_ahead, no, ctxt, step, at, ret, ustate, error);
  }

  boolean apply(final ParseContext ctxt) {
    return apply(ctxt, 1);
  }
}