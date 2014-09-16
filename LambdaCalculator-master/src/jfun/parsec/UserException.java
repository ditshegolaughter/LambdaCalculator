/*****************************************************************************
 * Copyright (C) Zephyr Business Solutions Corp. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/
/*
 * Created on Jan 29, 2005
 *
 * Author Ben Yu
 * ZBS
 */
package jfun.parsec;

/**
 * User code can throw this exception
 * when a non-recoverable error is encountered.
 * The framework will transform it to ParserException.
 * <p> Zephyr Business Solutions Corp.
 *
 * @author Ben Yu
 *
 */
public class UserException extends RuntimeException {
  private final int ind;
  
  /**
   * Get the index in the original source.
   */
  public int getInd() {
    return ind;
  }
  /**
   * Create a UserException object.
   * @param msg the error message.
   */
  public UserException(String msg){
    this(-1, msg);
  }
  /**
   * Create a UserException object.
   * @param msg the error message.
   */
  public UserException(String msg, Throwable cause){
    this(-1, msg, cause);
  }
  /**
   * Create a UserException object.
   * @param ind the index in the original source.
   * -1 if the index is unknown.
   */
  public UserException(final int ind) {
    this.ind = ind;
  }


  /**
   * Create a UserException object.
   * @param ind the index in the original source.
   * -1 if the index is unknown.
   * @param msg the error message.
   */
  public UserException(final int ind, String msg) {
    super(msg);
    this.ind = ind;
  }
  /**
   * Create a UserException object.
   * @param ind the index in the original source.
   * -1 if the index is unknown.
   * @param msg the error message.
   * @param arg1 the chained exception.
   */
  public UserException(int ind, String msg, Throwable arg1) {
    super(msg, arg1);
    this.ind = ind;
  }
  /**
   * Create a UserException object.
   * @param ind the index in the original source.
   * -1 if the index is unknown.
   * @param arg0 the chained exception.
   */
  public UserException(int ind, Throwable arg0) {
    super(arg0);
    this.ind = ind;
  }
}
