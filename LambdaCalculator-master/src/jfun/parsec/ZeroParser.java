/**
 * 
 */
package jfun.parsec;

final class ZeroParser<T> extends Parser<T> {
  ZeroParser(String n) {
    super(n);
  }

  boolean apply(final ParseContext ctxt) {
    return false;
  }

  boolean apply(final ParseContext ctxt, final int la) {
    return false;
  }
}