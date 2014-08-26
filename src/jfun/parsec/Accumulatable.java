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
 * Factory to create an Accumulator object.
 * @author Ben Yu
 *
 * 2004-11-12
 */
public interface Accumulatable<From,To> extends java.io.Serializable{
  Accumulator<From,To> getAccumulator();
}
