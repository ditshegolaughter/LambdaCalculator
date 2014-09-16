/**
 * 
 */
package jfun.parsec;

final class EofParser extends Parser<Object> {
  private final String msg;

  EofParser(String n, String msg) {
    super(n);
    this.msg = msg;
  }

  boolean apply(final ParseContext ctxt) {
    if (ctxt.isEof())
      return true;
    else {
      final AbstractParsecError expecting = ParserInternals.errExpecting(msg, ctxt);
      final AbstractParsecError unexpected = ctxt.getSysUnexpected();
      ctxt.setError(AbstractParsecError.mergeError(expecting, unexpected));
      return false;
    }
  }
}