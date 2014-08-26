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
 * maps two objects to one object.
 * Replaceable to Map2<A,B,R>.
 * <p> a -> b -> r
 * @author Ben Yu
 *
 * 2004-11-12
 */
public interface Map2<A,B,R> extends java.io.Serializable{
  R map(A o1, B o2);
}
