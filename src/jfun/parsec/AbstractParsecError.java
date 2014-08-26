/*****************************************************************************
 * Copyright (C) Zephyr Business Solutions Corp. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/
/*
 * Created on Feb 24, 2005
 *
 * Author Ben Yu
 * ZBS
 */
package jfun.parsec;

/**
 * Internally used by the parsec framework.
 * Because merging error messages can be expensive,
 * this class is introduced to delay the expensive operation until render time.
 * Operations in this class are used in Parsers and Scanners class.
 * <p>
 * Zephyr Business Solutions Corp.
 *
 * @author Ben Yu
 *
 */
abstract class AbstractParsecError implements java.io.Serializable{
  final Object getException(){return exception;}
  final boolean hasException(){return exception!=null;}
  abstract ParsecError render();
  abstract AbstractParsecError setExpecting(String s);
  final int getPrecedence(){return precedence;}
  public final int getIndex(){return at;}
  final boolean noMerge(){return nomerge;}
  private final boolean nomerge;
  private final int at;
  private final int precedence;
  private final Object exception;
  AbstractParsecError(final boolean nomerge, final int at,
      final int pred, final Object exception) {
    this.nomerge = nomerge;
    this.at = at;
    this.precedence = pred;
    this.exception = exception;
  }
  static AbstractParsecError mergeError(final AbstractParsecError e1, 
      final AbstractParsecError e2){
    if(e1==null) return e2;
    if(e2==null) return e1;
    if(e1==e2) return e1;
    final int pred = e1.precedence;
    final int pred2 = e2.precedence;
    final int at = e1.at;
    final int at2 = e2.at;
    if(at==at2){
      if(pred2>pred){
        return e2;
      }
      else if(pred>pred2){
        return e1;
      }
      //else return e1;
    }
    else if(at > at2){
      /*if(pred < pred2){
        return e2;
      }
      else */return e1;
    }
    else if(at < at2){
      /*if(pred > pred2){
        return e1;
      }
      else */return e2;
    }
    if(e1.nomerge && e2.nomerge){
      return e1;
    }
    return new MergedParsecError(at, pred, e1, e2);
  }
}
