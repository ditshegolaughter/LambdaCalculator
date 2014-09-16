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

/**
 * This class create some basic tokens.
 * @author Ben Yu
 *
 * 2004-11-15
 */
public final class Tokens {
  /**
   * Create a typed token.
   * @param text the token text.
   * @param type the token type.
   * @return the TypedToken object.
   * @since version 1.1
   */
  public static <T> TypedToken<T> createTypedToken(final String text, T type){
    return new TypedToken<T>(text, type);
  }
  /**
   * create a TokenReserved object.
   * @param n the reserved word.
   * @return the token object.
   */
  public static TokenReserved reserved(final String n){
    return new TokenReserved(n);
  }
  /**
   * create a TokenWord object.
   * @param n the word.
   * @return the token object.
   */
  public static TokenWord word(final String n){return new TokenWord(n);}
  
  /**
   * Create a MyToken object.
   * @param text the text.
   * @param kind the kind.
   * @return the token object.
   */
  @Deprecated
  public static MyToken my(final String text, final int kind){
    return new MyToken(text, kind);
  }
  /**
   * create a decimal literal token object.
   * @param s the decimal string representation.
   * @return the token object.
   */
  public static TokenDecimal decimal_literal(final String s){
    return new TokenDecimal(s);
  }
  /**
   * create a character literal token.
   * @param c the character.
   * @return the token object.
   */
  public static Character char_literal(final char c){
    return new Character(c);
  }
  /**
   * Create a integer litgeral token
   * @param n the number
   * @return the token object.
   * @deprecated use {@link #long_literal(long)} instead.
   */ 
  public static Long int_literal(final long n){
    return long_literal(n);
  }
  /**
   * Create a integer literal token whose value is within the range of a long integer.
   * @param n the number
   * @return the token object.
   * @since version 0.6
   */ 
  public static Long long_literal(final long n){
    return new Long(n);
  }
  /**
   * Create a quoted string token.
   * @param open the open quote
   * @param close the close quote
   * @param s the quoted string
   * @return the token object.
   */
  public static TokenQuoted quoted_string(final String open, final String close, final String s){
    return new TokenQuoted(open, close, s);
  }
  /**
   * Create a string literal token.
   * @param s the string literal.
   * @return the token object.
   */
  public static String str_literal(final String s){
    return s;
  }
  /**
   * Get a token instance for eof.
   * @since version 1.1
   */
  public static TokenEof eof(){
    return TokenEof.instance();
  }
}

