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
 * The token that represents an integer that is within 64 bits.
 * For bigger integer, use TokenDecimal instead.
 * @author Ben Yu
 *
 * 2004-11-7
 */
@Deprecated
public class TokenLong implements java.io.Serializable{
  private TokenLong(){}
  private static int toDecDigit(final char c){
    return c - '0';
  }
  private static int toOctDigit(final char c){
    return c - '0';
  }
  private static int toHexDigit(final char c){
    if(c>='0' && c<='9') return c-'0';
    if(c>='a' && c<='h') return c-'a'+10;
    else return c-'A'+10;
  }
  private static final Tokenizer dTokenizer = new Tokenizer(){
    public Object toToken(final CharSequence cs, final int from, final int len){
      final int end = from + len;
      long n = 0;
      for(int i=from; i<end; i++){
        n = n*10 + toDecDigit(cs.charAt(i));
      }
      return new Long(n);
    }
  };
  private static final Tokenizer oTokenizer = new Tokenizer(){
    public Object toToken(final CharSequence cs, final int from, final int len){
      final int end = from + len;
      long n = 0;
      for(int i=from; i<end; i++){
        n = n*8 + toOctDigit(cs.charAt(i));
      }
      return new Long(n);
    }
  };
  private static final Tokenizer hTokenizer = new Tokenizer(){
    public Object toToken(final CharSequence cs, final int from, final int len){
      if(len < 3) throw new IllegalStateException("illegal hex number");
      final int end = from + len;
      long n = 0;
      for(int i=from+2; i<end; i++){
        n = n*16 + toHexDigit(cs.charAt(i));
      }
      return new Long(n);
    }
  };
  /**
   * creates a Tokenizer instance that can parse a decimal integer string.
   * @return the Tokenizer instance.
   */
  public static Tokenizer getDecTokenizer(){return dTokenizer;}
  /**
   * creates a Tokenizer instance that can parse a octal integer string.
   * @return the Tokenizer instance.
   */
  public static Tokenizer getOctTokenizer(){return oTokenizer;}
  /**
   * creates a Tokenizer instance that can parse a hex integer string.
   * @return the Tokenizer instance.
   */
  public static Tokenizer getHexTokenizer(){return hTokenizer;}
}
