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
package jfun.parsec.tokens;

/**
 * Special token to represent end of input.
 * @author Ben Yu
 *
 * 2004-11-7
 */
final class TokenEof implements java.io.Serializable{
  private static final TokenEof singleton = new TokenEof();
  private TokenEof(){}
  public String toString(){return "EOF";}
  /**
   * creates a TokenEof instance.
   * @return the token object.
   */
  static TokenEof instance(){return singleton;}
}
