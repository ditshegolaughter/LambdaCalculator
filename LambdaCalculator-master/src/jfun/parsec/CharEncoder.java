/*****************************************************************************
 * Copyright (C) Zephyr Business Solutions Corp. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/
/*
 * Created on Dec 19, 2004
 *
 * Author Ben Yu
 */
package jfun.parsec;

/**
 * @author Ben Yu
 *
 * Dec 19, 2004
 */
final class CharEncoder {
  public static String encode(char c){
    switch(c){
      case ' ': return "SPACE";
      case '\t': return "HTAB";
      case '\r': return "CR";
      case '\n': return "LF";
      default: return ""+c;
    }
  }
}
