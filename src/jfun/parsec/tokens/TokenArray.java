/*****************************************************************************
 * Copyright (C) Zephyr Business Solutions Corp. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/
/*
 * Created on Dec 9, 2004
 *
 * Author Ben Yu
 */
package jfun.parsec.tokens;

import java.util.ArrayList;

import jfun.parsec.Tok;

/**
 * @author Ben Yu
 *
 * Dec 9, 2004
 */
final class TokenArray extends ArrayList {
  TokenArray(int init){super(init);}
  TokenArray(){}
  void addToken(Tok tok){
    super.add(tok);
  }
  void retain(int cnt){
    removeRange(cnt, size());
  }
  Tok[] toTokenArray(){
    final Tok[] ret = new Tok[size()];
    toArray(ret);
    return ret;
  }
}
