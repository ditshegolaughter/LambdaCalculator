/*****************************************************************************
 * Copyright (C) Zephyr Business Solutions Corp. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/
/*
 * Created on Nov 29, 2004
 *
 * Author Ben Yu
 */
package jfun.parsec;

/**
 * Provide some common IntOrder implementations.
 * @author Ben Yu
 *
 * Nov 29, 2004
 */
final class IntOrders {
  private static final IntOrder _lt = new IntOrder(){
    public boolean compare(int a, int b){return a<b;}
  };
  private static final IntOrder _gt = new IntOrder(){
    public boolean compare(int a, int b){return a>b;}
  };
  /**
   * An IntOrder instance that determines if the first integer is less than the second one.
   * lt().compare(1,2) == true.
   * @return the IntOrder instance.
   */
  public static IntOrder lt(){return _lt;}
  /**
   * An IntOrder instance that determines if the first integer is greater than the second one.
   * @return the IntOrder instance.
   */
  public static IntOrder gt(){return _gt;}
}
