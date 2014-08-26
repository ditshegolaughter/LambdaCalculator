/**
 * 
 */
package jfun.parsec;

final class GetIndexParser extends Parser<Integer> {
  GetIndexParser(String n) {
    super(n);
  }

  boolean apply(final ParseContext ctxt) {
    ctxt.setReturn(new Integer(ctxt.getIndex()));
    return true;
  }
}