/**
 * 
 */
package jfun.parsec;

final class AllBinders<T> implements ToParser<T, T> {
  private final String name;

  private final ToParser<T, T>[] binders;

  AllBinders(String name, ToParser<T, T>[] binders) {
    super();
    this.name = name;
    this.binders = binders;
  }

  public Parser<T> toParser(final T v) {
    return new Parser<T>(name) {
      boolean apply(final ParseContext ctxt) {
        T val = v;
        for (int i = 0; i < binders.length; i++) {
          final ToParser<T, T> pb = binders[i];
          final Parser<T> p = pb.toParser(val);
          if (!p.parse(ctxt))
            return false;
          val = p.getReturn(ctxt);
        }
        return true;
      }
    };
  }
}