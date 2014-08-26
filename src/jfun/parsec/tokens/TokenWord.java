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

import jfun.parsec.Tokenizer;

/**
 * Represents a word.
 * @author Ben Yu
 *
 * 2004-11-7
 */
@Deprecated
public class TokenWord extends TypedToken<TokenType>
implements java.io.Serializable{

  /**
   * gets the word value.
   * @return the word value.
   */
  public final String getWord(){return getText();}
  TokenWord(final String n){
    super(n, TokenType.Word);
  }
  private static final Tokenizer nTokenizer = new Tokenizer(){
    public Object toToken(final CharSequence cs, final int from, final int len){
      return new TokenWord(cs.subSequence(from, from+len).toString());
    }
  };
  /**
   * gets an instance of TokenWord that parses a input range to a TokenWord.
   * @return the tokenizer instance.
   */
  public static Tokenizer getTokenizer(){return nTokenizer;}
}
