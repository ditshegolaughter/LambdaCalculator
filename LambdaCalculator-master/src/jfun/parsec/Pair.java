package jfun.parsec;

import jfun.util.Misc;

/**
 * This is a simple Java Bean for a pair of objects.
 * <p>
 * @author Ben Yu
 * Apr 24, 2006 1:41:52 PM
 * @param <A>
 * @param <B>
 * @since version 1.1
 */
public class Pair<A, B> {
  private A value1;
  private B value2;
  public Pair(){}
  /**
   * To create a Pair object.
   * @param a the first object.
   * @param b the second object.
   */
  public Pair(A a, B b) {
    this.value1 = a;
    this.value2 = b;
  }
  /**
   * Get the first value.
   */
  public A getValue1() {
    return value1;
  }
  /**
   * Set the first value.
   */
  public void setValue1(A value1) {
    this.value1 = value1;
  }
  /**
   * Get the second value.
   */
  public B getValue2() {
    return value2;
  }
  /**
   * Set the second value.
   */
  public void setValue2(B value2) {
    this.value2 = value2;
  }
  public boolean equals(Pair other){
    return Misc.equals(value1, other.value1) && Misc.equals(value2, other.value2);
  }
  @Override
  public boolean equals(Object obj) {
    if(obj instanceof Pair){
      return equals((Pair)obj);
    }
    else return false;
  }
  @Override
  public int hashCode() {
    return Misc.hashcode(value1)*31+Misc.hashcode(value2);
  }
  @Override
  public String toString() {
    return "("+value1+","+value2+")";
  }
  
}
