/*****************************************************************************
 * Copyright (C) Zephyr Business Solutions Corp. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/
/*
 * Created on Dec 19, 2004
 *
 * Author Ben Yu
 */
package jfun.parsec;


/**
 * @author Ben Yu
 *
 * Dec 19, 2004
 */
final class WordsData implements java.io.Serializable{
  private final Map<String, Object> toks;
  private final Parser<Tok>[] lexers;
  
  /**
   * @param parsers
   * @param lexers
   */
  WordsData(final Map<String, Object> toks, final Parser<Tok>[] lexers) {
    this.toks = toks;
    this.lexers = lexers;
  }
  /**
   * @return Returns the lexers.
   */
  Parser<Tok>[] getLexers() {
    return lexers;
  }
  /**
   * @return Returns the token objects.
   */
  Map<String, Object> getTokens() {
    return toks;
  }
}
