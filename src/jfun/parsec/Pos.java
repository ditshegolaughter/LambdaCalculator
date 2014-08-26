/*****************************************************************************
 * Copyright (C) Zephyr Business Solutions Corp. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/
/*
 * Created on Dec 12, 2004
 *
 * Author Ben Yu
 */
package jfun.parsec;

/**
 * Pos represents a position in the source.
 * @author Ben Yu
 *
 * Dec 12, 2004
 */
public final class Pos implements java.io.Serializable{
  private final int lno;
  private final int cno;
  
  /**
   * Create a Pos object.
   * @param lno
   * @param cno
   */
  public Pos(final int lno, final int cno) {
    super();
    this.lno = lno;
    this.cno = cno;
  }
  
  /**
   * Get the column number.
   * @return Returns the cno.
   */
  public int getColumnNo() {
    return cno;
  }
  /**
   * Get the line number.
   * @return Returns the lno.
   */
  public int getLineNo() {
    return lno;
  }

  public boolean equals(Object obj) {
    if(obj instanceof Pos){
      final Pos other = (Pos)obj;
      return lno == other.lno && cno==other.cno;
    }
    else return false;
  }

  public int hashCode() {
    return lno*31+cno;
  }

  public String toString() {
    return "line " + lno + " column "+cno;
  }
  
}
