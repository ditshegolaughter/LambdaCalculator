/*****************************************************************************
 * Copyright (C) Zephyr Business Solutions Corp. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/
/*
 * Created on 2004-11-14
 *
 * Author Ben Yu
 */
package jfun.parsec;


/**
 * Tests if the token is the same token expected.
 * @author Ben Yu
 *
 * 2004-11-14
 */
public final class IsToken implements FromToken<Tok> {

  /**
   * return the token if the token is the same object that we are expecting.
   * null otherwise.
   */
  public Tok fromToken(Tok tok) {
    if(tok != null && tok.getToken() == target) return tok;
    else return null;
  }
  private final Object target;
  
  /**
   * @param target
   */
  private IsToken(final Object target) {
    this.target = target;
  }
  /**
   * Creates a FromToken instance using the given expected token t.
   * @param t the expected token object.
   * @return the FromToken instance.
   */
  public static FromToken<Tok> instance(final Object t){
    return new IsToken(t);
  }
}
