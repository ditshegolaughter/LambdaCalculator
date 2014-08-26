/**
 * 
 */
package jfun.parsec;

final class IsReturnParser<R> extends Parser<R> {
  private final ObjectPredicate<R> op;

  IsReturnParser(String n, ObjectPredicate<R> op) {
    super(n);
    this.op = op;
  }

  boolean apply(final ParseContext ctxt) {
    final Object r = ctxt.getReturn();
    return op.isObject((R) r);
  }
}