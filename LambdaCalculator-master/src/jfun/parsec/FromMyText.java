/*****************************************************************************
 * Copyright (C) Zephyr Business Solutions Corp. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/
/*
 * Created on Dec 8, 2004
 *
 * Author Ben Yu
 */
package jfun.parsec;

/**
 * Used by class Terms to identify a MyToken object. 
 * @author Ben Yu
 *
 * Dec 8, 2004
 */
public interface FromMyText<R> extends java.io.Serializable{
  /**
   * transform a text and a kind to an Object.
   * @param from the starting index of this character token.
   * @param len the length of this token.
   * @param text the text string.
   * @param kind the kind identifier.
   * @return the object.
   */
  R fromMyText(int from, int len, String text, int kind);
}
