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
 * The token that represents any integer literal with arbitrary length.
 * @author Ben Yu
 *
 * 2004-11-15
 */
@Deprecated
public class TokenInteger extends TypedToken<TokenType>
implements java.io.Serializable{

  /**
   * @param raw the string representation.
   */
  TokenInteger(final String raw) {
    super(raw, TokenType.Integer);
  }


  /**
   * gets the string representation of the decimal.
   * @return the integral part.
   */
  public final String getString(){return getText();}

  private static final Tokenizer tn = new Tokenizer(){
    public Object toToken(final CharSequence cs,
        final int from, final int len){
      return new TokenInteger(cs.subSequence(from, from+len).toString());
    }
  };
  /**
   * Creates a Tokenizer that's gonna tokenize any valid decimal literal string to a TokenDecimal object.
   * @return the Tokenizer instance.
   */
  public static Tokenizer getTokenizer(){return tn;}
}
