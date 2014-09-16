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
 * To delay the merging of two AbstractParsecError.
 * The merging is not done until render time.
 * Zephyr Business Solutions Corp.
 *
 * @author Ben Yu
 *
 */
final class MergedParsecError extends AbstractParsecError {
  ParsecError render() {
    return getMerged(err1.render(), err2.render());
  }
  AbstractParsecError setExpecting(String s){
    return ParsecError.raiseExpecting(getIndex(), s, this);
  }
  private ParsecError getMerged(ParsecError e1, ParsecError e2){
    return ParsecError.mergeError(getIndex(), getException(), e1, e2);
  }
  private final AbstractParsecError err1;
  private final AbstractParsecError err2;
  MergedParsecError(int ind, int pred,
      final AbstractParsecError err1, final AbstractParsecError err2) {
    super(false, ind, pred, mergeObj(err1.getException(), err2.getException()));
    this.err1 = err1;
    this.err2 = err2;
  }
  static Object mergeObj(final Object a, final Object b){
    return a==null?b:a;
  }
}
