package jfun.parsec;

import jfun.util.Misc;

/**
 * This is a simple Java Bean for 5 objects.
 * <p>
 * @author Ben Yu
 * Apr 24, 2006 1:45:42 PM
 * @param <A>
 * @param <B>
 * @param <C>
 * @param <D>
 * @param <E>
 * @since version 1.1
 */
public class Tuple5<A, B, C, D, E> extends Tuple4<A,B,C,D>{
  private E value5;
  public Tuple5(A value1, B value2, C value3, D value4, E value5) {
    super(value1, value2, value3, value4);
    this.value5 = value5;
  }
  public Tuple5() {
  }
  public E getValue5() {
    return value5;
  }
  public void setValue5(E value5) {
    this.value5 = value5;
  }
  public boolean equals(Tuple5 other){
    return super.equals(other) && Misc.equals(value5, other.value5);
  }
  @Override
  public boolean equals(Object obj) {
    if(obj instanceof Tuple5){
      return equals((Tuple5)obj);
    }
    else return false;
  }
  @Override
  public int hashCode() {
    return super.hashCode()*31+Misc.hashcode(value5);
  }
  @Override
  public String toString() {
    return "("+getValue1()+","+getValue2()
    +","+getValue3()+","+getValue4()+","+getValue5()+")";
  }
}
