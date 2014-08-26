package jfun.parsec;

import jfun.util.Misc;

/**
 * This is a simple Java Bean for 4 objects.
 * <p>
 * @author Ben Yu
 * Apr 24, 2006 1:44:23 PM
 * @param <A>
 * @param <B>
 * @param <C>
 * @param <D>
 * @since version 1.1
 */
public class Tuple4<A, B, C, D> extends Tuple3<A,B,C> {
  private D value4;
  public Tuple4(){}
  
  public Tuple4(A value1, B value2, C value3, D value4) {
    super(value1,value2,value3);
    this.value4 = value4;
  }
  public D getValue4() {
    return value4;
  }
  public void setValue4(D value4) {
    this.value4 = value4;
  }
  public boolean equals(Tuple4 other){
    return super.equals(other) && Misc.equals(value4, other.value4);
  }
  @Override
  public boolean equals(Object obj) {
    if(obj instanceof Tuple4){
      return equals((Tuple4)obj);
    }
    else return false;
  }
  @Override
  public int hashCode() {
    return super.hashCode()*31+Misc.hashcode(value4);
  }
  @Override
  public String toString() {
    return "("+getValue1()+","+getValue2()+","+getValue3()+","+getValue4()+")";
  }
}
