/*****************************************************************************
 * Copyright (C) Zephyr Business Solutions Corp. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/
/*
 * Created on 2004-11-7
 *
 * Author Ben Yu
 */
package jfun.parsec;

/**
 * A tokenizer is used by a scanner object to interpret the output of a Scanner.
 * 
 * @author Ben Yu
 *
 * 2004-11-7
 */
public interface Tokenizer extends java.io.Serializable{
  /**
   * interprets the matched input range to a token object.
   * @param cs - the input
   * @param from - the starting point
   * @param len - the length of the range
   * @return - the token object.
   */
  Object toToken(final CharSequence cs,
      final int from, final int len);
}
