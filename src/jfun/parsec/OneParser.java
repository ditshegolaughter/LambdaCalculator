/**
 * 
 */
package jfun.parsec;

final class OneParser extends Parser<Object> {
  OneParser(String n) {
    super(n);
  }

  boolean apply(final ParseContext ctxt) {
    return true;
  }

  boolean apply(final ParseContext ctxt, final int la) {
    return true;
  }
}