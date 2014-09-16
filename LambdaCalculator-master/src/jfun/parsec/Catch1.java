/*****************************************************************************
 * Copyright (C) Zephyr Business Solutions Corp. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/
/*
 * Created on 2004-11-13
 *
 * Author Ben Yu
 */
package jfun.parsec;


/**
 * Catch1 implements Catch and recovers the exception only when the exception is the same object that it expects.
 * @author Ben Yu
 *
 * 2004-11-13
 */
public final class Catch1<T> implements Catch<T>, java.io.Serializable {

  /**
   * if e is the same as the target object that this Catch object is expecting,
   * it recovers the parser and make v the current return value.
   * @see jfun.parsec.Catch#catchException(java.lang.Object, java.lang.Object)
   */
  public Parser<T> catchException(Object v, Object e) {
    if(e==target) return Parsers.retn(target);
    else return Parsers.raise("uncaught", e);
  }
  private final T target;
  

  /**
   * Create a Catch1 object.
   * @param target the expected target exception object.
   */
  public Catch1(final T target) {
    this.target = target;
  }
}
