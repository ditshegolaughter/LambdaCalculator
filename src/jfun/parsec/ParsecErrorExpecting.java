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
 * Represent an "expecting" error.
 * It works with MergedParsecError to implement laziness.
 * Zephyr Business Solutions Corp.
 *
 * @author Ben Yu
 *
 */
final class ParsecErrorExpecting extends AbstractParsecError {
  ParsecError render() {
    return err.render().setExpecting(s).render();
  }

  AbstractParsecError setExpecting(String s) {
    return new ParsecErrorExpecting(noMerge(), getIndex(), getPrecedence(),
        getException(), err, s);
  }
  private final AbstractParsecError err;
  private final String s;

  ParsecErrorExpecting(boolean nomerge, int at, int pred, Object exception,
      final AbstractParsecError err, final String s) {
    super(nomerge, at, pred, exception);
    this.err = err;
    this.s = s;
  }
}
