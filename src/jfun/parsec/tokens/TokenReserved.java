/*****************************************************************************
 * Copyright (C) Zephyr Business Solutions Corp. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/
/*
 * Created on Nov 20, 2004
 *
 * Author Ben Yu
 */
package jfun.parsec.tokens;

/**
 * token for any reserved word.
 * 
 * @author Ben Yu
 *
 * Nov 20, 2004
 */
@Deprecated
public class TokenReserved extends TypedToken<TokenType>
implements java.io.Serializable{
  /**
   * Get the value of the reserved word.
   * @return the value of the reserved word.
   */
  public final String getValue(){return getText();}
  TokenReserved(final String n){
    super(n, TokenType.Reserved);
  }
}
