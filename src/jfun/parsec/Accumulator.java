/*****************************************************************************
 * Copyright (C) Zephyr Business Solutions Corp. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/
package jfun.parsec;

/**
 * Used to accumulate objects.
 * Parsers.manyAccum() use Accumulator to collect return values.
 * Can be parameterized as Accumulator<T,R>
 * @author Ben Yu
 *
 * 2004-11-12
 */
public interface Accumulator<From,To> extends java.io.Serializable{
  /**
   * accumulate one object into the result.
   * @param obj the object to be accumulated.
   */
  void accumulate(From obj);
  /**
   * gets the accumulated result.
   * @return the result.
   */
  To getResult();
}
