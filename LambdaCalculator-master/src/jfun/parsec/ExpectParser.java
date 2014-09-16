/**
 * 
 */
package jfun.parsec;

final class ExpectParser<x> extends Parser<x> {
  private final String lbl;

  ExpectParser(String n, String lbl) {
    super(n);
    this.lbl = lbl;
  }

  boolean apply(final ParseContext ctxt) {
    return ParserInternals.setErrorExpecting(lbl, ctxt);
  }
}