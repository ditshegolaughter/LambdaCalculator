/*****************************************************************************
 * Copyright (C) Zephyr Business Solutions Corp. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/
/*
 * Created on Nov 29, 2004
 *
 * Author Ben Yu
 */
package jfun.parsec;

/**
 * To compare two integers.
 * @author Ben Yu
 *
 * Nov 29, 2004
 */
interface IntOrder extends java.io.Serializable{
  /**
   * compare two integers.
   * @param a 1st int
   * @param b 2nd int
   * @return the comparison result.
   */
  boolean compare(int a, int b);
}
