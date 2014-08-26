/**
 * 
 */
package jfun.parsec;

class ArrayAccumulator<E, T extends E> implements
    Accumulator<T, E[]> {
  public void accumulate(final T o) {
    l.add(o);
  }

  public E[] getResult() {
    return ParserInternals.getArrayResult(l, af);
  }

  private final java.util.ArrayList<T> l;

  private final ArrayFactory<E> af;

  ArrayAccumulator(final ArrayFactory<E> af, final java.util.ArrayList<T> al) {
    this.af = af;
    this.l = al;
  }
}