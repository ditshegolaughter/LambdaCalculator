/*****************************************************************************
 * Copyright (C) Zephyr Business Solutions Corp. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/
/*
 * Created on Dec 12, 2004
 *
 * Author Ben Yu
 */
package jfun.parsec;


/**
 * The interface to find the line number,
 * column number of a certain position in the source.
 * 
 * @author Ben Yu
 *
 * Dec 12, 2004
 */
public interface PositionMap extends java.io.Serializable{
  /**
   * Get the line/column number of a position identified by an offset number.
   * @param n the offset in the source, starting from 0.
   * @return the Pos object.
   */
  Pos getPos(int n);
}
