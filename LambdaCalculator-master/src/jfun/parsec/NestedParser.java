/**
 * 
 */
package jfun.parsec;

final class NestedParser<R> extends Parser<R> {
  private final ShowToken show;

  private final String module;

  private final Parser<Tok[]> lexer;

  private final Parser<R> p;

  private final String eof_title;

  NestedParser(String n, ShowToken show, String module, Parser<Tok[]> lexer, Parser<R> p, String eof_title) {
    super(n);
    this.show = show;
    this.module = module;
    this.lexer = lexer;
    this.p = p;
    this.eof_title = eof_title;
  }

  boolean apply(final ParseContext ctxt) {
    if (!lexer.parse(ctxt))
      return false;
    // final R obj = p.getReturn(ctxt);
    final Tok[] toks = lexer.getReturn(ctxt);// toTokens(obj);
    final ParserState s0 = new ParserState(toks, ctxt.getUserState(), toks,
        0, module, ctxt.getPositionMap(), ctxt.getIndex(), eof_title, show);
    return ParserInternals.cont(ctxt, s0, p);
  }
}