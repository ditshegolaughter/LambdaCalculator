package jfun.parsec;

/**
 * This class is the facade to create various tuple java beans.
 * <p>
 * @author Ben Yu
 * Apr 25, 2006 9:28:23 AM
 * @since version 1.1
 */
public class Tuples {
  /**
   * Create a Pair object.
   * @param a the first object.
   * @param b the 2nd object.
   * @return the Pair object.
   */
  public static <A,B> Pair<A,B> pair(A a, B b){
    return new Pair<A,B>(a,b);
  }
  /**
   * Create a 3-object tuple.
   * @param a the 1st object.
   * @param b the 2nd object.
   * @param c the 3rd object.
   * @return the tuple.
   */
  public static <A,B,C> Tuple3<A,B,C> tuple(A a, B b, C c){
    return new Tuple3<A,B,C>(a,b,c);
  }
  /**
   * Create a 4-object tuple.
   * @param a the 1st object.
   * @param b the 2nd object.
   * @param c the 3rd object.
   * @param d the 4th object.
   * @return the tuple.
   */
  public static <A,B,C,D> Tuple4<A,B,C,D> tuple(A a, B b, C c, D d){
    return new Tuple4<A,B,C,D>(a,b,c,d);
  }
  /**
   * Create a 5-object tuple.
   * @param a the 1st object.
   * @param b the 2nd object.
   * @param c the 3rd object.
   * @param d the 4th object.
   * @param e the 5th object.
   * @return the tuple.
   */
  public static <A,B,C,D,E> Tuple5<A,B,C,D,E> tuple(A a, B b, C c, D d, E e){
    return new Tuple5<A,B,C,D,E>(a,b,c,d,e);
  }
}
