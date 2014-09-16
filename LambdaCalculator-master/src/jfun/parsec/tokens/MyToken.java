/*****************************************************************************
 * Copyright (C) Zephyr Business Solutions Corp. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/
/*
 * Created on Dec 8, 2004
 *
 * Author Ben Yu
 */
package jfun.parsec.tokens;

import jfun.parsec.Tokenizer;

/**
 * Used to differentiate different token kind by a kind id.
 * It is to avoid creating multiple simple tokens.
 * @author Ben Yu
 *
 * Dec 8, 2004
 * 
 */
@Deprecated
public class MyToken extends TokenWord
implements java.io.Serializable{
  private final int kind;
  
  public boolean equals(Object v) {
    if(v instanceof MyToken){
      final MyToken mt2 = (MyToken)v;
      return kind == mt2.kind && getWord().equals(mt2.getWord()); 
    }
    else return false;
  }
  public String toString() {
    return super.toString() + ":" + kind;
  }
  public int hashCode() {
    return super.hashCode() * 31 + kind;
  }
  /**
   * @param text the text this token represents. 
   * @param kind the token kind.
   */
  MyToken(final String text, int kind) {
    super(text);
    this.kind = kind;
  }
  /**
   * Get the kind number.
   * @return the kind number.
   */
  public final int getKind(){return kind;}
  /**
   * Create a tokenizer that's gonna return a MyToken object with a certain kind number.
   * @param k the kind number.
   * @return the Tokenizer object.
   */
  public static Tokenizer getTokenizer(final int k){
    return new Tokenizer(){
      public Object toToken(final CharSequence cs, final int from, final int len){
        return new MyToken(cs.subSequence(from, from+len).toString(), k);
      }
    };
  }
}
