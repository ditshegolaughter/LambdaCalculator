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
 * Represents a character.
 * @author Ben Yu
 *
 * 2004-11-7
 */
@Deprecated
public class TokenCharLiteral implements java.io.Serializable{
  /**
   * convert the characters contained in range [from, from+len)
   * to a character.
   * @param cs the character sequence.
   * @param from the starting index.
   * @param len the length of the range.
   * @return the character.
   */
  public static char tokenize(final CharSequence cs, 
      final int from, final int len){
    if(len == 3){
      return cs.charAt(from+1);
    }
    else if(len == 4){
      return cs.charAt(from+2);
    }
    else throw new IllegalStateException("illegal char");
  }
  private TokenCharLiteral(){}
  private static final Tokenizer cTokenizer = new Tokenizer(){
    public Object toToken(final CharSequence cs, 
        final int from, final int len){
      return new Character(tokenize(cs, from, len));
    };
  };
  /**
   * Creates a tokenizer that's gonna tokenize a single quoted character literal possibly with escape character '\'
   * @return the tokenizer instance.
   */
  public static Tokenizer getTokenizer(){return cTokenizer;}
}
