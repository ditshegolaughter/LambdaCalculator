/*****************************************************************************
 * Copyright (C) Zephyr Business Solutions Corp. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/
/*
 * Created on Jan 20, 2005
 *
 */
package jfun.util;

/**
 * A simple, efficient and dynamic int array.
 * <p>
 * @author Ben Yu.
 *
 */
public final class IntArray {
  private int[] buf;
  private int len = 0;
  /**
   * Create a int[] object with all the elements stored in this IntArray.
   * @return the int[] object.
   */
  public int[] toArray(){
    final int[] ret = new int[len];
    for(int i=0; i<len; i++){
      ret[i] = buf[i];
    }
    return ret;
  }
  /**
   * Create an IntArray object with an initial capacity.
   * @param capacity the initial capacity.
   */
  public IntArray(int capacity){
    this.buf = new int[capacity];
  }
  /**
   * Create an IntArray object.
   */
  public IntArray(){
    this(10);
  }
  /**
   * Get the number of int values stored.
   */
  public int size(){
    return len;
  }
  private void checkIndex(int i){
    if(i<0 || i>=len)
      throw new ArrayIndexOutOfBoundsException(i);
  }
  /**
   * Get the int value at a certain index.
   * @param i the 0-based index of the value. 
   * @return the int value.
   * @throws ArrayIndexOutOfBoundsException if i is negative or >= size().
   */
  public int get(int i){
    checkIndex(i);
    return buf[i];
  }
  /**
   * Set the value at a certain index.
   * @param i the 0-based index.
   * @param val the new value.
   * @return the old value.
   * @throws ArrayIndexOutOfBoundsException if i is negative or >= size().
   */
  public int set(int i, int val){
    checkIndex(i);
    final int old = buf[i];
    buf[i] = val;
    return old;
  }
  private static int calcSize(int l, int factor){
    final int rem = l%factor;
    return l/factor*factor + (rem>0?factor:0);
  }
  /**
   * Ensure that the IntArray has at least "l" capacity.
   * @param capacity the minimal capacity.
   */
  public void ensureCapacity(int capacity){
    if(capacity > buf.length){
      final int factor = buf.length / 2 + 1;
      grow(calcSize(capacity-buf.length, factor));
    }
  }
  private void grow(int l){
    final int[] nbuf = new int[buf.length+l];
    for(int i=0; i<buf.length; i++){
      nbuf[i] = buf[i];
    }
    buf = nbuf;
  }
  /**
   * Add a int into the array.
   * @param i the int value.
   * @return this IntArray object.
   */
  public IntArray add(int i){
    ensureCapacity(len+1);
    buf[len++] = i;
    return this;
  }
}
