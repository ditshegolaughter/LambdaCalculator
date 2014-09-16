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
 * maps one object to another.
 * Replaceable with Map<From,To>.
 * <p> a -> r
 * @author Ben Yu
 *
 * 2004-11-12
 */
public interface Map<From,To> extends java.io.Serializable{
  To map(From v);
}
