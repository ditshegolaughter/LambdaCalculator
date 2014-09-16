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
 * Provide some standard ArrayFactory implementations.
 * @author Ben Yu
 *
 * Nov 19, 2004
 */
public final class ArrayFactories {
  /**
   * gets and instance of ArrayFactory that simply create Object[].
   * @return the ArrayFactory instance.
   */
  public static ArrayFactory<Object> defaultFactory(){
    return singleton;
  }
  /**
   * gets and instanceof ArrayFactory that creates T[] for a given element type T.
   * @param etype the element type.
   * @return the ArrayFactory instance.
   */
  public static <E> ArrayFactory<E> typedFactory(final Class<E> etype){
    return new ArrayFactory<E>(){
      public E[] createArray(int l){
        return (E[])java.lang.reflect.Array.newInstance(etype, l);
      }
    };
  }
  private static final ArrayFactory<Object> singleton = new ArrayFactory<Object>(){
    public Object[] createArray(int l){
      return new Object[l];
    }
  };
}
