/*****************************************************************************
 * Copyright (C) Zephyr Business Solutions Corp. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/
/*
 * Created on 2004-11-16
 *
 * Author Ben Yu
 */
package jfun.parsec;

/**
 * maps 5 objects to an Object.
 * Replaceable with Map5<A,B,C,D,E,R>.
 * <p> a -> b -> c -> d -> e -> r
 * @author Ben Yu
 *
 * 2004-11-16
 */
public interface Map5<A,B,C,D,E,R> extends java.io.Serializable{
  R map(A a, B b, C c, D d, E e);
}
