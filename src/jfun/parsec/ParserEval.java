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
 * This is to provide laziness of a Parser.
 * When mutual recursive dependency exists, it is mandatory to declare the depended parser to be lazy.
 * use Parsers.lazy(new ParserEval(){
 *   public Parser eval(){return myparser();}
 * });
 * @author Ben Yu
 *
 * 2004-11-14
 */
public interface ParserEval<R> extends java.io.Serializable{
  /**
   * do the evaluation.
   * @return the evaluated Parser object.
   */
  Parser<R> eval();
}
