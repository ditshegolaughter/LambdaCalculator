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

import java.io.PrintWriter;

import jfun.parsec.trace.Trace;
import jfun.parsec.trace.Traces;


/**
 * <p>
 * A parser runs either on character level or token level.
 * It takes as input a CharSequence object or a Tok[] array,
 * recognizes certain patterns and returns a value.
 * </p>
 * <p>
 * <ul>
 * <li>A character level parser object that simply recognizes input but not returning token is called a scanner.</li>
 * <li>A character level parser object that recognizes input and returns a token object is called a lexer.</li>
 * <li>A token level parser is called a parser. </li>
 * <li>a parser object can fail, or "return" a value via the retn() function.</li>
 * <li>a parser object can throw pseudo-exception object for other parsers to catch.</li>
 * <li>a parser object represents a parsing computation algorithm, not the actual parsing process.</li>
 * <li>a parser object is executed by Parsers.runParser() function.</li>
 * </ul>
 * </p>
 * @author Ben Yu
 *
 * 2004-11-11
 */
public abstract class Parser<Type> implements java.io.Serializable{
  private final ParsingFrame getErrorFrame(ParseContext ctxt, int ind){
    return new ParsingFrame(ctxt.getModuleName(),
        ctxt.getIndex(),
        ctxt.getPositionMap().getPos(ind), this);
  }
  private RuntimeException wrapException(Throwable e, ParseContext ctxt,
      int ind){
    if(e instanceof UserException){
      return (UserException)e;
    }
    else if(e instanceof ParserException){
      final ParserException pe = ((ParserException)e);
      pe.pushFrame(getErrorFrame(ctxt, ind));
      return pe;
    }
    else{
      final ParserException pe = 
        new ParserException(e, null,
            ctxt.getModuleName(),
            ctxt.getPositionMap().getPos(ind));
      return pe;
    }
  }
  final boolean parse(final ParseContext ctxt){
    final int ind = ctxt.getIndex();
    try{
      return apply(ctxt);
    }
    catch(RuntimeException e){
      throw wrapException(e, ctxt, ind);
    }
  }
  abstract boolean apply(final ParseContext ctxt);
  boolean apply(final ParseContext ctxt, final int look_ahead){
    return apply(ctxt);
  }
  
  final boolean parse(final ParseContext ctxt, final int look_ahead){
    final int ind = ctxt.getIndex();
    try{
      return apply(ctxt, look_ahead);
    }
    catch(RuntimeException e){
      throw wrapException(e, ctxt, ind);
    }
  }
  /*{
    throw new IllegalStateException("lookahead not in effect.");
  }*/
  private final String name;
  Parser(final String n){
    this.name = n;
  }
  final String getName(){return name;}
  public String toString(){return name;}
  /**
   * Create a Parser object that traces the parsing result of this parser.
   * Only effective when {@link Parsers#isDebugEnabled()} returns true.
   * @param trc the Trace object.
   * @return the new Parser object.
   * @since version 1.1
   */
  public final Parser<Type> trace(Trace<? super Type> trc){
    if(!Parsers.isDebugEnabled()) return this;
    return new TracedParser(name, this, trc);
  }
  /**
   * Create a Parser object that traces the parsing error of this parser when it fails.
   * Only effective when {@link Parsers#isDebugEnabled()} returns true.
   * @param id the identifier of the parser object in the trace message.
   * @param min_steps the minimal logical steps consumed to trigger the trace message.
   * @return the new Parser object.
   * @since version 1.1
   */
  public final Parser<Type> printError(String id, final int min_steps){
    return trace(Traces.printError(id, new PrintWriter(System.out), min_steps));
  }
  /**
   * Create a Parser object that traces the parsing error of this parser when it fails.
   * Only effective when {@link Parsers#isDebugEnabled()} returns true.
   * The trace message is triggered only when 1 or more logical steps are consumed.
   * @param id the identifier of the parser object in the trace message.
   * @return the new Parser object.
   * @since version 1.1
   */
  public final Parser<Type> printError(String id){
    return printError(id, 1);
  }
  /**
   * Create a Parser object that traces the parsing result of this parser when it succeeds.
   * Only effective when {@link Parsers#isDebugEnabled()} returns true.
   * @param id the identifier of the parser object in the trace message.
   * @return the new Parser object.
   * @since version 1.1
   */
  public final Parser<Type> printResult(String id){
    return trace(Traces.printResult(id, new PrintWriter(System.out)));
  }
  /**
   * Create a Parser object that traces the parsing result of this parser when it terminates.
   * Only effective when {@link Parsers#isDebugEnabled()} returns true.
   * @param id the identifier of the parser object in the trace message.
   * @return the new Parser object.
   * @since version 1.1
   */
  public final Parser<Type> printTrace(String id){
    return trace(Traces.printTrace(id, new PrintWriter(System.out)));
  }
  /**
   * if this parser succeeds, the returned value gets passed on to tp.
   * The monadic bind (product) operation.
   * @param tp the next step.
   * @return the new Parser.
   */
  public final <To> Parser<To> bind(final ToParser<? super Type,To> tp){
    return Parsers.bind("bind", this, tp);
  }
  /**
   * if this parser succeeds, the returned value gets passed on to tp.
   * The monadic bind (product or >>=) operation. 
   * @param name the name of the new parser.
   * @param tp the next step.
   * @return the new Parser.
   */
  public final <To> Parser<To> bind(final String name, 
      final ToParser<? super Type, To> tp){
    return Parsers.bind(name, this, tp);
  }
  /**
   * if this parser succeeds,
   * the returned value is discarded and the next parser is excuted.
   * The monadic seq (>>) operation. 
   * @param p the next parser.
   * @return the new Parser.
   */
  public final <R> Parser<R> seq(final Parser<R> p){
    return Parsers.seq("seq", this, p);
  }
  /**
   * if this parser succeeds,
   * the returned value is discarded and the next parser is excuted.
   * The monadic seq (>>) operation. 
   * @param name the name of the new parser.
   * @param p the next parser.
   * @return the new Parser.
   */
  public final <R> Parser<R> seq(final String name, final Parser<R> p){
    return Parsers.seq(name, this, p);
  }
  
  /**
   * Run Parser 'this' for n times. 
   * The return values are discarded.
   * @param n the number of times to run.
   * @return the new Parser object.
   */
  public final Parser<_> repeat(final int n){
    return Parsers.repeat("repeat", n, this);
  }
  /**
   * Run Parser 'this' for n times, collect the return values in an array
   * whose element type is etype.
   * @param etype the array element type.
   * @param n the number of times to run.
   * @return the new Parser object.
   */
  public final Parser<Type[]> repeat(final Class<Type> etype, final int n){
    return Parsers.repeat("repeat", etype, n, this);
  }
  /**
   * Run Parser 'this' for n times, collect the return values in an array
   * created by the ArrayFactory object.
   * @param af the ArrayFactory object.
   * @param n the number of times to run.
   * @return the new Parser object.
   */
  public final Parser<Type[]> repeat(final ArrayFactory<Type> af, final int n){
    return Parsers.repeat("repeat", af, n, this);
  }

  /**
   * Run Parser 'this' for n times.
   * The return values are discarded.
   * @param name the name of the new Parser object.
   * @param n the number of times to run.
   * @return the new Parser object.
   */
  public final Parser<_> repeat(final String name, final int n){
    return Parsers.repeat(name, n, this);
  }
  /**
   * Run Parser 'this' for n times, collect the return values in an array
   * whose element type is etype.
   * @param name the name of the new Parser object.
   * @param etype the array element type.
   * @param n the number of times to run.
   * @return the new Parser object.
   */
  public final Parser<Type[]> repeat(final String name, final Class<Type> etype, final int n){
    return Parsers.repeat(name, etype, n, this);
  }
  /**
   * Run Parser 'this' for n times, collect the return values in an array
   * created by the ArrayFactory object.
   * @param name the name of the new Parser object.
   * @param af the ArrayFactory object.
   * @param n the number of times to run.
   * @return the new Parser object.
   */
  public final Parser<Type[]> repeat(final String name, final ArrayFactory<Type> af, final int n){
    return Parsers.repeat(name, af, n, this);
  }
  /**
   * p.many(af) is equivalent to p* in EBNF.
   * The return values are collected and returned in an array
   * created by the ArrayFactory object.
   * @param af the ArrayFactory.
   * @return the new Parser.
   */
  public final Parser<Type[]> many(final ArrayFactory<Type> af){
    return Parsers.many("many", af, this);
  }
  /**
   * p.many(name, af) is equivalent to p* in EBNF.
   * The return values are collected and returned in an array created by the ArrayFactory object.
   * @param name the name of the new parser.
   * @param af the ArrayFactory.
   * @return the new Parser.
   */
  public final Parser<Type[]> many(final String name, final ArrayFactory<Type> af){
    return Parsers.many(name, af, this);
  }
  /**
   * p.many(elem_type) is equivalent to p* in EBNF.
   * The return values are collected and returned in an array.
   * @param elem_type the element type of the result array.
   * @return the new Parser.
   */
  public final Parser<Type[]> many(final Class<Type> elem_type){
    return Parsers.many("many", elem_type, this);
  }
  /**
   * p.many(name, elem_type) is equivalent to p* in EBNF.
   * The return values are collected and returned in an array.
   * @param name the name of the new parser.
   * @param elem_type the element type of the result array.
   * @return the new Parser.
   */
  public final Parser<Type[]> many(final String name, final Class<Type> elem_type){
    return Parsers.many(name, elem_type, this);
  }
  /**
   * p.many() is equivalent to p* in EBNF.
   * The return values are discarded.
   * @return the new Parser.
   */
  public final Parser<_> many(){
    return Parsers.many("many", this);
  }
  /**
   * p.many(name) is equivalent to p* in EBNF.
   * The return values are discarded.
   * @param name the name of the new parser.
   * @return the new Parser.
   */
  public final Parser<_> many(final String name){
    return Parsers.many(name, this);
  }

  

  
  
  /**
   * Runs this parser greedily for at least min times.
   * The return values are collected and returned in an array created by the ArrayFactory object.
   * @param af the ArrayFactory.
   * @param min the minimal number of times to run this parser.
   * @return the new Parser.
   */
  public final Parser<Type[]> many(final ArrayFactory<Type> af, final int min){
    return Parsers.many("many", af, min, this);
  }
  /**
   * Runs this parser greedily for at least min times.
   * The return values are collected and returned in an array created by the ArrayFactory object.
   * @param name the name of the new parser.
   * @param af the ArrayFactory.
   * @param min the minimal number of times to run this parser.
   * @return the new Parser.
   */
  public final Parser<Type[]> many(final String name, final ArrayFactory<Type> af, final int min){
    return Parsers.many(name, af, min, this);
  }
  /**
   * Runs this parser greedily for at least min times.
   * The return values are collected and returned in an array.
   * @param elem_type the element type of the result array.
   * @param min the minimal number of times to run this parser.
   * @return the new Parser.
   */
  public final Parser<Type[]> many(final Class<Type> elem_type, final int min){
    return Parsers.many("many", elem_type, min, this);
  }
  /**
   * Runs this parser greedily for at least min times.
   * The return values are collected and returned in an array.
   * @param name the name of the new parser.
   * @param elem_type the element type of the result array.
   * @param min the minimal number of times to run this parser.
   * @return the new Parser.
   */
  public final Parser<Type[]> many(final String name, final Class<Type> elem_type, final int min){
    return Parsers.many(name, elem_type, min, this);
  }
  /**
   * Runs this parser greedily for at least min times.
   * The return values are discarded.
   * @return the new Parser.
   */
  public final Parser<_> many(final int min){
    return Parsers.many("many", min, this);
  }
  /**
   * Runs this parser greedily for at least min times.
   * The return values are discarded.
   * @param name the name of the new parser.
   * @param min the minimal number of times to run this parser.
   * @return the new Parser.
   */
  public final Parser<_> many(final String name, final int min){
    return Parsers.many(name, min, this);
  }

  
  
  
  
  /**
   * Runs this for at least min times and at most max times.
   * The return values are collected and returned in an array created by the ArrayFactory object.
   * @param af the ArrayFactory.
   * @param min the minimal number of times to run this parser.
   * @param max the maximal number of times to run this parser.
   * @return the new Parser.
   */
  public final Parser<Type[]> some(final ArrayFactory<Type> af, final int min, final int max){
    return Parsers.some("some", af, min, max, this);
  }
  /**
   * Runs this for at least min times and at most max times.
   * The return values are collected and returned in an array
   * created by the ArrayFactory object.
   * @param name the name of the new parser.
   * @param af the ArrayFactory.
   * @param min the minimal number of times to run this parser.
   * @param max the maximal number of times to run this parser.
   * @return the new Parser.
   */
  public final Parser<Type[]> some(final String name, final ArrayFactory<Type> af, final int min, final int max){
    return Parsers.some(name, af, min, max, this);
  }
  /**
   * Runs this for at least min times and at most max times.
   * The return values are collected and returned in an array
   * whose element type is elem_type.
   * @param elem_type the element type of the result array.
   * @param min the minimal number of times to run this parser.
   * @param max the maximal number of times to run this parser.
   * @return the new Parser.
   */
  public final Parser<Type[]> some(final Class<Type> elem_type, final int min, final int max){
    return Parsers.some("some", elem_type, min, max, this);
  }
  /**
   * Runs this for at least min times and at most max times.
   * The return values are collected and returned in an array
   * whose element type is elem_type.
   * @param elem_type the element type of the result array.
   * @param min the minimal number of times to run this parser.
   * @param max the maximal number of times to run this parser.
   * @return the new Parser.
   */
  public final Parser<Type[]> some(final String name, final Class<Type> elem_type, final int min, final int max){
    return Parsers.some(name, elem_type, min, max, this);
  }
  /**
   * Runs this for at least min times and at most max times.
   * The return values are discarded.
   * @param min the minimal number of times to run this parser.
   * @param max the maximal number of times to run this parser.
   * @return the new Parser.
   */
  public final Parser<_> some(final int min, final int max){
    return Parsers.some("some", min, max, this);
  }
  /**
   * Runs this for at least min times and at most max times.
   * The return values are discarded.
   * @param name the name of the new parser.
   * @param min the minimal number of times to run this parser.
   * @param max the maximal number of times to run this parser.
   * @return the new Parser.
   */
  public final Parser<_> some(final String name, final int min, final int max){
    return Parsers.some(name, min, max, this);
  }
  
  /**
   * Runs this for up to max times.
   * The return values are discarded.
   * @param max the maximal number of times to run.
   * @return the new Parser.
   */
  public final Parser<_> some(final int max){
    return Parsers.some("some", max, this);
  }
  /**
   * Runs this for up to max times.
   * The return values are collected and returned in an array
   * whose element type is etype.
   * @param etype the element type of the result array.
   * @param max the maximal number times to run.
   * @return the new Parser.
   */
  public final Parser<Type[]> some(final Class<Type> etype, final int max){
    return Parsers.some("some", etype, max, this);
  }
  /**
   * Runs this for up to max times.
   * The return values are collected and returned in an array
   * created by af.
   * @param max the maximal number of times to run.
   * @return the new Parser.
   */
  public final Parser<Type[]> some(final ArrayFactory<Type> af, final int max){
    return Parsers.some("some", af, max, this);
  }
  /**
   * Runs this for up to max times.
   * The return values are discarded.
   * @param name the name of the new parser.
   * @param max the maximal number times to run.
   * @return the new Parser.
   */
  public final Parser<_> some(final String name, final int max){
    return Parsers.some(name, max, this);
  }
  /**
   * Runs this for up to max times.
   * The return values are collected and returned in an array
   * whose element type is etype.
   * @param name the name of the new parser.
   * @param etype the element type of the result array.
   * @param max the maximal number of times to run.
   * @return the new Parser.
   */
  public final Parser<Type[]> some(final String name, final Class<Type> etype, final int max){
    return Parsers.some(name, etype, max, this);
  }
  /**
   * Runs this for up to max times.
   * The return values are collected and returned in an array
   * created by af.
   * @param name the name of the new parser.
   * @param af the ArrayFactory object.
   * @param max the maximal number of times to run.
   * @return the new Parser.
   */
  public final Parser<Type[]> some(final String name, final ArrayFactory<Type> af, final int max){
    return Parsers.some(name, af, max, this);
  }
  /**
   * p.many1(elem_type) is equivalent to p+ in EBNF.
   * The return values are collected and returned in an array
   * whose element type is elem_type.
   * @param elem_type the element type of the result array.
   * @return the new Parser.
   */
  public final Parser<Type[]> many1(final Class<Type> elem_type){
    return Parsers.many("many1", elem_type, 1, this);
  }
  /**
   * p.many1(name, elem_type) is equivalent to p+ in EBNF.
   * The return values are collected and returned in an array
   * whose element type is elem_type.
   * @param name the name of the new parser.
   * @param elem_type the element type of the result array.
   * @return the new Parser.
   */
  public final Parser<Type[]> many1(final String name, final Class<Type> elem_type){
    return Parsers.many(name, elem_type, 1, this);
  }
  /**
   * p.many1(af) is equivalent to p+ in EBNF.
   * The return values are collected and returned in an array
   * created by the ArrayFactory object.
   * @param af the ArrayFactory.
   * @return the new Parser.
   */
  public final Parser<Type[]> many1(final ArrayFactory<Type> af){
    return Parsers.many("many1", af, 1, this);
  }
  /**
   * p.many1(name, af) is equivalent to p+ in EBNF.
   * The return values are collected and returned in an array
   * created by the ArrayFactory object.
   * @param name the name of the new parser.
   * @param af the ArrayFactory.
   * @return the new Parser.
   */
  public final Parser<Type[]> many1(final String name, final ArrayFactory<Type> af){
    return Parsers.many(name, af, 1, this);
  }
  /**
   * p.many1() is equivalent to p+ in EBNF.
   * The return values are discarded.
   * @return the new Parser.
   */
  public final Parser<_> many1(){
    return Parsers.many("many1", 1, this);
  }
  /**
   * p.many1(name) is equivalent to p+ in EBNF.
   * The return values are discarded.
   * @param name the name of the new parser.
   * @return the new Parser.
   */
  public final Parser<_> many1(final String name){
    return Parsers.many(name, 1, this);
  }

  /**
   * p.optional() is equivalent to p? in EBNF. null is the result when p fails.
   * @return the new Parser.
   */
  public final Parser<Type> optional(){
    return Parsers.optional("optional", this);
  }
  /**
   * p.optional(name) is equivalent to p? in EBNF. null is the result when p fails.
   * @param name the name of the new Parser.
   * @return the new Parser.
   */
  public final Parser<Type> optional(final String name){
    return Parsers.optional(name, this);
  }
  /**
   * If this fails with no input consumed, the default value is returned.
   * p.option(name, def) = p | retn(def).
   * @param def the default value.
   * @return the new Parser.
   */
  public final Parser<Type> option(final Type def){
    return Parsers.option("option", def, this);
  }
  /**
   * If this fails with no input consumed, the default value is returned.
   * p.option(name, def) = p | retn(def).
   * @param name the name of the new Parser.
   * @param def the default value.
   * @return the new Parser.
   */
  public final Parser<Type> option(final String name, final Type def){
    return Parsers.option(name, def, this);
  }
  /**
   * fails if 'this' succeeds. Input consumption is undone.
   * @return the new Parser.
   */
  public final Parser<?> not(){
    return Parsers.not("not", this);
  }
  /**
   * fails if 'this' succeeds. Input consumption is undone.
   * @param err the error message if fails.
   * @return the new Parser.
   */
  public final Parser<?> not(final String err){
    return Parsers.not("not", this, err);
  }
  /**
   * fails if 'this' succeeds. Input consumption is undone.
   * @param name the name of the new Parser.
   * @param err the error message if fails.
   * @return the new Parser.
   */
  public final Parser<?> not(final String name, final String err){
    return Parsers.not(name, this, err);
  }
  /**
   * this is a look-ahead operation.
   * Succeed or not, the input consumption is undone.
   * @return the new Parser.
   */
  public final Parser<Type> peek(){
    return Parsers.peek("peek", this);
  }
  /**
   * this is a look-ahead operation.
   * Succeed or not, the input consumption is undone.
   * @param name the name of the new Parser
   * @return the new Parser
   */
  public final Parser<Type> peek(final String name){
    return Parsers.peek(name, this);
  }
  /**
   * if this succeeds, the returned value is transformed with m to a new return value.
   * @param m the map object to transform return value.
   * @return the new Parser.
   */
  public final <R> Parser<R> map(final Map<? super Type, R> m){
    return Parsers.map("map", this, m);
  }
  /**
   * if this succeeds, the returned value is transformed with m to a new return value.
   * @param name the name of the new Parser.
   * @param m the map object to transform return value.
   * @return the new Parser.
   */
  public final <R> Parser<R> map(final String name, final Map<? super Type, R> m){
    return Parsers.map(name, this, m);
  }
  /**
   * it sequentially run this and p, and then transforms the two return values with m to a new return value.
   * @param p the next parser to run.
   * @param m the transformation.
   * @return the new Parser object.
   */
  public final <T, R> Parser<R> and(final Parser<T> p, final Map2<? super Type, ? super T, R> m){
    return Parsers.map2("and", this, p, m);
  }
  /**
   * it sequentially run this and p, and then transforms the two return values with m to a new return value.
   * @param name the name of the new parser.
   * @param p the next parser to run.
   * @param m the transformation.
   * @return the new Parser object.
   */
  public final <T, R> Parser<R> and(final String name, final Parser<T> p, final Map2<? super Type, ? super T, R> m){
    return Parsers.map2(name, this, p, m);
  }
  /**
   * 'this' and 'sep' are executed sequentially.
   * The return value of 'this' is returned.
   * @param sep the following parser.
   * @return the new Parser.
   */
  public final Parser<Type> followedBy(final Parser<?> sep){
    return Parsers.followedBy("followedBy", sep, this);
  }
  /**
   * 'this' and 'sep' are executed sequentially.
   * The return value of 'this' is returned.
   * @param name the name of the new Parser.
   * @param sep the following parser.
   * @return the new Parser.
   */
  public final Parser<Type> followedBy(final String name, final Parser<?> sep){
    return Parsers.followedBy(name, sep, this);
  }
  /**
   * Make sure 'this' is atomic. When fails, no input is consumed.
   * For lookahead, a successful atomized operation is considered 
   * at most one logical step.
   * @param name the name of the new Parser.
   * @return the new Parser.
   */
  public final Parser<Type> atomize(final String name){
    return Parsers.atomize(name, this);
  }
  /**
   * Make sure 'this' is atomic. When fails, no input is consumed.
   * For lookahead, a successful atomized operation is considered 
   * at most one logical step.
   * @return the new Parser.
   */
  public final Parser<Type> atomize(){
    return Parsers.atomize("atomize", this);
  }
  /**
   * run yes if this succeeds, no if this fails without consuming input;
   * fails otherwise.
   * @param yes the true branch.
   * @param no the false branch.
   * @return the new Parser object.
   */
  public final <R> Parser ifelse(final Parser<R> yes, final Parser<? extends R> no){
    return Parsers.ifelse("ifelse", this, yes, no);
  }
  /**
   * run yes if this succeeds, no if this fails without consuming input;
   * fails otherwise.
   * @param name the name of the new Parser object.
   * @param yes the true branch.
   * @param no the false branch.
   * @return the new Parser object.
   */
  public final <R> Parser<R> ifelse(final String name, final Parser<R> yes, final Parser<R> no){
    return Parsers.ifelse(name, this, yes, no);
  }

  /**
   * run yes if this succeeds, no if this fails without consuming input;
   * fails otherwise.
   * @param yes the true branch.
   * @param no the false branch.
   * @return the new Parser object.
   */
  public final <R> Parser<R> ifelse(final ToParser<? super Type, R> yes, final Parser<R> no){
    return Parsers.ifelse("ifelse", this, yes, no);
  }
  /**
   * run yes if this succeeds, no if this fails without consuming input;
   * fails otherwise.
   * @param name the name of the new Parser object.
   * @param yes the true branch.
   * @param no the false branch.
   * @return the new Parser object.
   */
  public final <R> Parser<R> ifelse(final String name, final ToParser<? super Type, R> yes, final Parser<R> no){
    return Parsers.ifelse(name, this, yes, no);
  }
  /**
   * By default, ifelse, plus, sum will not try to run the next branch if the previous branch failed
   * and consumed some input.
   * this is because the default look-ahead token is 1.
   * <br> by using lookahead, this default behavior can be altered.
   * Parsers.plus(p1, p2).lookahead(3)
   * will still try p2 even if p1 fails and consumes one or two inputs.
   * <p> lookahead only affects one nesting level.
   * Parsers.plus(p1,p2).ifelse(yes,no).lookahead(3)
   * will not affect the Parsers.plus(p1,p2) nested within ifelse.
   * <p> lookahead directly on top of lookahead will override the previous lookahead.
   * Parsers.plus(p1,p2).lookahead(3).lookahead(1)
   * is equivalent as Parsers.plus(p1, p2).lookahead(1).
   * <br>
   * lookahead looks at logical step.
   * by default, each terminal is one logical step.
   * atomize() combinator ensures at most 1 logical step for a parser.
   * Use step() combinator to fine control logical steps.
   * @param name the name of the new Parser object.
   * @param toknum the number of tokens to look ahead.
   * @return the new Parser object.
   */
  public final Parser<Type> lookahead(final String name, final int toknum){
    return Parsers.lookahead(name, toknum, this);
  }
  /**
   * By default, ifelse, plus, sum will not try to run the next branch if the previous branch failed
   * and consumed some input.
   * this is because the default look-ahead token is 1.
   * <br> by using lookahead, this default behavior can be altered.
   * Parsers.plus(p1, p2).lookahead(3)
   * will still try p2 even if p1 fails and consumes one or two inputs.
   * <p> lookahead only affects one nesting level.
   * Parsers.plus(p1,p2).ifelse(yes,no).lookahead(3)
   * will not affect the Parsers.plus(p1,p2) nested within ifelse.
   * <p> lookahead directly on top of lookahead will override the previous lookahead.
   * Parsers.plus(p1,p2).lookahead(3).lookahead(1)
   * is equivalent as Parsers.plus(p1, p2).lookahead(1).
   * <br>
   * lookahead looks at logical step.
   * by default, each terminal is one logical step.
   * atomize() combinator ensures at most 1 logical step for a parser.
   * Use step() combinator to fine control logical steps.
   * @param toknum the number of tokens to look ahead.
   * @return the new Parser object.
   */
  public final Parser<Type> lookahead(final int toknum){
    return Parsers.lookahead("lookahead", toknum, this);
  }
  /**
   * if fails and did not consume input, 
   * reports an expecting error with the given label.
   * @param lbl the label text.
   * @return the new Parser object.
   */
  public final Parser<Type> label(final String lbl){
    return Parsers.label("label", lbl, this);
  }
  /**
   * if fails and did not consume input,
   * reports an expecting error with the given label.
   * @param name the name of the new Parser object.
   * @param lbl the label text.
   * @return the new Parser object.
   */
  public final Parser<Type> label(final String name, final String lbl){
    return Parsers.label(name, lbl, this);
  }
  
  /**
   * Fails if the return value of this parser does not satisify the given predicate.
   * No-op otherwise.
   * @param name the name of the new Parser object.
   * @param op the predicate object.
   * @return the new Parser object.
   */
  public final Parser<Type> isReturn(final String name, final ObjectPredicate<? super Type> op){
    return Parsers.isReturn(name, this, op);
  }
  /**
   * Fails if the return value of this parser does not satisify the given predicate.
   * No-op otherwise.
   * @param op the predicate object.
   * @return the new Parser object.
   */
  public final Parser<Type> isReturn(final ObjectPredicate<? super Type> op){
    return Parsers.isReturn("isReturn", this, op);
  }
  /**
   * Fails if the return value of this parser does not satisify the given predicate.
   * No-op otherwise.
   * When fails, an "expecting" error will be generated.
   * @param name the name of the new Parser object.
   * @param op the predicate object.
   * @param expecting the expected string.
   * @return the new Parser object.
   */
  public final Parser<Type> isReturn(final String name, final ObjectPredicate<? super Type> op, final String expecting){
    return Parsers.isReturn(name, this, op, expecting);
  }
  /**
   * Fails if the return value of this parser does not satisify the given predicate.
   * No-op otherwise.
   * When fails, an "expecting" error will be generated.
   * @param op the predicate object.
   * @param expecting the expected string.
   * @return the new Parser object.
   */
  public final Parser<Type> isReturn(final ObjectPredicate<? super Type> op, final String expecting){
    return Parsers.isReturn("isReturn", this, op, expecting);
  }
  /**
   * lookahead looks at logical steps.
   * step(int) runs this parser and sets the number of logical steps.
   * 
   * @param name the name of the new Parser object.
   * @param n the number logical steps. n>=0 has to be true.
   * @return the new Parser object.
   */
  public final Parser<Type> step(String name, int n){
    return Parsers.step(name, n, this);
  }
  /**
   * lookahead looks at logical steps.
   * step() runs this parser and sets 1 logical step.
   * 
   * @param name the name of the new Parser object.
   * @return the new Parser object.
   */
  public final Parser<Type> step(final String name){
    return Parsers.step(name, 1, this);
  }
  /**
   * lookahead looks at logical steps.
   * step(int) runs this parser and sets the number of logical steps.
   * 
   * @param n the number logical steps. n>=0 has to be true.
   * @return the new Parser object.
   */
  public final Parser<Type> step(int n){
    return Parsers.step("step", n, this);
  }
  /**
   * lookahead looks at logical steps.
   * step() runs this parser and sets 1 logical step.
   * 
   * @return the new Parser object.
   */
  public final Parser<Type> step(){
    return Parsers.step("step", 1, this);
  }
  
  /**
   * To cast the current Parser to a Parser object that returns a subtype of the current type.
   * @param <R> the target return type.
   * @return the Parser object that returns the expected type.
   * @since version 1.0
   */
  public final <R extends Type> Parser<R> cast(){
    return (Parser<R>)this;
  }
  /**
   * To cast the current Parser to a Parser object that returns a subtype of the current type.
   * @param <R> the target return type.
   * @param type the class literal for the target type.
   * @return the Parser object that returns the expected type.
   * @since version 1.0
   */
  public final <R extends Type> Parser<R> cast(Class<R> type){
    return cast();
  }
  /**
   * To convert the current Parser to a Parser object that returns any target type.
   * @param <R> the target return type.
   * @return the Parser object that returns the expected type.
   * @since version 1.0
   */
  public final <R> Parser<R> convert(){
    return (Parser<R>)this;
  }
  /**
   * To convert the current Parser to a Parser object that returns any target type.
   * @param <R> the target return type.
   * @param type the class literal for the target type.
   * @return the Parser object that returns the expected type.
   * @since version 1.0
   */
  public final <R> Parser<R> convert(Class<R> type){
    return convert();
  }
  Type getReturn(ParseContext ctxt){
    return (Type)ctxt.getReturn();
  }
  /**
   * To parse a source string. 
   * @param source the source string.
   * @param moduleName the name of the module, this name appears in error message.
   * @return the result.
   * @since v1.2
   */
  public final Type parse(CharSequence source, String moduleName) {
    return Parsers.runParser(source, this, moduleName);
  }
  /**
   * To parse a source string. 
   * @param source the source string.
   * @return the result.
   * @since v1.2
   */
  public final Type parse(CharSequence source) {
    return parse(source, "source");
  }
}
