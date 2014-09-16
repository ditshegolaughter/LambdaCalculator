/*****************************************************************************
 * Copyright (C) Zephyr Business Solutions Corp. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/
/*
 * Created on Nov 19, 2004
 *
 * Author Ben Yu
 */
package jfun.parsec;

/**
 * Expressions provides helper functions to build parser for a operator-precedence expression grammar. <br>
 * It supports prefix unary, postfix unary, infix left associative binary,
 * infix right associative binary and infix non-associative binary operators.
 * <br>
 * @author Ben Yu
 *
 * Nov 19, 2004
 */
public final class Expressions {
  /**
   * Creates a Parser object based on information described by OperatorTable.
   * @param term parser for the terminals.
   * @param table the operator table.
   * @return the expression parser.
   */
  public static <E,T extends E> Parser<E> buildExpressionParser(final Parser<T> term, final OperatorTable<E> table){
    return buildExpressionParser("expression", term, table);
  }
  /**
   * Creates a Parser object based on information described by OperatorTable.
   * @param name the name of the parser.
   * @param term parser for the terminals.
   * @param table the operator table.
   * @return the expression parser.
   */
  public static <E,T extends E> Parser<E> buildExpressionParser(final String name, 
      final Parser<T> term, final OperatorTable<E> table){
    final OperatorTable.Operator[] ops = table.getOperators();
    if(ops.length == 0) return term.convert();//Parsers.subsume(term);
    int begin = 0;
    int prec = ops[0].getPrecedence();
    int kind = ops[0].getKind();
    int end = 0;
    Parser ret = term;
    for(int i=1; i<ops.length; i++){
      final OperatorTable.Operator op = ops[i];
      end = i;
      if(op.getPrecedence() == prec && op.getKind() == kind){
        continue;
      }
      else{
        end = i;
        final Parser p = slice(name, ops, begin, end);
        ret = build(name, p, kind, ret);
        begin = i;
        prec = ops[i].getPrecedence();
        kind = ops[i].getKind();
      }
    }
    if(end != ops.length){
      end = ops.length;
      kind = ops[begin].getKind();
      final Parser p = slice(name, ops, begin, end);
      ret = build(name, p, kind, ret);
    }
    return ret;
  }
  private static Parser slice(final String name, final OperatorTable.Operator[] ops, final int begin, final int end){
    final Parser[] ps = new Parser[end-begin];
    for(int i=0; i<ps.length; i++){
      ps[i] = ops[i+begin].getOp();
    }
    return Parsers.sum(name, ps);
  }
  private static <E, T extends E> Parser<E> build(final String name, 
      final Parser op, final int kind, final Parser<T> operand){
    switch(kind){
      case OperatorTable.PREFIX:
        return Parsers.prefix(name, op, operand);
      case OperatorTable.POSTFIX:
        return Parsers.postfix(name, op, operand);
      case OperatorTable.LASSOC:
        return Parsers.infixl(name, op, operand);
      case OperatorTable.RASSOC:
        return Parsers.infixr(name, op, operand);
      case OperatorTable.NASSOC:
        return Parsers.infixn(name, op, operand);
      default:
        return operand.convert();//Parsers.subsume(operand);
    }
  }
}
