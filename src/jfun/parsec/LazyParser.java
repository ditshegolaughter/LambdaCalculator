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
 * @author Ben Yu
 *
 * 2004-11-14
 */
final class LazyParser<T> extends Parser {
  boolean apply(ParseContext ctxt) {
    return eval.eval().apply(ctxt);
  }
  boolean apply(ParseContext ctxt, int look_ahead){
    return eval.eval().apply(ctxt, look_ahead);
  }
  private final ParserEval<T> eval;
  
  /**
   * @param eval
   */
  private LazyParser(final String name, final ParserEval eval) {
    super(name);
    this.eval = eval;
  }
  static <R> Parser<R> instance(final String name, final ParserEval<R> e){
    return new LazyParser<R>(name, e);
  }
}
