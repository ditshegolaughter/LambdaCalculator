/*****************************************************************************
 * Copyright (C) Zephyr Business Solutions Corp. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/
/*
 * Created on Dec 16, 2004
 *
 * Author Ben Yu
 */
package jfun.parsec;


/**
 * @author Ben Yu
 *
 * Dec 16, 2004
 */
abstract class ParseContext {
  String getModuleName(){return module;}
  abstract boolean isEof();
  abstract int getIndex();
  abstract Tok getToken();
  abstract char peekChar();
  abstract CharSequence getSource();
  abstract ParsecError getSysUnexpected();
  protected int at;
  private int step;
  private Object userState;
  private AbstractParsecError err;
  private Object ret;
  private final String module;
  private final PositionMap pmap;
  final PositionMap getPositionMap(){
    return pmap;
  }
  final void set(final int step, final int at, final Object ret, final Object userState, final AbstractParsecError err){
    this.step = step;
    this.at = at;
    this.ret = ret;
    this.userState = userState;
    this.err = err;
  }
  /**
   * @return Returns the err.
   */
  final AbstractParsecError getError() {
    return err;
  }
  /**
   * @param err The err to set.
   */
  final void setError(AbstractParsecError err) {
    this.err = err;
  }
  /**
   * @return Returns the ret.
   */
  final Object getReturn() {
    return ret;
  }
  /**
   * @param ret The ret to set.
   */
  final void setReturn(Object ret) {
    this.ret = ret;
  }
  final boolean hasException(){
    return err!=null && err.hasException();
  }
  //private final Message[] msgs;
  final Object getUserState(){return userState;}

  
  final int getStep(){return step;}
  final int getAt(){return at;}
  final void setAt(int step, int a){
    this.step = step;
    this.at = a;
  }
  final void setStep(int s){
    this.step = s;
  }
  final void next(){
    at ++;
    step ++;
  }
  final void next(int n){
    at += n;
    if(n>0) step++;
  }
  final void setUserState(final Object obj){
    userState = obj;
  }
  //caller should not change input after it is passed in.
  ParseContext(final Object us, final int at, final String module, final PositionMap pmap){
    this.userState = us;
    this.step = 0;
    this.at = at;
    this.module = module;
    this.pmap = pmap;
  }
  ParseContext(final Object ret, final Object us, 
      final int at, final String module, final PositionMap pmap){
    this.ret = ret;
    this.userState = us;
    this.step = 0;
    this.at = at;
    this.module = module;
    this.pmap = pmap;
  }  
  final void prependError(final AbstractParsecError err){
    this.err = AbstractParsecError.mergeError(err, this.err);
  }
  final void appendError(final AbstractParsecError err){
    this.err = AbstractParsecError.mergeError(this.err, err);
  }
}
