/*****************************************************************************
 * Copyright (C) Zephyr Business Solutions Corp. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/
/*
 * Created on Nov 19, 2004
 *
 * Author Ben Yu
 */
package jfun.parsec;



/**
 * This class provides some standard Map implementations.
 * @author Ben Yu
 *
 * Nov 19, 2004
 */
public final class Maps {
  private static final Unary<Object> _id = new Unary<Object>(){
    public Object map(Object v) {
      return v;
    }
    public String toString(){return "id";}
  };
  private static final Mapn<Object[]> _idn = new Mapn<Object[]>(){
    public Object[] map(Object... v){
      return v;
    }
  };
  /**
   * Returns an identity map. map a = a.
   * @return the Map instance.
   */
  public static <x> Unary<x> id(){return (Unary<x>)_id;}
  /**
   * Returns an identity map. map a = a.
   * @param type the class literal for the type parameter.
   * @return the Map instance.
   */
  public static <R> Unary<R> id(Class<R> type){
    return id();
  }
  /**
   * Returns an identity map. map a = a.
   * @return the Mapn instance.
   */  
  public static Mapn<Object[]> idn(){return _idn;}
  /**
   * Creates a map that maps any object to the same object.
   * @param v the object that is gonna be returned from the Map.
   * @return the Map instance.
   */
  public static <x, V> Map<x, V> cnst(final V v){
    return new Map<x,V>(){
      public V map(final x from){return v;}
    };
  }
  /**
   * Adapts a java.util.Map to jfun.util.Map.
   * @param m the java.util.Map object.
   * @return the jfun.util.Map instance.
   */
  public static <From,To> Map<From,To> jmap(final java.util.Map<From, To> m){
    return new Map<From,To>(){
      public To map(final From k){
        return m.get(k);
      }
    };
  }
  
  /**
   * Transform a Tok object to the wrapped token object.
   * @return the Map instance.
   */
  public static <T> Map<Tok, T> toToken(){
    return new Map<Tok, T>(){
      public T map(final Tok o){
        return (T)o.getToken();
      }
    };
  }
  private static final Map2 _id2 = new Map2(){
    public Object map(Object o1, Object o2){
      return new Pair(o1, o2);
    }
  };
  private static final Map3 _id3 = new Map3(){
    public Object map(Object o1, Object o2, Object o3){
      return new Tuple3(o1, o2, o3);
    }
  };
  private static final Map4 _id4 = new Map4(){
    public Object map(Object o1, Object o2, Object o3, Object o4){
      return new Tuple4(o1, o2, o3, o4);
    }
  };
  private static final Map5 _id5 = new Map5(){
    public Object map(Object o1, Object o2, Object o3, Object o4, Object o5){
      return new Tuple5(o1, o2, o3, o4, o5);
    }
  };
  /**
   * Create a Map2 object that stores the two objects into a Pair object.
   */
  public static <A,B> Map2<A,B,Pair<A,B>> id2(){
    return _id2;
  }
  /**
   * Create a Map3 object that stores the 3 objects into a Tuple3 object.
   */
  public static <A,B,C> Map3<A,B,C,Tuple3<A,B,C>> id3(){
    return _id3;
  }
  /**
   * Create a Map4 object that stores the 4 objects into a Tuple4 object.
   */
  public static <A,B,C,D> Map4<A,B,C,D,Tuple4<A,B,C,D>> id4(){
    return _id4;
  }
  /**
   * Create a Map5 object that stores the 5 objects into a Tuple5 object.
   */
  public static <A,B,C,D,E> Map5<A,B,C,D,E,Tuple5<A,B,C,D,E>> id5(){
    return _id5;
  }
}
