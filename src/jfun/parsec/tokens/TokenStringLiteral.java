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
 * This Token represents a string literal.
 * @author Ben Yu
 *
 * 2004-11-7
 */
@Deprecated
public class TokenStringLiteral implements java.io.Serializable{
  /**
   * creates a Tokenizer instance() that parsers a double quoted string literal that is possiblly escaped by '\'
   * @return the tokenizer instance.
   */
  public static Tokenizer getDoubleQuoteTokenizer(){return sTokenizer;}
  /**
   * creates a Tokenizer instance() that parsers a single quoted string literal.
   * two single quotes represents a single quote.
   * @return the tokenizer instance.
   */
  public static Tokenizer getSqlTokenizer(){return sqlTokenizer;}
  private TokenStringLiteral(){}
  private static final char escapedChar(final char c){
    switch(c){
      case 'r':
        return '\r';
      case 'n':
        return '\n';
      case 't':
        return '\t';
      default:
        return c;
      
    }
  }
  static String tokenizeDoubleQuote(final CharSequence cs, 
      final int from, final int len){
    final int end = from + len -1;
    final StringBuffer buf = new StringBuffer();
    for(int i=from+1; i<end;i++){
      final char c = cs.charAt(i);
      if(c!='\\'){
        buf.append(c);
      }
      else{
        final char c1 = cs.charAt(++i);
        buf.append(escapedChar(c1));
      }
    }
    return buf.toString();
  }
  private static final Tokenizer sTokenizer = new Tokenizer(){
    public Object toToken(final CharSequence cs, final int from, final int len){
      return tokenizeDoubleQuote(cs, from, len);
    }
  };

  private static final Tokenizer sqlTokenizer = new Tokenizer(){
    public Object toToken(final CharSequence cs, final int from, final int len){      
      final int end = from+len-1;
      final StringBuffer buf = new StringBuffer();
      for(int i=from+1; i<end; i++){
        final char c = cs.charAt(i);
        if(c!='\'')
          buf.append(c);
        else{
          buf.append('\'');
          i++;
        }
      }
      return buf.toString();
    }
  };
}
