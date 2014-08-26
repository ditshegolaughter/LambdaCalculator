package jfun.parsec;

import jfun.util.Misc;

/**
 * This is a simple Java Bean for 3 objects.
 * <p>
 * @author Ben Yu
 * Apr 24, 2006 1:43:12 PM
 * @param <A>
 * @param <B>
 * @param <C>
 * @since version 1.1
 */
public class Tuple3<A, B, C> extends Pair<A,B> {
  private C value3;
  public Tuple3(){}
  public Tuple3(A value1, B value2, C value3) {
    super(value1, value2);
    this.value3 = value3;
  }
  public C getValue3() {
    return value3;
  }
  public void setValue3(C value3) {
    this.value3 = value3;
  }
  public boolean equals(Tuple3 other){
    return super.equals(other) && Misc.equals(value3, other.value3);
  }
  @Override
  public boolean equals(Object obj) {
    if(obj instanceof Tuple3){
      return equals((Tuple3)obj);
    }
    else return false;
  }
  @Override
  public int hashCode() {
    return super.hashCode()*31+Misc.hashcode(value3);
  }
  @Override
  public String toString() {
    return "("+getValue1()+","+getValue2()+","+getValue3()+")";
  }
}
