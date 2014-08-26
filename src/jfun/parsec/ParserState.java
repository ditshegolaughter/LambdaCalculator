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
final class ParserState extends ParseContext{
  
  private final Tok[] input;
  private final ParsecError[] sys_unexpected;
  //in case a terminating eof token is not explicitly created,
  //the implicit one is used.
  private final int end_index;
  private final ParsecError eof_unexpected;
  
  
  private final ShowToken show;

  boolean isEof(){
    return at >= input.length; //|| input[at].getToken()==Tokens.eof();
  }
  int length(){return input.length;}
  int getIndex(){
    if(at==input.length) return end_index;
    return input[at].getIndex();
  }
  

  Tok getToken(){
    return input[at];
  }
  //caller should not change input after it is passed in.
  ParserState(final Object us, final Tok[] input,
      final int at, final String module, final PositionMap pmap,
      final int end_index, 
      final String eof_str, final ShowToken show){
    super(us, at, module, pmap);
    this.input = input;
    this.sys_unexpected = new ParsecError[input.length];
    this.show = show;
    this.end_index = end_index;
    this.eof_unexpected = ParsecError.raiseSysUnexpected(
        end_index, eof_str);
  }
  ParserState(final Object r, final Object us, final Tok[] input,
      final int at, final String module, final PositionMap pmap,
      final int end_index, 
      final String eof_str, final ShowToken show){
    super(r, us, at, module, pmap);
    this.input = input;
    this.sys_unexpected = new ParsecError[input.length];
    this.show = show;
    this.end_index = end_index;
    this.eof_unexpected = ParsecError.raiseSysUnexpected(
        end_index, eof_str);
  }

  ParsecError getSysUnexpected(){
    return getSysUnexpected(at);
  }
  private ParsecError getSysUnexpected(final int i){
    if(i>=sys_unexpected.length) return eof_unexpected;
    ParsecError r = sys_unexpected[i];
    if(r == null){
      final Tok ptok = input[i];
      r = ParsecError.raiseSysUnexpected(ptok.getIndex(), 
          show.show(ptok.getToken()));
      sys_unexpected[i] = r;
    }
    return r;
  }
  char peekChar(){
    throw new IllegalStateException("parser not on char level.");
  }
  char peekChar(int i){
    throw new IllegalStateException("parser not on char level.");
  }
  CharSequence getSource(){
    throw new IllegalStateException("parser not on char level.");
  }
}
