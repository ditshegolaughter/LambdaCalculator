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
package jfun.parsec;

/**
 * This class carries the position information of a token.
 * @since version 1.0
 * @author Ben Yu
 *
 * 2004-11-7
 */
@SuppressWarnings("deprecation")
public final class Tok extends PositionedToken{
  /**
   * Create a Tok object.
   * @param i the starting index.
   * @param l the length of the token.
   * @param tok the token.
   */
  public Tok(final int i, final int l, final Object tok) {
    super(i, l, tok);
  }
}
