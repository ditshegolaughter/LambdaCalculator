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
 * To convert a token object to String.
 * This is normally not necessary unless you want fine control over error messages or internationalization.
 * @author Ben Yu
 *
 * 2004-11-12
 */
public interface ShowToken extends java.io.Serializable{
  /**
   * convert a token to a String. 
   * @param tok the token.
   * @return the string.
   */
  public String show(Object tok);
}
