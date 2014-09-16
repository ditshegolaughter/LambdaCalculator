/*****************************************************************************
 * Copyright (C) Zephyr Business Solutions Corp. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/
/*
 * Created on 2004-11-13
 *
 * Author Ben Yu
 */
package jfun.parsec;


/**
 * Default ShowToken implementation that uses toString() to convert. 
 * @author Ben Yu
 *
 * 2004-11-13
 */
public final class Token2String implements ShowToken,
java.io.Serializable{
  private static final ShowToken singleton = new Token2String();
  public String show(final Object t){return t.toString();}
  public static ShowToken instance(){return singleton;}
}
