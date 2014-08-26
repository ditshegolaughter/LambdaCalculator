/*****************************************************************************
 * Copyright (C) Zephyr Business Solutions Corp. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/

package jfun.parsec.pattern;

/**
 * The interface to test a character value.
 * Created on 2004-11-7
 *
 * Author Ben Yu
 */
public interface CharPredicate extends java.io.Serializable {
  /**
   * tests whether the character satisfies the predicate.
   * @param c
   * @return whether the character satisfies the predicate.
   */
  boolean isChar(final char c);
}
