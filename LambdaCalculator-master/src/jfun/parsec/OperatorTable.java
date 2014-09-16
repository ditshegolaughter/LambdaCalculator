/*****************************************************************************
 * Copyright (C) Zephyr Business Solutions Corp. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/
/*
 * Created on Nov 21, 2004
 *
 * Author Ben Yu
 */
package jfun.parsec;
import java.util.ArrayList;
import java.util.Collections;
/**
 * This class is used to describe operator information.
 * Operators have precedences. the higher the precedence number, the higher the precedence.
 * For the same precedence, prefix > postfix > left infix > right infix > non-associative infix.
 * @author Ben Yu
 *
 * Nov 21, 2004
 */
public final class OperatorTable<E> implements java.io.Serializable{
  static final int PREFIX = 0;
  static final int POSTFIX = 1;
  static final int LASSOC = 2;
  static final int RASSOC = 3;
  static final int NASSOC = 4;
  private final ArrayList ops = new ArrayList();
  static final class Operator implements Comparable<Operator>{
    private final Parser op;
    private final int prec;
    private final int kind;
    Operator(final Parser op, final int prec, final int k){
      this.op = op;
      this.prec = prec;
      this.kind = k;
    }
    Parser getOp(){return op;}
    int getPrecedence(){return prec;}
    int getKind(){return kind;}
    public int compareTo(final Operator other){
      final Operator op2 = (Operator)other;
      if(prec > op2.prec) return -1;
      else if(prec < op2.prec) return 1;
      else if(kind < op2.kind) return -1;
      else if(kind > op2.kind) return 1;
      return 0;
    }
  }
  /**
   * Adds a prefix unary operator.
   * @param p the parser for the operator.
   * @param precedence the precedence number.
   * @return this.
   */
  public OperatorTable<E> prefix(final Parser<? extends Map<? super E, ? extends E>> p, final int precedence){
    ops.add(new Operator(p,precedence, PREFIX));
    return this;
  }
  /**
   * Adds a postfix unary operator.
   * @param p the parser for the operator.
   * @param precedence the precedence number.
   * @return this.
   */
  public OperatorTable<E> postfix(final Parser<? extends Map<? super E, ? extends E>> p, final int precedence){
    ops.add(new Operator(p,precedence, POSTFIX));
    return this;
  }
  /**
   * Adds a infix left-associative binary operator.
   * @param p the parser for the operator.
   * @param precedence the precedence number.
   * @return this.
   */
  public OperatorTable<E> infixl(final Parser<? extends Map2<? super E, ? super E, ? extends E>> p, 
      final int precedence){
    ops.add(new Operator(p,precedence, LASSOC));
    return this;
  }
  /**
   * Adds a infix right-associative binary operator.
   * @param p the parser for the operator.
   * @param precedence the precedence number.
   * @return this.
   */
  public OperatorTable<E> infixr(final Parser<? extends Map2<? super E, ? super E, ? extends E>> p, 
      final int precedence){
    ops.add(new Operator(p,precedence, RASSOC));
    return this;
  }
  /**
   * Adds a infix non-associative binary operator.
   * @param p the parser for the operator.
   * @param precedence the precedence number.
   * @return this.
   */
  public OperatorTable<E> infixn(final Parser<? extends Map2<? super E, ? super E, ? extends E>> p, 
      final int precedence){
    ops.add(new Operator(p,precedence, NASSOC));
    return this;
  }
  Operator[] getOperators(){
    Collections.sort(ops);
    final Operator[] ret = new Operator[ops.size()];
    ops.toArray(ret);
    return ret;
  }
}
