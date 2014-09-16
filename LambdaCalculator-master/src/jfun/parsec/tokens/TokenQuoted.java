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
package jfun.parsec.tokens;

import jfun.parsec.Tokenizer;

/**
 * represents a string that is quoted by a open and close string.
 * Use this token if the value of open quote and close quote matters to the syntax.
 * @author Ben Yu
 *
 * 2004-11-15
 */
public class TokenQuoted implements java.io.Serializable{
  private final String open;
  private final String close;
  private final String quoted;
  
  /**
   * @param open the open quote
   * @param close the close quote
   * @param quoted the quoted string
   */
  
  TokenQuoted(final String open, final String close, final String quoted) {
    this.open = open;
    this.close = close;
    this.quoted = quoted;
  }
  
  public boolean equals(Object obj) {
    if(obj instanceof TokenQuoted){
      final TokenQuoted tq2 = (TokenQuoted)obj;
      return open.equals(tq2.open) && close.equals(tq2.close)
      && quoted.equals(tq2.quoted);
    }
    else return false;
  }
  public int hashCode() {
    return open.hashCode() + quoted.hashCode() + close.hashCode();
  }
  public String toString(){
    return open+quoted+close;
  }
  /**
   * creates a Tokenizer instance that can parse a string quoted by open and close.
   * @param open the open quote
   * @param close the close quote
   * @return the tokenizer.
   * @deprecated Use {@link Tokenizers#forQuotedString(char, char)} instead.
   */
  public static Tokenizer getTokenizer(final char open, final char close){
    return getTokenizer(""+open, ""+close);
  }
  /**
   * creates a Tokenizer instance that can parse a string quoted by open and close.
   * @param open the opening quote
   * @param close the closeing quote
   * @return the tokenizer.
   * @deprecated Use {@link Tokenizers#forQuotedString(String, String)} instead.
   */
  public static Tokenizer getTokenizer(final String open, final String close){
    return new Tokenizer(){
      public Object toToken(final CharSequence cs, final int from, final int len){
        final int start = from + open.length();
        final int end = from + len - close.length();
        return new TokenQuoted(open, close, cs.subSequence(start, end).toString());
      }
    };
  }
  /**
   * Returns the closing quote.
   * @return the closing quote
   */
  public final String getClose() {
    return close;
  }
  /**
   * Returns the quoted text.
   * @return the quoted text
   */
  public final String getQuoted() {
    return quoted;
  }
  /**
   * Returns the opening quote.
   * @return the opening quote
   */
  public final String getOpen() {
    return open;
  }
}
