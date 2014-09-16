/*****************************************************************************
 * Copyright (C) Zephyr Business Solutions Corp. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/
/*
 * Created on 2004-11-11
 *
 * Author Ben Yu
 */
package jfun.parsec;

/**
 * @author Ben Yu
 *
 * 2004-11-11
 */
final class ParsecError extends AbstractParsecError implements ParseError{
  //private static final String[] err0 = new String[0];
  ParsecError render(){return this;}
  private final Object sys_unexpected;
  private final String[] unexpected;
  private final String[] expecting;
  private final String[] raw;
  static int getPrecedenceForExpecting(String s){
    return s!=null?2:1;
  }
  /**
   * @param lno
   * @param cno
   * @param msgs
   */
  private ParsecError(final boolean nm, final int at, 
      Object sys, String[] unexpected, String[] expecting, String[] raw, 
      Object exception) {
    super(nm, at, (expecting != null || unexpected != null || raw != null)?
        2:1, exception);
    this.sys_unexpected = sys;
    this.unexpected = unexpected;
    this.expecting = expecting;
    this.raw = raw;
    //Benchmark.instance().inc("ParsecError");
  }
  AbstractParsecError setExpecting(final String s){
    return new ParsecError(false, getIndex(),  
        sys_unexpected, unexpected, new String[]{s}, raw, getException());
  }
  static ParsecError raiseRaw(final int at, 
      final String msg){
    return new ParsecError(false, at, null, null, null, new String[]{msg}, null);
  }
  static ParsecError raiseSysUnexpected(final int at, 
      final Object obj){
    return new ParsecError(true, at, obj, null, null, null, null);
  }
  static ParsecError raiseUnexpected(final int at
      ,final String s){
    return new ParsecError(false, at, null, new String[]{s}, null, null, null);
  }
  static ParsecError raiseExpecting(final int at, 
      final String s){
    return new ParsecError(false, at, null, null, new String[]{s}, null, null);
  }
  static AbstractParsecError raiseExpecting(final int at, final String s,
      final AbstractParsecError err){
    return new ParsecErrorExpecting(false, at,
        myPrecedence(err.getPrecedence(), s), err.getException(),
        err, s);
  }
  private static int max(int a, int b){
    return a>b?a:b;
  }
  private static int myPrecedence(int pred, String s){
     return max(pred, ParsecError.getPrecedenceForExpecting(s));
  }
  static ParsecError throwException(final int at, 
      final Object e){
    return new ParsecError(false, at, null, null, null, null, e);
  }
  Object getSysUnexpected(){return sys_unexpected;}
  public String getEncountered(){
    if(sys_unexpected == null) return null;
    else return sys_unexpected.toString();
  }
  public String[] getUnexpected(){return unexpected;}
  public String[] getExpecting(){return expecting;}
  public String[] getMessages(){return raw;}
  static ParsecError noError(){
    return null;
  }
  private static String[] mergeMsgs(final String[] a, final String[] b){
    if(a==null)return b;
    if(b==null)return a;
    if(a==b)return a;
    final String[] msgs = new String[a.length+b.length];
    /*for(int i=0; i<a.length; i++){
      msgs[i] = a[i];
    }
    for(int i=0; i<b.length; i++){
      msgs[i+a.length] = b[i];
    }*/
    System.arraycopy(a, 0, msgs, 0, a.length);
    System.arraycopy(b, 0, msgs, a.length, b.length);
    return msgs;
  }
  static ParsecError mergeError(
      final int ind, final Object exception, final ParsecError e1, final ParsecError e2){
    return new ParsecError(false, ind, 
        MergedParsecError.mergeObj(e1.sys_unexpected, e2.sys_unexpected),
        mergeMsgs(e1.unexpected, e2.unexpected),
        mergeMsgs(e1.expecting, e2.expecting),
        mergeMsgs(e1.raw, e2.raw),
        exception
    );
  }
}
