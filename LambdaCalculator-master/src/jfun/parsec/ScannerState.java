/*****************************************************************************
 * Copyright (C) Zephyr Business Solutions Corp. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/
/*
 * Created on Dec 17, 2004
 *
 * Author Ben Yu
 */
package jfun.parsec;



/**
 * @author Ben Yu
 *
 * Dec 17, 2004
 */
final class ScannerState extends ParseContext {
  private final CharSequence src;
  private final int len;
  ScannerState(final CharSequence src, 
      int a, final String module, final PositionMap pmap,
      final Object ustate){
    super(ustate, a, module, pmap);
    this.src = src;
    this.len = src.length();
  }
  ScannerState(final CharSequence src, int a, 
      final String module, final PositionMap pmap, 
      int l, final Object ustate){
    super(ustate, a, module, pmap);
    this.src = src;
    this.len = l;
  }
  ScannerState(final CharSequence src, int a, 
      final String module, final PositionMap pmap,
      int l, final Object r,
      final Object ustate){
    super(r, ustate, a, module, pmap);
    this.src = src;
    this.len = l;
  }
  char peekChar(){
    return src.charAt(at);
  }
  boolean isEof(){
    return len == at;
  }
  CharSequence getSource(){
    return src;
  }
  int length(){return len;}

  /*
   * @see jfun.parsec.ParseContext#getIndex()
   */
  int getIndex() {
    return at;
  }

  /*
   * @see jfun.parsec.ParseContext#getToken()
   */
  Tok getToken() {
    throw new IllegalStateException("Parser not on token level");
  }



  /*
   * @see jfun.parsec.ParseContext#getSysUnexpected()
   */
  ParsecError getSysUnexpected() {
    final String msg = (len==at)?"EOF":(""+src.charAt(at));
    return ParsecError.raiseSysUnexpected(getIndex(), msg);
  }
}
