/*****************************************************************************
 * Copyright (C) Zephyr Business Solutions Corp. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/
/*
 * Created on Nov 18, 2004
 *
 * Author Ben Yu
 */
package jfun.parsec;

/**
 * Describes Parse error.
 * @author Ben Yu
 *
 * Nov 18, 2004
 */
public interface ParseError extends java.io.Serializable{
  
  /**
   * Gets the index number in the original source.
   * @return the index number.
   */
  int getIndex();
  /**
   * Get the "... encountered" error.
   * @return the actually encountered token when error happens. 
   */
  String getEncountered();
  /**
   * Get the "expecting ..." errors.
   * @return all the expectings.
   */
  String[] getExpecting();
  /**
   * Get the "unexpected ..." error.
   * @return all the unexpected.
   */
  String[] getUnexpected();
  /**
   * Get the user error messages.
   * @return all the user-provided message.
   */
  String[] getMessages();
}
