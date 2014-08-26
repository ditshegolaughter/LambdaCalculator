/*****************************************************************************
 * Copyright (C) Zephyr Business Solutions Corp. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/
/*
 * Created on Dec 9, 2004
 *
 * Author Ben Yu
 */
package jfun.parsec.tokens;

/**
 * A token that has a name.
 * The name is used in error reporting.
 * @author Ben Yu
 *
 * Dec 9, 2004
 */
public class NamedToken implements java.io.Serializable{
  private final String name;
  
  /**
   * @param name
   */
  NamedToken(final String name) {
    this.name = name;
  }
  /**
   * Get the name.
   * @return Returns the name.
   */
  public final String getName() {
    return name;
  }
  /*
   * Return the name of the token.
   * @see java.lang.Object#toString()
   */
  public String toString(){
    return name;
  }
  
  public boolean equals(Object obj) {
    if(obj instanceof NamedToken){
      final NamedToken nt2 = (NamedToken)obj;
      return name.equals(nt2.name);
    }
    else return false;
  }
  public int hashCode() {
    return name.hashCode();
  }
}
