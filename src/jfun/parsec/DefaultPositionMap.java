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

import jfun.util.IntArray;


/**
 * This default implementation of PositionMap.
 * <p>
 * This class internally keeps a cache of the positions of
 * all the line break characters scanned so far,
 * therefore repeated position lookup can be done in amortized log(n) time.
 * </p>
 * @author Ben Yu
 *
 * Dec 12, 2004
 */
public class DefaultPositionMap implements PositionMap, java.io.Serializable {
  private final CharSequence src;
  private final int start_lno;
  private final IntArray line_breaks = new IntArray(20);
  private int next_ind = 0;
  private int next_col;
  private final char line_break;
  
  public int searchLineIndex(final int ind){
    final int len = line_breaks.size();
    int begin = 0;
    int to = len;
    for(;;){
      if(begin==to) return begin;
      final int i = (to+begin)/2;
      final int x = line_breaks.get(i);
      if(x==ind) return i;
      else if(x > ind){
        to = i;
      }
      else{
        begin = i+1;
      }
    }
  }
  private Pos searchPosition(final int ind){
    final int sz = line_breaks.size();
    if(sz==0){
      return newPos(0, ind+1);
    }
    else{
      final int last_break = line_breaks.get(sz-1);
      if(ind > last_break){
        return newPos(sz, ind-last_break);
      }
      else{
        final int lno = searchLineIndex(ind);
        if(lno==0){
          return newPos(0, ind+1);
        }
        else{
          final int previous_break = line_breaks.get(lno-1);
          return newPos(lno, ind-previous_break);
        }
      }
    }
  }
  private Pos searchForward(int ind){
    boolean eof = false;
    if(ind==src.length()){
      eof= true;
      ind--;
    }
    int col = next_col;
    for(int i=next_ind; i<=ind; i++){
      final char c = src.charAt(i);
      if(c == line_break){
        line_breaks.add(i);
        col = 1;
      }
      else{
        col++;
      }
    }
    this.next_ind = ind;
    this.next_col = col;
    final int lines = line_breaks.size();
    if(eof){
      return newPos(lines, col);
    }
    else if(col==1){
      return getLineBreakPos(lines-1);
    }
    else{
      return newPos(lines, col-1);
    }
  }
  private int getLineBreakColumn(int lno){
    final int line_break = line_breaks.get(lno);
    if(lno==0)
      return line_break+1;
    else{
      return line_break - line_breaks.get(lno-1);
    }
  }
  private Pos getLineBreakPos(int lno){
    return newPos(lno, getLineBreakColumn(lno));
  }
  private Pos newPos(int l, int c){
    return new Pos(start_lno+l, c);
  }
  //private final int tabwidth;
  /*
   * @see jfun.parsec.scanner.PositionMap#getSourcePos(int)
   */
  public Pos getPos(final int n) {
    //return getPos(n, src, start_lno, start_cno);
    if(n < next_ind){
      return searchPosition(n);
    }
    else{
      return searchForward(n);
    }
  }
  /*
  static Pos getPos(int n,
      final CharSequence src, 
      final int lno, final int cno){
    int ln = lno;
    int cn = cno;
    for(int i=0; i<n; i++){
      final char c = src.charAt(i);
      switch(c){
        case '\n' :
          ln++;
          cn = 1;
          break;
        default:
          cn++;
      }
    }
    return new Pos(ln, cn);
  }*/
  /**
   * Create a DefaultPositionMap object.
   * @param src the source.
   * @param lno the starting line number.
   * @param cno the starting column number.
   * @param line_break the line break character.
   */
  public DefaultPositionMap(final CharSequence src, 
      final int lno, final int cno, final char line_break) {
    this.src = src;
    this.start_lno = lno;
    this.next_col = cno;
    this.line_break = line_break;
  }
  /**
   * Create a DefaultPositionMap object.
   * @param src the source.
   * @param lno the starting line number.
   * @param cno the starting column number.
   */
  public DefaultPositionMap(final CharSequence src, 
      final int lno, final int cno) {
    this(src, lno, cno, '\n');
  }

}
