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
 * A factory to create arrays.
 * @author Ben Yu
 *
 * Nov 19, 2004
 */
public interface ArrayFactory<T> extends java.io.Serializable{
  /**
   * Create an array of length len.
   * @param len the array length.
   * @return the array.
   */
  T[] createArray(int len);
}
