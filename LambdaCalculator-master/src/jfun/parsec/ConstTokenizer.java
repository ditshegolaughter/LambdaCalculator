/*****************************************************************************
 * Copyright (C) Zephyr Business Solutions Corp. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/
/*
 * Created on 2004-11-15
 *
 * Author Ben Yu
 */
package jfun.parsec;

/**
 * This tokenizer will always return the same token regardless of the input range.
 * @author Ben Yu
 *
 * 2004-11-15
 */
public final class ConstTokenizer implements Tokenizer, java.io.Serializable {

  /*
   * @see jfun.parsec.scanner.Tokenizer#toToken(java.lang.CharSequence, int, int)
   */
  public Object toToken(CharSequence cs, int from, int len) {
    return tok;
  }
  private final Object tok;
  private ConstTokenizer(final Object t){this.tok = t;}
  /**
   * Creates the Tokenizer instance.
   * @param t the token to be returned by the tokenizer.
   * @return the tokenizer instance.
   */
  public static Tokenizer instance(final Object t){
    return new ConstTokenizer(t);
  }
}
