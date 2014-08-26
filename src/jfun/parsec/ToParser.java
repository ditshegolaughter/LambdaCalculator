/*****************************************************************************
 * Copyright (C) Zephyr Business Solutions Corp. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/
/*
 * Created on 2004-11-11
 *
 * Author Ben Yu
 */
package jfun.parsec;

/**
 * Maps an Object to a Parser.
 * User code implements this interface to call Parsers.bind(). <br>
 * <p> a -> Parser b.
 * @author Ben Yu
 *
 * 2004-11-11
 */
public interface ToParser<From, To> extends java.io.Serializable{
  /**
   * maps v to a Parser.
   * @param v the value.
   * @return the new Parser.
   */
  Parser<To> toParser(From v);
}
