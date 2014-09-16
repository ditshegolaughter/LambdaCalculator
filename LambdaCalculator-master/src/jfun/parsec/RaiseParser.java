/**
 * 
 */
package jfun.parsec;

final class RaiseParser<R> extends Parser<R> {
  private final Object e;

  RaiseParser(String n, Object e) {
    super(n);
    this.e = e;
  }

  boolean apply(final ParseContext ctxt) {
    ctxt.setError(ParserInternals.newException(e, ctxt));
    return false;
  }
}