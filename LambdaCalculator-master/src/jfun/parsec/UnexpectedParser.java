/**
 * 
 */
package jfun.parsec;

final class UnexpectedParser<x> extends Parser<x> {
  private final String msg;

  UnexpectedParser(String n, String msg) {
    super(n);
    this.msg = msg;
  }

  boolean apply(final ParseContext ctxt) {
    return ParserInternals.raiseUnexpected(msg, ctxt);
  }
}