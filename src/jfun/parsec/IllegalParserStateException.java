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
 * Represents an illegal state in parser.
 * @author Ben Yu
 *
 * Dec 19, 2004
 */
public class IllegalParserStateException extends IllegalStateException {
  /**
   * 
   */
  public IllegalParserStateException() {
    super();

  }

  /**
   * @param arg0
   */
  public IllegalParserStateException(String arg0) {
    super(arg0);

  }
}
