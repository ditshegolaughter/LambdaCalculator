/*****************************************************************************
 * Copyright (C) Zephyr Business Solutions Corp. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/
/*
 * Created on 2004-11-12
 *
 * Author Ben Yu
 */
package jfun.parsec;

/**
 * maps a token object to an Object.
 * <br>
 * Implement your own FromToken when you have your own Token classes.
 * <p> SourcePos -> Token -> a
 * @author Ben Yu
 *
 * 2004-11-12
 */
public interface FromToken<R> extends java.io.Serializable{
  /**
   * Transform a token to an Object.
   * @param tok the token
   * @return the transformed Object.
   */
  public R fromToken(final Tok tok);
}
