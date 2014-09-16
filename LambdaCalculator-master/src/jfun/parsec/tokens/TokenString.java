/*****************************************************************************
 * Copyright (C) Zephyr Business Solutions Corp. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/
/*
 * Created on Dec 20, 2004
 *
 * Author Ben Yu
 */
package jfun.parsec.tokens;

import jfun.parsec.Tokenizer;

/**
 * This implementation of Tokenizer transforms the recognized
 * character range to a String.
 * @author Ben Yu
 *
 * Dec 20, 2004
 */
@Deprecated
public final class TokenString implements Tokenizer,
java.io.Serializable{

  /**
   * 
   */
  private TokenString() {}
  private static final Tokenizer singleton = new TokenString();
  /**
   * Get the Tokenizer instance.
   * @return the Tokenizer instance.
   */
  public static Tokenizer getTokenizer(){return singleton;}
  /*
   * @see jfun.parsec.Tokenizer#toToken(java.lang.CharSequence, int, int)
   */
  public Object toToken(CharSequence cs, int from, int len) {
    return cs.subSequence(from, from+len);
  }

}
