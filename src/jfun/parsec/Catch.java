/*****************************************************************************
 * Copyright (C) Zephyr Business Solutions Corp. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/
/*
 * Created on 2004-11-12
 *
 * Author Ben Yu
 */
package jfun.parsec;

/**
 * To catch a pseudo-exception 'thrown' from a Parser.
 * A pseudo-exception is not a real java exception.
 * It only affects the execution of a parser and can only be caught by Parsers.tryParser method.
 * @author Ben Yu
 *
 * 2004-11-12
 */
public interface Catch<T> extends java.io.Serializable{
  /**
   * in case of a pseudo-exception e happens, user can specify what Parser to use.
   * @param v the return value of the exceptional parser.
   * @param e the exception thrown.
   * @return the new Parser object.
   */
  Parser<T> catchException(final Object v, final Object e);
}
