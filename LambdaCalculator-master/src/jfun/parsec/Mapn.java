/*****************************************************************************
 * Copyright (C) Zephyr Business Solutions Corp. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/
/*
 * Created on Dec 1, 2004
 *
 * Author Ben Yu
 */
package jfun.parsec;

/**
 * Responsible for mapping an array of n objects to one object.
 * @author Ben Yu
 *
 * Dec 1, 2004
 */
public interface Mapn<R> extends java.io.Serializable{
  /**
   * Map n objects to one.
   * @param vals the array of objects to map.
   * @return the map result.
   */
  R map(Object... vals);
}
