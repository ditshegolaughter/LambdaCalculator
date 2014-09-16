/*****************************************************************************
 * Copyright (C) Zephyr Business Solutions Corp. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/
/*
 * Created on Dec 20, 2004
 *
 * Author Ben Yu
 */
package jfun.parsec;

/**
 * Maps an Object to a boolean value.
 * @author Ben Yu
 *
 * Dec 20, 2004
 */
public interface ObjectPredicate<T> extends java.io.Serializable{
  /**
   * Tests whether the given object satisfies the predicate.
   * @param v the object to test.
   * @return true if satisfies, false otherwise.
   */
  boolean isObject(T v);
}
