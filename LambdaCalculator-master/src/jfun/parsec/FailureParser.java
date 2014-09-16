/**
 * 
 */
package jfun.parsec;

final class FailureParser<R> extends Parser<R> {
  private final String msg;

  FailureParser(String n, String msg) {
    super(n);
    this.msg = msg;
  }

  boolean apply(final ParseContext ctxt) {
    return ParserInternals.raiseRaw(msg, ctxt);
  }
}