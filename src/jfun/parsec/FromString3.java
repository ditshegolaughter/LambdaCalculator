/*****************************************************************************
 * Copyright (C) Zephyr Business Solutions Corp. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/
/*
 * Created on Nov 20, 2004
 *
 * Author Ben Yu
 */
package jfun.parsec;

/**
 * maps 3 strings to object.
 * <p> CharSequence -> CharSequence -> CharSequence -> a
 * @author Ben Yu
 *
 * Nov 20, 2004
 */
public interface FromString3<R> extends java.io.Serializable{
  /**
   * @param from the starting index of this character token.
   * @param len the length of this token.
   * @param s1 1st string.
   * @param s2 2nd string.
   * @param s3 3rd string.
   * @return the Object.
   */
  R fromString3(int from, int len, String s1, String s2, String s3);
}
