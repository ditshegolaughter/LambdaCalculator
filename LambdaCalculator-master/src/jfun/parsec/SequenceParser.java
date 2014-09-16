/**
 * 
 */
package jfun.parsec;

final class SequenceParser extends Parser<Object> {
  private final Parser<?>[] ps;

  SequenceParser(String n, Parser<?>[] ps) {
    super(n);
    this.ps = ps;
  }

  boolean apply(ParseContext ctxt) {
    for (int i = 0; i < ps.length; i++) {
      final Parser p = ps[i];
      if (!p.parse(ctxt))
        return false;
    }
    return true;
  }
}