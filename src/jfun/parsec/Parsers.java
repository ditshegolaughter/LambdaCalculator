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
 * <p>
 * This class provides general parser combinators that work on both character
 * level and token level.
 * </p>
 * <p>
 * A parser can work on character level or token level.
 * <ul>
 * <li>A character level parser that returns a token object is called a lexer.
 * </li>
 * <li>A character level parser that does not return a token object is called a
 * scanner. </li>
 * </ul>
 * A token level parser is called a "parser".
 * </p>
 * <p>
 * For each Parser combinator, we write the signature in a haskellish sytax.
 * <ul>
 * <li>A parser that returns an object of type a is denoted as: Parser a. </li>
 * <li>A value of any type is denoted as * (null is an example, exceptions,
 * errors are all type *). We use the lower case "x", "y", "z" variables as type parameter for such cases.</li>
 * <li>A no-value type is denoted as _. </li>
 * <li>An unknown type is denoted as ?. </li>
 * <li>A parser transformer that maps a Parser a to Parser b is denoted as:
 * Parser a -> Parser b. </li>
 * <li>A parser combinator that accepts object of type a and returns a Parser b
 * is denoted as: a -> Parser b. </li>
 * <li>Interface Map is denoted as a -> b. this is because Map will be changed
 * to Map<A,B> once we have generics. </li>
 * <li>Map2 is denoted as a->b->r, Map3 is a->b->c->r, etc. </li>
 * <li>ToParser is denoted as a -> Parser b. </li>
 * </ul>
 * </p>
 * <p>
 * A Parser object is safely covariant about the type parameter. Jparsec
 * tries its best to support covariance in the API since java 5 generics has no covariance support.
 * It is always safe to use the convert() method to explicitly force covariance whenever necessary.
 * </p>
 * <p>
 * A parser has a user state that can be set and retrieved. Only setState,
 * getState, transformState combinators are user state related.
 * </p>
 * <p>
 * We denote a user state concerned parser as Parser u a, where u is the user
 * state type and a is the return type.
 * </p>
 * <p>
 * An array of element type t, is denoted as [t].
 * </p>
 * 
 * @author Ben Yu
 * 
 * 2004-11-11
 */
public final class Parsers {
  private static boolean debugged = true;
  /**
   * Is debugging enabled?
   * @since version 1.1
   */
  public synchronized static boolean isDebugEnabled() {
    return debugged;
  }
  /**
   * enable or disable debugging.
   * @param debugged true if debugging is enabled, false otherwise.
   * @since version 1.1
   */
  public synchronized static void setDebug(boolean debugged) {
    Parsers.debugged = debugged;
  }
  /**
   * Runs a character level parser with a CharSequence input.
   * 
   * @param src
   *          the input source.
   * @param p
   *          the parser object to run.
   * @param pmap
   *          the PositionMap object used to map a character index to
   *          line/column number.
   * @param module
   *          the module name. Use any name that's meaningful.
   * @return the result object.
   */
  public static <R> R runParser(final CharSequence src, final Parser<R> p,
      final PositionMap pmap, final String module) {
    final ScannerState ctxt = new ScannerState(src, 0, module, pmap, null);
    return ParserInternals.runParser(ctxt, p, pmap);
  }

  /**
   * Runs a character level parser with a CharSequence input.
   * The {@link Parser#parse(CharSequence, String)} is equivalent
   * to this call and is more convenient.
   * @param src the input source.
   * @param p the parser object to run.
   * @param module the module name. This name apears in error message.
   * @return the result object.
   */
  public static <R> R runParser(final CharSequence src, final Parser<R> p,
      final String module) {
    return runParser(src, p, new DefaultPositionMap(src, 1, 1), module);
  }

  /**
   * Runs a token level Parser object with an array of tokens.
   * <p>
   * [Tok] -> int -> Parser a -> ShowToken -> String -> PositionMap ->
   * a
   * 
   * @param toks
   *          the input tokens
   * @param end_index
   *          the index after the last character in the source.
   * @param p
   *          the parser object.
   * @param show
   *          the object to show the tokens.
   * @param eof_title
   *          the name of "end of file".
   * @param module
   *          the module name. Use any name that's meaningful. This value will
   *          be shown in any EOF related messages.
   * @param pmap
   *          the PositionMap object to map a character index to the line/column
   *          number.
   * @return the return value of the Parser object. (returned by retn()
   *         function)
   * @throws ParserException
   *           when parsing fails.
   */
  public static <R> R runParser(final Tok[] toks,
      final int end_index, final Parser<R> p, final ShowToken show,
      final String eof_title, final PositionMap pmap, final String module)
      throws ParserException {
    final ParserState s0 = new ParserState(null, toks, 0, module, pmap,
        end_index, eof_title, show);
    return ParserInternals.runParser(s0, p, pmap);
  }

  /**
   * To create a Parser that always succeeds and causes a certain side effect
   * using a Runnable object.
   * 
   * @param name
   *          the parser name.
   * @param runnable
   *          the Runnable object.
   * @return the Parser object.
   */
  public static Parser<?> runnable(String name, Runnable runnable) {
    return new ActionParser(name, runnable);
  }

  /**
   * To create a Parser that always succeeds and causes a certain side effect
   * using a Runnable object.
   * 
   * @param runnable
   *          the Runnable object.
   * @return the Parser object.
   */
  public static Parser<?> runnable(Runnable runnable) {
    return runnable("runnable", runnable);
  }

  /**
   * The created parser object will take as input the array of Tok
   * returned from the lexer object, feed it into the Parser object p and run
   * it, return the result from parser p. <br>
   * It fails if the lexer fails or parser p fails. 
   * Parser [Tok] -> Parser a -> Parser a
   * 
   * @param lexer
   *          the lexer object that returns an array of Tok objects.
   * @param p
   *          the token level parser object.
   * @param module
   *          the module name. Use any name that's meaningful. that will parse
   *          the array of Tok objects.
   * @return the new Parser object.
   */
  public static <R> Parser<R> parseTokens(
      final Parser<Tok[]> lexer, final Parser<R> p,
      final String module) {
    return parseTokens("parseTokens", lexer, p, module);
  }

  /**
   * The created parser object will take as input the array of Tok
   * returned from the lexer object, feed it into the Parser object p and run
   * it, return the result from parser p. <br>
   * It fails if the lexer fails or parser p fails. 
   * Parser [Tok] -> Parser a -> Parser a
   * 
   * @param name
   *          the name of the new Parser object.
   * @param lexer
   *          the lexer object that returns an array of Tok objects.
   * @param p
   *          the token level parser object.
   * @param module
   *          the module name. Use any name that's meaningful. that will parse
   *          the array of Tok objects.
   * @return the new Parser object.
   */
  public static <R> Parser<R> parseTokens(final String name,
      final Parser<Tok[]> lexer, final Parser<R> p,
      final String module) {
    return parseTokens(name, "EOF", Token2String.instance(), lexer, p, module);
  }

  /**
   * The created parser object will take as input the array of Tok
   * returned from the lexer object, feed it into the Parser object p and run
   * it, return the result from parser p. <br>
   * It fails if the lexer fails or parser p fails. String -> ShowToken ->
   * Parser [Tok] -> Parser a -> Parser a
   * 
   * @param eof_title
   *          the name of "end of input"
   * @param show
   *          the object to transform a token to a string.
   * @param lexer
   *          the lexer object that returns an array of Tok objects.
   * @param p
   *          the token level parser object.
   * @param module
   *          the module name. Use any name that's meaningful. that will parse
   *          the array of Tok objects.
   * @return the new Parser object.
   */
  public static <R> Parser<R> parseTokens(final String eof_title,
      final ShowToken show,
      final Parser<Tok[]> lexer, final Parser<R> p, String module) {
    return parseTokens("parseTokens", eof_title, show, lexer, p, module);
  }

  /**
   * The created parser object will take as input the array of Tok
   * returned from the lexer object, feed it into the Parser object p and run
   * it, return the result from parser p. <br>
   * It fails if the lexer fails or parser p fails. String -> ShowToken ->
   * Parser [Tok] -> Parser a -> Parser a
   * 
   * @param name
   *          the name of the new Parser object.
   * @param eof_title
   *          the name of "end of input"
   * @param show
   *          the object to transform a token to a string.
   * @param lexer
   *          the lexer object that returns an array of Tok objects.
   * @param p
   *          the token level parser object
   * @param module
   *          the module name. Use any name that's meaningful. that will parse
   *          the array of Tok objects.
   * @return the new Parser object.
   */
  public static <R> Parser<R> parseTokens(final String name,
      final String eof_title, final ShowToken show,
      final Parser<Tok[]> lexer, final Parser<R> p,
      final String module) {
    return new NestedParser<R>(name, show, module, lexer, p, eof_title);
  }

  /** ****************** monadic combinators ******************* */
  private static final Parser<?> _one = one("one");

  /**
   * The parser that always succeed. It does not consume any input.
   * <p>
   * Parser ?
   * @return the Parser object.
   */
  public static Parser<?> one() {
    return _one;
  }

  /**
   * The parser that always succeed. It does not consume any input.
   * <p>
   * Parser ?
   * @param name
   *          the name of the Parser.
   * @return the Parser object.
   */
  public static Parser<?> one(final String name) {
    return new OneParser(name);
  }

  private static final Parser _zero = zero("zero");

  /**
   * The parser that always fails. It does not consume any input.
   * <p>
   * Parser *
   * 
   * @return the Parser object.
   */
  public static <x> Parser<x> zero() {
    return _zero;
  }

  /**
   * The parser that always fails. It does not consume any input.
   * <p>
   * Parser *
   * 
   * @param name
   *          the name of the Parser.
   * @return the Parser object.
   */
  public static <x> Parser<x> zero(final String name) {
    return new ZeroParser<x>(name);
  }

  /**
   * The parser that returns value v. It does not consume any input.
   * <p>
   * a -> Parser a
   * @param r the value to be returned by the parser.
   * @return the Parser object.
   */
  public static <R> Parser<R> retn(final R r) {
    return retn("return", r);
  }

  /**
   * The parser that returns value v. It does not consume any input.
   * <p>
   * a -> Parser a
   * 
   * @param name the name of the Parser.
   * @param r the value to be returned by the parser.
   * @return the Parser object.
   */
  public static <R> Parser<R> retn(final String name, final R r) {
    return new ReturnParser<R>(name, r);
  }

  /**
   * First run p, if it succeeds, run ToParser f with the value returned from p.
   * <p>
   * Parser a -> (a->Parser b) -> Parser b
   * 
   * @param name
   *          the name of the Parser.
   * @param p
   *          the first Parser object.
   * @param f
   *          the ToParser object to run if the first succeeds.
   * @return the Parser object.
   */
  public static <From, To> Parser<To> bind(final String name,
      final Parser<From> p, final ToParser<? super From, To> f) {
    return new Parser<To>(name) {
      boolean apply(final ParseContext ctxt) {
        if (!p.parse(ctxt))
          return false;
        return runNext(ctxt, f);
      }
    };
  }

  private static final ToParser _toReturn = toReturn("return");

  /**
   * Returns a ToParser instance that simply returns the previous return value.
   * <p>
   * a -> Parser a
   * 
   * @return The ToParser object.
   */
  public static <T> ToParser<T, T> toReturn() {
    return _toReturn;
  }

  /**
   * Returns a ToParser instance that simply returns the previous return value.
   * <p>
   * a -> Parser a
   * 
   * @return The ToParser object.
   */
  public static <T> ToParser<T, T> toReturn(final String name) {
    return new ToReturn<T>(name);
  }

  private static final ToParser _toOne = toParser(one());

  /**
   * Returns a ToParser that ignores the value passed in and simply returns
   * one().
   * <p> x -> Parser ?
   * 
   * @return the ToParser object.
   */
  public static <x> ToParser<x, ?> toOne() {
    return _toOne;
  }

  /**
   * Returns a ToParser that ignores the value passed in and simply returns
   * one().
   * <p> x -> Parser ?
   * 
   * @param name the name of the Parser that ToParser returns.
   * @return the ToParser object.
   */
  public static <x> ToParser<x, ?> toOne(final String name) {
    Parser<?> p = one(name);
    return toParser(p);
  }

  private static final ToParser _toZero = toParser(zero());

  /**
   * Returns a ToParser that ignores the value passed in and simply returns
   * zero().
   * <p> _ -> Parser *
   * 
   * @return the ToParser object.
   */
  public static <x, y> ToParser<x, y> toZero() {
    return _toZero;
  }

  /**
   * Returns a ToParser that ignores the value passed in and simply returns
   * zero().
   * <p> _ -> Parser *
   * 
   * @param name the name of the Parser that ToParser returns.
   * @return the ToParser object.
   */
  public static <x, y> ToParser<x, y> toZero(final String name) {
    Parser<y> p = zero(name);
    return toParser(p);
  }
  
  /**
   * Creates a ToParser object by always returning the same Parser object.
   * @param <x> the type of the input parameter. "x" is used to indicate that this type is irrelevant and we don't use it.
   * @param <R> the result type of the Parser object.
   * @param parser the parser object.
   * @return the ToParser object.
   * @since version 0.4.1
   */
  public static <x, R> ToParser<x, R> toParser(final Parser<R> parser){
    return new ToParser<x, R>() {
      public Parser<R> toParser(final x v) {
        return parser;
      }
    };
  }
  /**
   * Threads an array of ToParser into a single ToParser. The first return value
   * is passed to the first ToParser, and the result Parser is executed to get
   * the next return value. The return value keeps pass down until all ToParser
   * are called. If any Parser fails, the threading fails.
   * <p>
   * [(a->Parser a)] -> a -> Parser a
   * 
   * @param binders all the ToParser objects.
   * @return the new ToParser.
   */
  public static <T> ToParser<T, T> bindAll(final ToParser<T, T>... binders) {
    return bindAll("bindAll", binders);
  }

  /**
   * Threads an array of ToParser into a single ToParser. The first return value
   * is passed to the first ToParser, and the result Parser is executed to get
   * the next return value. The return value keeps pass down until all ToParser
   * are called. If any Parser fails, the threading fails.
   * <p>
   * [(a->Parser a)] -> a -> Parser a
   * 
   * @param binders all the ToParser objects.
   * @param name the name of the Parser created by the result ToParser.
   * @return the new ToParser.
   */
  public static <T> ToParser<T, T> bindAll(final String name,
      final ToParser<T, T>... binders) {
    if (binders.length == 0)
      return toReturn();
    if (binders.length == 1)
      return binders[0];
    return new AllBinders<T>(name, binders);
  }

  /**
   * Sequencing 2 parser objects. The first Parser is executed, if it succeeds,
   * the second Parser is executed.
   * <p>
   * Parser ? -> Parser b -> Parser b
   * 
   * @param p1 1st Parser.
   * @param p2 2nd Parser.
   * @return the new Parser.
   */
  public static <R> Parser<R> seq(final Parser<?> p1, final Parser<R> p2) {
    return seq(">>", p1, p2);
  }

  /**
   * Sequencing 2 parser objects. The first Parser is executed, if it succeeds,
   * the second Parser is executed.
   * <p>
   * Parser ? -> Parser b -> Parser b
   * 
   * @param name the name of the new Parser object.
   * @param p1 1st Parser.
   * @param p2 2nd Parser.
   * @return the new Parser.
   */
  public static <R> Parser<R> seq(final String name, final Parser<?> p1,
      final Parser<R> p2) {
    // return _seqAll(name, new Parser[]{p1, p2});
    // optimize. inline _seqAll to avoid creating array object.
    return new SeqParser<R>(name, p1, p2);
  }

  /**
   * Sequencing 3 parser objects.
   * <p>
   * Parser a -> Parser b -> Parser c -> Parser c
   * 
   * @param p1 1st Parser.
   * @param p2 2nd Parser.
   * @param p3 3rd Parser.
   * @return the new Parser.
   */
  public static <R> Parser<R> seq(final Parser<?> p1, final Parser<?> p2,
      final Parser<R> p3) {
    return seq(">>", p1, p2, p3);
  }

  /**
   * Sequencing 3 parser objects.
   * <p>
   * Parser a -> Parser b -> Parser c -> Parser c
   * 
   * @param name the name of the new Parser object.
   * @param p1 1st Parser.
   * @param p2 2nd Parser.
   * @param p3 3rd Parser.
   * @return the new Parser.
   */
  public static <R> Parser<R> seq(final String name, final Parser<?> p1,
      final Parser<?> p2, final Parser<R> p3) {
    return new Seq3Parser<R>(name, p3, p1, p2);
  }

  /**
   * Sequencing 4 parser objects.
   * <p>
   * Parser a -> Parser b -> Parser c -> Parser c -> Parser d -> Parser d
   * 
   * @param p1 1st Parser.
   * @param p2 2nd Parser.
   * @param p3 3rd Parser.
   * @param p4 4th Parser.
   * @return the new Parser.
   */
  public static <R> Parser<R> seq(final Parser<?> p1, final Parser<?> p2,
      final Parser<?> p3, final Parser<R> p4) {
    return seq(">>", p1, p2, p3, p4);
  }

  /**
   * Sequencing 4 parser objects.
   * <p>
   * Parser a -> Parser b -> Parser c -> Parser c -> Parser d -> Parser d
   * 
   * @param name the name of the new Parser object.
   * @param p1 1st Parser.
   * @param p2 2nd Parser.
   * @param p3 3rd Parser.
   * @param p4 4th Parser.
   * @return the new Parser.
   */
  public static <R> Parser<R> seq(final String name, final Parser<?> p1,
      final Parser<?> p2, final Parser<?> p3, final Parser<R> p4) {
    return new Seq4Parser<R>(name, p4, p1, p2, p3);
  }

  /**
   * Sequencing 5 parser objects.
   * <p>
   * Parser a -> Parser b -> Parser c -> Parser c -> Parser d -> Parser e -> Parser e
   * 
   * @param p1 1st Parser.
   * @param p2 2nd Parser.
   * @param p3 3rd Parser.
   * @param p4 4th Parser.
   * @param p5 5th Parser.
   * @return the new Parser.
   */
  public static <R> Parser<R> seq(final Parser<?> p1, final Parser<?> p2,
      final Parser<?> p3, final Parser<?> p4, final Parser<R> p5) {
    return seq(">>", p1, p2, p3, p4, p5);
  }

  /**
   * Sequencing 5 parser objects.
   * <p>
   * Parser a -> Parser b -> Parser c -> Parser c -> Parser d -> Parser e -> Parser e
   * 
   * @param name the name of the new Parser object.
   * @param p1 1st Parser.
   * @param p2 2nd Parser.
   * @param p3 3rd Parser.
   * @param p4 4th Parser.
   * @param p5 5th Parser.
   * @return the new Parser.
   */
  public static <R> Parser<R> seq(final String name, final Parser<?> p1,
      final Parser<?> p2, final Parser<?> p3, final Parser<?> p4,
      final Parser<R> p5) {
    return new Seq5Parser<R>(name, p1, p3, p5, p4, p2);
  }

  /**
   * Sequentially run 2 parser objects and collect the results in a Pair object.
   * @param p1 the first parser.
   * @param p2 the second parser.
   * @return the result parser.
   */
  public static <A,B> Parser<Pair<A,B>> pair(Parser<A> p1, Parser<B> p2){
    return pair("pair", p1, p2);
  }
  /**
   * Sequentially run 3 parser objects and collect the results in a Tuple3 object.
   * @param p1 the first parser.
   * @param p2 the second parser.
   * @param p3 the 3rd parser.
   * @return the result parser
   */
  public static <A,B,C> Parser<Tuple3<A,B,C>> tuple(Parser<A> p1, Parser<B> p2, Parser<C> p3){
    return tuple("tuple3", p1, p2, p3);
  }
  /**
   * Sequentially run 4 parser objects and collect the results in a Tuple4 object.
   * @param p1 the first parser.
   * @param p2 the second parser.
   * @param p3 the 3rd parser.
   * @param p4 the 4th parser.
   * @return the result parser
   */
  public static <A,B,C,D> Parser<Tuple4<A,B,C,D>> tuple(
      Parser<A> p1, Parser<B> p2, Parser<C> p3, Parser<D> p4){
    return tuple("tuple4", p1, p2, p3, p4);
  }
  /**
   * Sequentially run 5 parser objects and collect the results in a Tuple5 object.
   * @param p1 the first parser.
   * @param p2 the second parser.
   * @param p3 the 3rd parser.
   * @param p4 the 4th parser.
   * @param p5 the 5th parser.
   * @return the result parser
   */
  public static <A,B,C,D,E> Parser<Tuple5<A,B,C,D,E>> tuple(
      Parser<A> p1, Parser<B> p2, Parser<C> p3, Parser<D> p4, Parser<E> p5){
    return tuple("tuple5", p1, p2, p3, p4, p5);
  }
  /**
   * Sequentially run 2 parser objects and collect the results in a Pair object.
   * @param name the result parser name.
   * @param p1 the first parser.
   * @param p2 the second parser.
   * @return the result parser.
   */
  public static <A,B> Parser<Pair<A,B>> pair(String name, Parser<A> p1, Parser<B> p2){
    final Map2<A,B,Pair<A,B>> m2 = Maps.id2();
    return map2(name, p1, p2, m2);
  }
  /**
   * Sequentially run 3 parser objects and collect the results in a Tuple3 object.
   * @param name the result parser name.
   * @param p1 the first parser.
   * @param p2 the second parser.
   * @param p3 the 3rd parser.
   * @return the result parser
   */
  public static <A,B,C> Parser<Tuple3<A,B,C>> tuple(String name,
      Parser<A> p1, Parser<B> p2, Parser<C> p3){
    final Map3<A,B,C,Tuple3<A,B,C>> m3 = Maps.id3();
    return map3(name, p1, p2, p3, m3);
  }
  /**
   * Sequentially run 4 parser objects and collect the results in a Tuple4 object.
   * @param name the result parser name.
   * @param p1 the first parser.
   * @param p2 the second parser.
   * @param p3 the 3rd parser.
   * @param p4 the 4th parser.
   * @return the result parser
   */
  public static <A,B,C,D> Parser<Tuple4<A,B,C,D>> tuple(String name,
      Parser<A> p1, Parser<B> p2, Parser<C> p3, Parser<D> p4){
    final Map4<A,B,C,D,Tuple4<A,B,C,D>> m4 = Maps.id4();
    return map4(name, p1, p2, p3, p4, m4);
  }
  /**
   * Sequentially run 5 parser objects and collect the results in a Tuple5 object.
   * @param name the result parser name.
   * @param p1 the first parser.
   * @param p2 the second parser.
   * @param p3 the 3rd parser.
   * @param p4 the 4th parser.
   * @param p5 the 5th parser.
   * @return the result parser
   */
  public static <A,B,C,D,E> Parser<Tuple5<A,B,C,D,E>> tuple(String name,
      Parser<A> p1, Parser<B> p2, Parser<C> p3, Parser<D> p4, Parser<E> p5){
    final Map5<A,B,C,D,E,Tuple5<A,B,C,D,E>> m5 = Maps.id5();
    return map5(name, p1, p2, p3, p4, p5, m5);
  }
  /**
   * Sequencing of an array of Parser objects. If the array is empty, one() is
   * returned. <br>
   * The array of Parser objects are executed sequentially until an error
   * occured or all the Parsers are executed. Return values are discarded.
   * <p>
   * [Parser ?] -> Parser ?
   * 
   * @param ps the array of Parser objects.
   * @return the new Parser object.
   * @since version 1.0
   */
  public static Parser<?> sequence(final Parser<?>... ps) {
    return seqAll(ps);
  }

  /**
   * Sequencing of an array of Parser objects. If the array is empty, one() is
   * returned. <br>
   * The array of Parser objects are executed sequentially until an error
   * occured or all the Parsers are executed. Return values are discarded.
   * <p>
   * [Parser ?] -> Parser ?
   * 
   * @param name the name of the new Parser.
   * @param ps the array of Parser objects.
   * @return the new Parser object.
   * @since version 1.0
   */
  public static Parser<?> sequence(final String name, final Parser<?>... ps) {
    return seqAll(name, ps);
  }

  /**
   * Sequencing of an array of Parser objects. If the array is empty, one() is
   * returned. <br>
   * The array of Parser objects are executed sequentially until an error
   * occured or all the Parsers are executed. Return values are discarded.
   * <p>
   * [Parser ?] -> Parser Object
   * 
   * @param ps
   *          the array of Parser objects.
   * @return the new Parser object.
   */
  public static Parser<Object> seqAll(final Parser<?>[] ps) {
    return seqAll("seqAll", ps);
  }

  /**
   * Sequencing of an array of Parser objects. If the array is empty, one() is
   * returned. <br>
   * The array of Parser objects are executed sequentially until an error
   * occured or all the Parsers are executed. Return values are discarded.
   * <p>
   * [Parser ?] -> Parser Object
   * 
   * @param name
   *          the name of the new Parser.
   * @param ps
   *          the array of Parser objects.
   * @return the new Parser object.
   */
  public static Parser<Object> seqAll(final String name, final Parser<?>[] ps) {
    if (ps.length == 0)
      return one().convert();
    if (ps.length == 1)
      return ps[0].convert();
    return _seqAll(name, ps);
  }

  private static Parser<Object> _seqAll(final String name, final Parser<?>[] ps) {
    return new SequenceParser(name, ps);
  }

  /**
   * Fails if the return value of the previous parser does not satisify the
   * given predicate. No-op otherwise.
   * 
   * @param name
   *          the name of the new Parser object.
   * @param op
   *          the predicate object.
   * @return the new Parser object.
   */
  public static <R> Parser<R> isReturn(final String name,
      final ObjectPredicate<R> op) {
    return new IsReturnParser<R>(name, op);
  }

  /**
   * The created Parser object will first run parser p, if the return value of
   * parser p does not satisify the given predicate, it fails and the input
   * consumption of parser p is undone. It is an atomic parser.
   * 
   * @param name
   *          the name of the new Parser object.
   * @param p
   *          the parser object to test the return value of.
   * @param op
   *          the predicate object.
   * @return the new Parser object.
   */
  public static <R> Parser<R> isReturn(final String name, final Parser<R> p,
      final ObjectPredicate<? super R> op) {
    final Parser<R> p2 = isReturn(name, op).convert();
    return seq(name, p, p2).atomize(name);
  }

  /**
   * The created Parser object will first run parser p, if the return value of
   * parser p does not satisify the given predicate, it fails and the input
   * consumption of parser p is undone. It is an atomic parser.
   * 
   * @param name
   *          the name of the new Parser object.
   * @param p
   *          the parser object to test the return value of.
   * @param op
   *          the predicate object.
   * @param expecting
   *          the "expected" error message.
   * @return the new Parser object.
   */
  public static <R> Parser<R> isReturn(final String name, final Parser<R> p,
      final ObjectPredicate<? super R> op, final String expecting) {
    final Parser<R> p2 = isReturn(name, op).convert();
    return p.seq(name, p2.label(name, expecting)).atomize(name);
  }

  /**
   * Fails if the current user state value does not satisify the given
   * predicate. No-op otherwise.
   * 
   * @param op
   *          the predicate object.
   * @return the new Parser object.
   * @deprecated as of version 0.6
   */
  @Deprecated
  public static Parser<?> isState(final ObjectPredicate<Object> op) {
    return isState("isState", op);
  }

  /**
   * Fails if the current user state value does not satisify the given
   * predicate. No-op otherwise.
   * 
   * @param name
   *          the name of the new Parser object.
   * @param op
   *          the predicate object.
   * @return the new Parser object.
   * @deprecated as of version 0.6
   */
  @Deprecated
  public static Parser<?> isState(final String name,
      final ObjectPredicate<Object> op) {
    return new Parser<Object>(name) {
      boolean apply(final ParseContext ctxt) {
        final Object r = ctxt.getUserState();
        return op.isObject(r);
      }
    };
  }

  /**
   * lookahead looks at logical steps. step(String, int, Parser) runs this
   * parser and sets the number of logical steps.
   * 
   * @param name
   *          the name of the new Parser object.
   * @param n
   *          the number logical steps. n>=0 has to be true.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <R> Parser<R> step(final String name, final int n,
      final Parser<R> p) {
    if (n < 0)
      throw new IllegalArgumentException("" + n + "<0");
    return new StepParser<R>(name, p, n);
  }

  /**
   * By default, ifelse, plus, sum will not try to run the next branch if the
   * previous branch failed and consumed some input. this is because the default
   * look-ahead token is 1. <br>
   * by using lookahead, this default behavior can be altered. Parsers.plus(p1,
   * p2).lookahead(3) will still try p2 even if p1 fails and consumes one or two
   * inputs.
   * <p>
   * lookahead only affects one nesting level.
   * Parsers.plus(p1,p2).ifelse(yes,no).lookahead(3) will not affect the
   * Parsers.plus(p1,p2) nested within ifelse.
   * <p>
   * lookahead directly on top of lookahead will override the previous
   * lookahead. Parsers.plus(p1,p2).lookahead(3).lookahead(1) is equivalent as
   * Parsers.plus(p1, p2).lookahead(1). <br>
   * lookahead looks at logical step. by default, each terminal is one logical
   * step. atomize() combinator ensures at most 1 logical step for a parser. Use
   * step() combinator to fine control logical steps.
   * 
   * @param name
   *          the name of the new Parser object.
   * @param toknum
   *          the number of tokens to look ahead.
   * @return the new Parser object.
   */
  public static <R> Parser<R> lookahead(final String name, final int toknum,
      final Parser<R> p) {
    if (toknum <= 0)
      return p;
    return new LookaheadParser<R>(name, p, toknum);
  }

  /**
   * Sequencing of an array of Parser objects. The array of Parser objects are
   * executed sequentially until an error occured or all the Parsers are
   * executed. Return values are collected as a Object[] array and transformed
   * by a Mapn object.
   * <p>
   * [Parser a] -> ([a]->r) -> Parser r
   * 
   * @param ps
   *          the array of Parser objects.
   * @param mn
   *          the Mapn object.
   * @return the new Parser object.
   */
  public static <R> Parser<R> mapn(final Parser<?>[] ps, final Mapn<R> mn) {
    return mapn("mapn", ps, mn);
  }

  /**
   * Sequencing of an array of Parser objects. The array of Parser objects are
   * executed sequentially until an error occured or all the Parsers are
   * executed. Return values are collected and returned as an Object[] array and
   * transformed by a Mapn object.
   * <p>
   * [Parser a] -> ([a]->r) -> Parser r
   * 
   * @param name
   *          the name of the new Parser.
   * @param ps
   *          the array of Parser objects.
   * @param mn
   *          the Mapn object.
   * @return the new Parser object.
   */
  public static <R> Parser<R> mapn(final String name, final Parser<?>[] ps,
      final Mapn<R> mn) {
    if (ps.length == 0) {
      final R r = mn.map(_empty_array);
      return retn(r);
    }
    return mapn(name, ArrayFactories.defaultFactory(), ps, mn);
  }

  /**
   * Sequencing of an array of Parser objects. The array of Parser objects are
   * executed sequentially until an error occured or all the Parsers are
   * executed. Return values are collected and returned as a T[] array where T
   * is the element type, and then transformed by a Mapn object.
   * <p>
   * [Parser a] -> ([a]->r) -> Parser r
   * 
   * @param etype
   *          the element type of the return value array.
   * @param ps
   *          the array of Parser objects.
   * @param mn
   *          the Mapn object.
   * @return the new Parser object.
   */
  public static <E, R> Parser<R> mapn(final Class<? super E> etype,
      final Parser<E>[] ps, final Mapn<R> mn) {
    return mapn("mapn", etype, ps, mn);
  }

  /**
   * Sequencing of an array of Parser objects. The array of Parser objects are
   * executed sequentially until an error occured or all the Parsers are
   * executed. Return values are collected and returned as a T[] array where T
   * is the element type, and then transformed by a Mapn object.
   * <p>
   * [Parser a] -> ([a]->r) -> Parser r
   * 
   * @param name
   *          the name of the new Parser.
   * @param etype
   *          the element type of the return value array.
   * @param ps
   *          the array of Parser objects.
   * @param mn
   *          the Mapn object.
   * @return the new Parser object.
   */
  public static <E, R> Parser<R> mapn(final String name,
      final Class<? super E> etype, final Parser<E>[] ps, final Mapn<R> mn) {
    return mapn(name, ArrayFactories.typedFactory(etype), ps, mn);
  }

  /**
   * Sequencing of an array of Parser objects. The array of Parser objects are
   * executed sequentially until an error occured or all the Parsers are
   * executed. Return values are collected and returned as an array created by
   * the ArrayFactory parameter, and then transformed by a Mapn object.
   * <p>
   * [Parser a] -> ([a]->r) -> Parser r
   * 
   * @param af
   *          the ArrayFactory object.
   * @param ps
   *          the array of Parser objects.
   * @param mn
   *          the Mapn object.
   * @return the new Parser object.
   */
  public static <E, R> Parser<R> mapn(final ArrayFactory<?> af,
      final Parser<?>[] ps, final Mapn<R> mn) {
    return mapn("mapn", af, ps, mn);
  }

  /**
   * Sequencing of an array of Parser objects. The array of Parser objects are
   * executed sequentially until an error occured or all the Parsers are
   * executed. Return values are collected and returned as an array created by
   * the ArrayFactory parameter and then transformed by a Mapn object.
   * <p>
   * [Parser a] -> ([a]->r) -> Parser r
   * 
   * @param name
   *          the name of the new Parser.
   * @param af
   *          the ArrayFactory object.
   * @param ps
   *          the array of Parser objects.
   * @param mn
   *          the Mapn object.
   * @return the new Parser object.
   */
  public static <R> Parser<R> mapn(final String name,
      final ArrayFactory<?> af, final Parser<?>[] ps, final Mapn<R> mn) {
    return _mapn(name, af, ps, mn);
  }

  private static <R> Parser<R> _mapn(final String name,
      final ArrayFactory<?> af, final Parser<?>[] ps, final Mapn<R> mn) {
    return new MapnParser<R>(name, mn, af, ps);
  }

  /**
   * A parser that always fail with the given error message.
   * <p>
   * Parser *
   * 
   * @param msg
   *          the error message.
   * @return the Parser object.
   */
  public static <R> Parser<R> fail(final String msg) {
    return fail("fail", msg);
  }

  /**
   * A parser that always fail with the given error message.
   * <p>
   * Parser *
   * 
   * @param name
   *          the Parser object name.
   * @param msg
   *          the error message.
   * @return the Parser object.
   */
  public static <R> Parser<R> fail(final String name, final String msg) {
    return new FailureParser<R>(name, msg);
  }

  /**
   * First run Parser p, if it succeeds, thread the return value to ToParser
   * yes; if it fails and no input is consumed, run Parser no; fails if p fails
   * and some input is consumed.
   * <p>
   * Parser a -> (a->Parser b) -> Parser b -> Parser b
   * 
   * @param name
   *          the name of the new Parser object.
   * @param p
   *          the Parser object to test.
   * @param yes
   *          the true branch.
   * @param no
   *          the false branch.
   * @return the new Parser object.
   */
  public static <C, R> Parser<R> ifelse(final String name, final Parser<C> p,
      final ToParser<? super C, R> yes, final Parser<? extends R> no) {
    return new IfElseParser<R, C>(name, no, p, yes);
  }

  /**
   * First run Parser p, if it succeeds, run Parser yes; if it fails and no
   * input is consumed, run Parser no; fails if p fails and some input is
   * consumed.
   * <p>
   * Parser x -> Parser b -> Parser b -> Parser b
   * 
   * @param name
   *          the name of the new Parser object.
   * @param p
   *          the Parser object to test.
   * @param yes
   *          the true branch.
   * @param no
   *          the false branch.
   * @return the new Parser object.
   */
  public static <C, R> Parser<R> ifelse(final String name, final Parser<C> p,
      final Parser<R> yes, final Parser<? extends R> no) {
    final ToParser<C, R> binder = toParser(yes);
    return ifelse(name, p, binder, no);
    /*
     * return new Parser(name){ boolean apply(final ParserState state){ final
     * Object ustate = state.getUserState(); final Object ret =
     * state.getReturn(); final int at = state.getAt(); final ParsecError error =
     * state.getError(); if(p.parse(state)) return yes.parse(state); return
     * recover(no, state, at, ret, ustate, error); } };
     */
  }
  /**
   * 2 alternative parser objects.
   * If the first Parser fails with no input consumption, the second one is executed.
   * <p>
   * For backwward compatibility with java 1.4. Use the vararg version in java 5.
   * </p>
   * <p> Parser a -> Parser a -> Parser a
   * @param p1 1st Parser.
   * @param p2 2nd Parser.
   * @return the new Parser.
   */
  public static <R> Parser<R> plus(final Parser<R> p1, final Parser<? extends R> p2){
    return plus("plus", p1, p2);
  }
  /**
   * 2 alternative parser objects.
   * If the first Parser fails with no input consumption, the second one is executed.
   * <p>
   * For backwward compatibility with java 1.4. Use the vararg version in java 5.
   * </p>
   * <p> Parser a -> Parser a -> Parser a
   * @param name the name of the new Parser object.
   * @param p1 1st Parser.
   * @param p2 2nd Parser.
   * @return the new Parser.
   */
  public static <R> Parser<R> plus(final String name, final Parser<R> p1, final Parser<? extends R> p2){
    return plus(name, new Parser[]{p1, p2});
    //special optimize. inline _plusAll rather than creating the array object.
    /*
    return new Parser(name){
      boolean apply(final ParserState state){
        final Object ustate = state.getUserState();
        final Object ret = state.getReturn();
        final int at = state.getAt();
        final ParsecError error = state.getError();
        if(p1.parse(state)) return true;
        return recover(p2, state, at, ret, ustate, error);
      }
    };*/
  }
  /**
   * 3 alternative parser objects.
   * <p>
   * For backwward compatibility with java 1.4. Use the vararg version in java 5.
   * </p>
   * <p> Parser a -> Parser a -> Parser a -> Parser a
   * @param p1 1st Parser.
   * @param p2 2nd Parser.
   * @param p3 3rd Parser.
   * @return the new Parser.
   */
  public static <R> Parser<R> plus(final Parser<R> p1, 
      final Parser<? extends R> p2, final Parser<? extends R> p3){
    return plus("plus", p1, p2, p3);
  }
  /**
   * 3 alternative parser objects.
   * <p>
   * For backwward compatibility with java 1.4. Use the vararg version in java 5.
   * </p>
   * <p> Parser a -> Parser a -> Parser a -> Parser a
   * @param name the name of the new Parser object.
   * @param p1 1st Parser.
   * @param p2 2nd Parser.
   * @param p3 3rd Parser.
   * @return the new Parser.
   */
  public static <R> Parser<R> plus(final String name, final Parser<R> p1, 
      final Parser<? extends R> p2, final Parser<? extends R> p3){
    return plus(name, new Parser[]{p1, p2, p3});
  }
  /**
   * 4 alternative parser objects.
   * <p>
   * For backwward compatibility with java 1.4. Use the vararg version in java 5.
   * </p>
   * <p> Parser a -> Parser a -> Parser a -> Parser a -> Parser a
   * @param p1 1st Parser.
   * @param p2 2nd Parser.
   * @param p3 3rd Parser.
   * @param p4 4th Parser.
   * @return the new Parser.
   */
  public static <R> Parser<R> plus(final Parser<R> p1, 
      final Parser<? extends R> p2, final Parser<? extends R> p3, final Parser<? extends R> p4){
    return plus("plus", p1, p2, p3, p4);
  }
  /**
   * 4 alternative parser objects.
   * <p>
   * For backwward compatibility with java 1.4. Use the vararg version in java 5.
   * </p>
   * <p> Parser a -> Parser a -> Parser a -> Parser a -> Parser a
   * @param name the name of the new Parser object.
   * @param p1 1st Parser.
   * @param p2 2nd Parser.
   * @param p3 3rd Parser.
   * @param p4 4th Parser.
   * @return the new Parser.
   */
  public static <R> Parser<R> plus(final String name, final Parser<R> p1, final Parser<? extends R> p2, 
      final Parser<? extends R> p3, final Parser<? extends R> p4){
    return plus(name, new Parser[]{p1, p2, p3, p4});
  }
  /**
   * 5 alternative parser objects.
   * <p>
   * For backwward compatibility with java 1.4. Use the vararg version in java 5.
   * </p>
   * <p> Parser a -> Parser a -> Parser a -> Parser a -> Parser a -> Parser a
   * @param p1 1st Parser.
   * @param p2 2nd Parser.
   * @param p3 3rd Parser.
   * @param p4 4th Parser.
   * @param p5 5th Parser.
   * @return the new Parser.
   */
  public static <R> Parser<R> plus(final Parser<R> p1, final Parser<? extends R> p2, final Parser<? extends R> p3, 
      final Parser<? extends R> p4, final Parser<? extends R> p5){
    return plus("plus", p1, p2, p3, p4, p5);
  }
  /**
   * 5 alternative parser objects.
   * <p>
   * For backwward compatibility with java 1.4. Use the vararg version in java 5.
   * </p>
   * <p> Parser a -> Parser a -> Parser a -> Parser a -> Parser a -> Parser a
   * @param name the name of the new Parser object.
   * @param p1 1st Parser.
   * @param p2 2nd Parser.
   * @param p3 3rd Parser.
   * @param p4 4th Parser.
   * @param p5 5th Parser.
   * @return the new Parser.
   */
  public static <R> Parser<R> plus(final String name, 
      final Parser<R> p1, final Parser<? extends R> p2, final Parser<? extends R> p3, 
      final Parser<? extends R> p4, final Parser<? extends R> p5){
    return plus(name, new Parser[]{p1, p2, p3, p4, p5});
  }
  /**
   * combine alternative parser objects. If the first Parser fails with no input
   * consumption, the next ones are executed until one succeeds.
   * 
   * @param name
   *          the name of the created parser.
   * @param ps
   *          the Parser objects.
   * @return the new Parser.
   */
  public static <R> Parser<R> plus(String name, final Parser<R>... ps) {
    return sum(name, ps).convert();
  }

  /**
   * combine alternative parser objects. If the first Parser fails with no input
   * consumption, the next ones are executed until one succeeds.
   * 
   * @param ps
   *          the Parser objects.
   * @return the new Parser.
   */
  public static <R> Parser<R> plus(final Parser<R>... ps) {
    return sum("plus", ps).convert();
  }

  /**
   * An array of alternative Parser objects. zero() is returned if the array is
   * empty. the returned Parser object will try the Parser objects in the array
   * one by one, until one of the following conditioins are met: the Parser
   * succeeds, (sum() succeeds) <br>
   * the Parser fails with input consumed (sum() fails with input consumed) <br>
   * the end of array is encountered. (sum() fails with no input consumed).
   * <p>
   * [Parser a] -> Parser a
   * 
   * @param ps
   *          the array of alternative Parser objects.
   * @return the new Parser object.
   */
  public static Parser<Object> sum(final Parser<?>... ps) {
    return sum("sum", ps);
  }

  /**
   * An array of alternative Parser objects. zero() is returned if the array is
   * empty. the returned Parser object will try the Parser objects in the array
   * one by one, until one of the following conditioins are met: the Parser
   * succeeds, (sum() succeeds) <br>
   * the Parser fails with input consumed (sum() fails with input consumed) <br>
   * the end of array is encountered. (sum() fails with no input consumed).
   * <p>
   * [Parser a] -> Parser a
   * 
   * @param name
   *          the name of the new Parser object.
   * @param ps
   *          the array of alternative Parser objects.
   * @return the new Parser object.
   */
  public static Parser<Object> sum(final String name, final Parser<?>... ps) {
    if (ps.length == 0)
      return zero();
    if (ps.length == 1)
      return ps[0].convert();
    return _plusAll(name, ps);
  }

  /**
   * Runs two alternative parsers. If both succeed, the one with the longer
   * match wins. If both matches the same length, the first one is favored.
   * 
   * @param name
   *          the name of the new Parser object.
   * @param p1
   *          the 1st alternative parser.
   * @param p2
   *          the 2nd alternative parser.
   * @return the new Parser object.
   */
  public static <R> Parser<R> longer(final String name, final Parser<R> p1,
      final Parser<R> p2) {
    return _longest(name, p1, p2);
  }

  /**
   * Runs an array of alternative parsers. If more than one succeed, the one
   * with the longest match wins. If two matches have the same length, the first
   * one is favored.
   * 
   * @param name
   *          the name of the new Parser object.
   * @param ps
   *          the array of alternative parsers.
   * @return the new Parser object.
   */

  public static <R> Parser<R> longest(final String name, final Parser<R>... ps) {
    if (ps.length == 0)
      return zero();
    if (ps.length == 1)
      return ps[0];
    return _longest(name, ps);
  }

  /**
   * Runs two alternative parsers. If both succeed, the one with the shorter
   * match wins. If both matches the same length, the first one is favored.
   * 
   * @param name
   *          the name of the new Parser object.
   * @param p1
   *          the 1st alternative parser.
   * @param p2
   *          the 2nd alternative parser.
   * @return the new Parser object.
   */

  public static <R> Parser<R> shorter(final String name, final Parser<R> p1,
      final Parser<R> p2) {
    return _shortest(name, p1, p2);
  }

  /**
   * Runs an array of alternative parsers. If more than one succeed, the one
   * with the shortest match wins. If two matches have the same length, the
   * first one is favored.
   * 
   * @param name
   *          the name of the new Parser object.
   * @param ps
   *          the array of alternative parsers.
   * @return the new Parser object.
   */

  public static <R> Parser<R> shortest(final String name, final Parser<R>... ps) {
    if (ps.length == 0)
      return zero();
    if (ps.length == 1)
      return ps[0];
    return _shortest(name, ps);
  }

  private static <R> Parser<R> _shortest(final String name,
      final Parser<R>... ps) {
    return _alternate(name, ps, IntOrders.lt());
  }

  private static <R> Parser<R> _longest(final String name,
      final Parser<R>... ps) {
    return _alternate(name, ps, IntOrders.gt());
  }

  /**
   * Runs two alternative parsers. If both succeed, the one with the longer
   * match wins. If both matches the same length, the first one is favored.
   * 
   * @param p1
   *          the 1st alternative parser.
   * @param p2
   *          the 2nd alternative parser.
   * @return the new Parser object.
   */
  public static <R> Parser<R> longer(final Parser<R> p1, final Parser<R> p2) {
    return longer("longer", p1, p2);
  }

  /**
   * Runs an array of alternative parsers. If more than one succeed, the one
   * with the longest match wins. If two matches have the same length, the first
   * one is favored.
   * 
   * @param ps
   *          the array of alternative parsers.
   * @return the new Parser object.
   */

  public static <R> Parser<R> longest(final Parser<R>... ps) {
    return longest("longest", ps);
  }

  /**
   * Runs two alternative parsers. If both succeed, the one with the shorter
   * match wins. If both matches the same length, the first one is favored.
   * 
   * @param p1
   *          the 1st alternative parser.
   * @param p2
   *          the 2nd alternative parser.
   * @return the new Parser object.
   */

  public static <R> Parser<R> shorter(final Parser<R> p1, final Parser<R> p2) {
    return shorter("shorter", p1, p2);
  }

  /**
   * Runs an array of alternative parsers. If more than one succeed, the one
   * with the shortest match wins. If two matches have the same length, the
   * first one is favored.
   * 
   * @param ps
   *          the array of alternative parsers.
   * @return the new Parser object.
   */

  public static <R> Parser<R> shortest(final Parser<R>... ps) {
    return shortest("shortest", ps);
  }
  /**
   * To create a Parser that runs an array of Parser objects
   * until one succeeds. Regardless of look-ahead,
   * input consumption will not prevent the next parser from being executed.
   * 
   * @param name the new parser name.
   * @param alternatives the alternative parsers.
   * @return the new Parser object.
   */
  public static Parser<Object> or(String name, final Parser<?>... alternatives){
    return new OrParser(name, alternatives);
  }
  /**
   * To create a Parser that runs an array of Parser objects
   * until one succeeds. Regardless of look-ahead,
   * input consumption will not prevent the next parser from being executed.
   * 
   * @param alternatives the alternative parsers.
   * @return the new Parser object.
   */
  public static Parser<Object> or(final Parser<?>... alternatives){
    return or("or", alternatives);
  }
  /**
   * To create a Parser that runs an array of Parser objects
   * until one succeeds. Regardless of look-ahead,
   * input consumption will not prevent the next parser from being executed.
   * 
   * @param name the new parser name.
   * @param alternatives the alternative parsers.
   * @return the new Parser object.
   */
  public static <T> Parser<T> alt(String name, final Parser<T>... alternatives){
    return new OrParser(name, alternatives);
  }
  /**
   * To create a Parser that runs an array of Parser objects
   * until one succeeds. Regardless of look-ahead,
   * input consumption will not prevent the next parser from being executed.
   * 
   * @param alternatives the alternative parsers.
   * @return the new Parser object.
   */
  public static <T> Parser<T> alt(final Parser<T>... alternatives){
    return alt("or", alternatives);
  }
  private static Parser<Object> _plusAll(final String name, final Parser<?>... ps) {
    return new SumParser(name, ps);
  }

  private static <R> Parser<R> _alternate(final String name,
      final Parser<R>[] ps, final IntOrder ord) {
    return new BestParser<R>(name, ps, ord);
  }

  /** ****************** monadic operations ******************* */

  /** ****************** additional combinators ******************* */
  /**
   * First run the Parser p, if it succeeds with input consumed, isConsumed()
   * succeeds; if it fails or did not consume input, isConsumed() fails.
   * <p>
   * Parser a -> Parser a
   * 
   * @param p
   *          the Parser object to test.
   * @return the new Parser object.
   */
  public static <R> Parser<R> isConsumed(final Parser<R> p) {
    return isConsumed("isConsumed", p);
  }

  /**
   * First run the Parser p, if it succeeds with input consumed, isConsumed()
   * succeeds; if it fails or did not consume input, isConsumed() fails.
   * <p>
   * Parser a -> Parser a
   * 
   * @param p
   *          the Parser object to test.
   * @param err
   *          the error message when p succeeds with no input consumed.
   * @return the new Parser object.
   */
  public static <R> Parser<R> isConsumed(final Parser<R> p, final String err) {
    return isConsumed("isConsumed", p, err);
  }

  /**
   * First run the Parser p, if it succeeds with input consumed, isConsumed()
   * succeeds; if it fails or did not consume input, isConsumed() fails.
   * <p>
   * Parser a -> Parser a
   * 
   * @param name
   *          the name of the new Parser object.
   * @param p
   *          the Parser object to test.
   * @return the new Parser object.
   */
  public static <R> Parser<R> isConsumed(final String name, final Parser<R> p) {
    return isConsumed(name, p, "input not consumed");
  }

  /**
   * First run the Parser p, if it succeeds with input consumed, isConsumed()
   * succeeds; if it fails or did not consume input, isConsumed() fails.
   * <p>
   * Parser a -> Parser a
   * 
   * @param name
   *          the name of the new Parser.
   * @param p
   *          the Parser object to test.
   * @param err
   *          the error message when p succeeds with no input consumed.
   * @return the new Parser object.
   */
  public static <R> Parser<R> isConsumed(final String name, final Parser<R> p,
      final String err) {
    return new IsConsumedParser<R>(name, p, err);
  }

  /**
   * First run the Parser p, if it succeeds with no input consumed,
   * notConsumed() succeeds; if it fails, notConsumed() fails; if it succeeds
   * and consumes input, the input consumption is undone and notConsumed()
   * fails.
   * <p>
   * Parser a -> Parser a
   * 
   * @param p
   *          the Parser object to test.
   * @return the new Parser object.
   */
  public static <R> Parser<R> notConsumed(final Parser<R> p) {
    return notConsumed("notConsumed", p);
  }

  /**
   * First run the Parser p, if it succeeds with no input consumed,
   * notConsumed() succeeds; if it fails, notConsumed() fails; if it succeeds
   * and consumes input, the input consumption is undone and notConsumed()
   * fails.
   * <p>
   * Parser a -> Parser a
   * 
   * @param p
   *          the Parser object to test.
   * @param err
   *          the error message when p succeeds and consumes some input.
   * @return the new Parser object.
   */
  public static <R> Parser<R> notConsumed(final Parser<R> p, final String err) {
    return notConsumed("notConsumed", p, err);
  }

  /**
   * First run the Parser p, if it succeeds with no input consumed,
   * notConsumed() succeeds; if it fails, notConsumed() fails; if it succeeds
   * and consumes input, the input consumption is undone and notConsumed()
   * fails.
   * <p>
   * Parser a -> Parser a
   * 
   * @param name
   *          the name of the new Parser.
   * @param p
   *          the Parser object to test.
   * @return the new Parser object.
   */
  public static <R> Parser<R> notConsumed(final String name, final Parser<R> p) {
    return notConsumed(name, p, "input consumed");
  }

  /**
   * First run the Parser p, if it succeeds with no input consumed,
   * notConsumed() succeeds; if it fails, notConsumed() fails; if it succeeds
   * and consumes input, the input consumption is undone and notConsumed()
   * fails.
   * <p>
   * Parser a -> Parser a
   * 
   * @param name
   *          the name of the new Parser.
   * @param p
   *          the Parser object to test.
   * @param err
   *          the error message when p succeeds and consumes some input.
   * @return the new Parser object.
   */
  public static <R> Parser<R> notConsumed(final String name, final Parser<R> p,
      final String err) {
    return new NotConsumedParser<R>(name, p, err);
  }

  /**
   * Create a lazy evaluated Parser. the ParserEval object is evaluated only
   * when the Parser is actually executed.
   * <p>
   * Parser a -> Parser a
   * 
   * @param p
   *          the ParserEval object.
   * @return the Parser object.
   */
  public static <R> Parser<R> lazy(final ParserEval<R> p) {
    return lazy("lazy", p);
  }
  
  /**
   * Create a lazy evaluated parser.
   * When evaluated, it reads the parser object stored in an array indexed by pos.
   * @param placeholder the array that contains parser object.
   * @param pos the position (0-based) of the parser to lazily evaluate.
   * @return the lazy parser.
   */
  public static <R> Parser<R> lazy(final Parser<R>[] placeholder, final int pos){
    return lazy(new ParserEval<R>(){
      public Parser<R> eval() {
        return placeholder[pos];
      }
    });
  }
  /**
   * Create a lazy evaluated parser.
   * When evaluated, it reads the first parser object stored in an array.
   * @param placeholder the array whose first object is the lazily evaluated parser object.
   * @return the lazy parser.
   */
  public static <R> Parser<R> lazy(final Parser<R>[] placeholder){
    return lazy(placeholder, 0);
  }
  /**
   * Create a lazy evaluated Parser. the ParserEval object is evaluated only
   * when the Parser is actually executed.
   * <p>
   * Parser a -> Parser a
   * 
   * @param name
   *          the name of the Parser object.
   * @param p
   *          the ParserEval object.
   * @return the Parser object.
   */
  public static <R> Parser<R> lazy(final String name, final ParserEval<R> p) {
    return LazyParser.instance(name, p);
  }

  private static final Parser<Object> _getState = getState("getState");

  /**
   * Retrieves the user state.
   * <p>
   * Parser u u
   * 
   * @return the Parser object.
   * @deprecated as of version 0.6
   */
  @Deprecated
  public static Parser<Object> getState() {
    return _getState;
  }

  /**
   * Retrieves the user state.
   * <p>
   * Parser u u
   * 
   * @param name
   *          the name of the Parser object.
   * @return the Parser object.
   */
  @Deprecated
  public static Parser<Object> getState(final String name) {
    return transformState(name, Maps.id());
  }

  /**
   * Updates the user state. The old user state value is returned.
   * <p>
   * Parser u1 u
   * 
   * @param s
   *          the new user state value.
   * @return the Parser object.
   * @deprecated as of version 0.6
   */
  @Deprecated
  public static Parser<Object> setState(final Object s) {
    return setState("setState", s);
  }

  /**
   * Updates the user state.
   * <p>
   * Parser u1 u
   * 
   * @param name
   *          the name of the Parser object.
   * @param s
   *          the new user state value.
   * @return the Parser object.
   * @deprecated as of version 0.6
   */
  @Deprecated
  public static Parser<Object> setState(final String name, final Object s) {
    return transformState(name, Maps.cnst(s));
  }

  /**
   * Transforms and updates the user state. The old user state value is
   * returned.
   * <p>
   * (u1->u2) -> Parser u2 u1
   * 
   * @param m
   *          the transformation.
   * @return the Parser object.
   * @deprecated as of version 0.6
   */
  @Deprecated
  public static <State> Parser<State> transformState(final Map<State, ?> m) {
    return transformState("transformState", m);
  }

  /**
   * Transforms and updates the user state. The old user state value is
   * returned.
   * <p>
   * (u1->u2) -> Parser u2 u1
   * 
   * @param name
   *          the name of the Parser object.
   * @param m
   *          the transformation.
   * @return the Parser object.
   * @deprecated as of version 0.6
   */
  @Deprecated
  public static <State> Parser<State> transformState(final String name,
      final Map<State, ?> m) {
    return new Parser<State>(name) {
      boolean apply(final ParseContext ctxt) {
        final Object ustate = ctxt.getUserState();
        ctxt.setUserState(m.map((State) ustate));
        ctxt.setReturn(ustate);
        return true;
      }
    };
  }

  private static final Parser<Integer> _getIndex = getIndex("getIndex");

  /**
   * Retrieves the current index in the source.
   * <p>
   * Parser Integer
   * 
   * @return the Parser object.
   */
  public static Parser<Integer> getIndex() {
    return _getIndex;
  }

  /**
   * Retrieves the current index in the source.
   * <p>
   * Parser Integer
   * 
   * @param name
   *          the name of the Parser object.
   * @return the Parser object.
   */
  public static Parser<Integer> getIndex(final String name) {
    return new GetIndexParser(name);
  }

  /**
   * Look ahead with Parser p. The input consumption is undone.
   * <p>
   * Parser a -> Parser a
   * 
   * @param name
   *          the name of the new Parser object.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <R> Parser<R> peek(final String name, final Parser<R> p) {
    return new PeekParser<R>(name, p);
  }

  /**
   * Backout input consumption if p fails. The logical step is ensured to be at
   * most 1.
   * <p>
   * Parser a -> Parser a
   * 
   * @param name
   *          the name of the new Parser object.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <R> Parser<R> atomize(final String name, final Parser<R> p) {
    return new AtomicParser<R>(name, p);
  }

  /**
   * if Parser p throws an exception, it is handled by Catch hdl.
   * <p>
   * Parser a -> Parser a
   * 
   * @param p
   *          the Parser object.
   * @param hdl
   *          the exception handler.
   * @return the new Parser object.
   */
  public static <R> Parser<R> tryParser(final Parser<R> p,
      final Catch<? extends R> hdl) {
    return tryParser("try_catch", p, hdl);
  }

  /**
   * if Parser p throws an exception, it is handled by Catch hdl.
   * <p>
   * Parser a -> Parser a
   * 
   * @param name
   *          the name of the new Parser object.
   * @param p
   *          the Parser object.
   * @param hdl
   *          the exception handler.
   * @return the new Parser object.
   */
  public static <R> Parser<R> tryParser(final String name, final Parser<R> p,
      final Catch<? extends R> hdl) {
    return new TryParser<R>(name, p, hdl);
  }

  /**
   * Transform the return value of Parser p to a different value.
   * <p>
   * Parser a -> (a->b) -> Parser b
   * 
   * @param name
   *          the name of the new Parser object.
   * @param p
   *          the Parser object.
   * @param m
   *          the Map object.
   * @return the new Parser object.
   */
  public static <R, From> Parser<R> map(final String name,
      final Parser<From> p, final Map<? super From, R> m) {
    return new MapParser<R, From>(name, p, m);
  }

  /**
   * Reports an unexpected error.
   * <p>
   * Parser *
   * 
   * @param msg
   *          the error message.
   * @return the new Parser object.
   */
  public static <x> Parser<x> unexpected(final String msg) {
    return unexpected("unexpected", msg);
  }

  /**
   * Reports an unexpected error.
   * <p>
   * Parser *
   * 
   * @param name
   *          the name of the new Parser object.
   * @param msg
   *          the error message.
   * @return the new Parser object.
   */
  public static <x> Parser<x> unexpected(final String name, final String msg) {
    return new UnexpectedParser<x>(name, msg);
  }

  /**
   * Token level parser. checks the current token with the FromToken object. If
   * the fromToken() method returns null, a system unexpected token error
   * occurs; if the method returns anything other than null, the token is
   * consumed and token() succeeds.
   * <p>
   * (SourcePos->Token->a) -> Parser a
   * 
   * @param ft
   *          the FromToken object.
   * @return the new Parser object.
   */
  public static <R> Parser<R> token(final FromToken<R> ft) {
    return token("token", ft);
  }

  /**
   * Token level parser. checks the current token with the FromToken object. If
   * the fromToken() method returns null, a system unexpected token error
   * occurs; if the method returns anything other than null, the token is
   * consumed and token() succeeds.
   * <p>
   * (SourcePos->Object->a) -> Parser a
   * 
   * @param name
   *          the name of the new Parser object.
   * @param ft
   *          the FromToken object.
   * @return the new Parser object.
   */
  public static <R> Parser<R> token(final String name, final FromToken<R> ft) {
    return new IsTokenParser<R>(name, ft);
  }

  /**
   * Token level parser. checks to see if the current token is token t. (using
   * ==). If no, a system unexpected token error occurs; if yes, the token is
   * consumed and token() succeeds. the token is used as the parse result.
   * <p>
   * Object -> Parser SourcePos
   * 
   * @param t
   *          the expected Token object.
   * @return the new Parser object.
   */
  public static Parser<Tok> token(final Object t) {
    return token("token", t);
  }

  /**
   * Token level parser. checks to see if the current token is token t. (using
   * ==). If no, a system unexpected token error occurs; if yes, the token is
   * consumed and token() succeeds. the token is used as the parse result.
   * <p>
   * Token -> Parser SourcePos
   * 
   * @param name
   *          the name of the new Parser object.
   * @param t
   *          the expected Token object.
   * @return the new Parser object.
   */
  public static Parser<Tok> token(final String name, final Object t) {
    // the token has to be singleton
    return token(name, IsToken.instance(t));
  }

  /**
   * if Parser p fails and does not consume input, reports an expecting error
   * with the given label.
   * <p>
   * Parser a -> Parser a
   * 
   * @param name
   *          the name of the new Parser object.
   * @param lbl
   *          the label text.
   * @param p
   *          the Parser object to label.
   * @return the new Parser object.
   */
  public static <R> Parser<R> label(final String name, final String lbl,
      final Parser<R> p) {
    final Parser<R> otherwise = expect(lbl);
    return plus(name, p, otherwise);
    /*
    return new Parser<R>(name) {
      boolean apply(final ParseContext ctxt) {
        final int at = ctxt.getAt();
        final boolean r = p.parse(ctxt);
        if (ctxt.getAt() != at)
          return r;
        if (r)
          return r;
        return setErrorExpecting(lbl, ctxt);
      }
    };*/
  }
  /**
   * Create a Parser object that reports a "something expected" error.
   * @param <x> the result Parser object is good for any result type. 
   * (it does not return anyway)
   * @param name the parser name.
   * @param lbl the label.
   * @return the Parser object.
   * @since version 0.6
   * 
   */
  public static <x> Parser<x> expect(final String name, final String lbl){
    return new ExpectParser<x>(name, lbl);    
  }
  /**
   * Create a Parser object that reports a "something expected" error.
   * @param <x> the result Parser object is good for any result type. 
   * (it does not return anyway)
   * @param lbl the label.
   * @return the Parser object.
   * @since version 0.6
   */
  public static <x> Parser<x> expect(final String lbl){
    return expect("expect", lbl);  
  }
  /**
   * throws a pseudo-exception.
   * <p>
   * Parser *
   * 
   * @param e
   *          the exception object.
   * @return the Parser object.
   */
  public static <R> Parser<R> raise(final Object e) {
    return raise("raise", e);
  }

  /**
   * throws a pseudo-exception.
   * <p>
   * Parser *
   * 
   * @param name
   *          the name of the new Parser object.
   * @param e
   *          the exception object.
   * @return the Parser object.
   */
  public static <R> Parser<R> raise(final String name, final Object e) {
    return new RaiseParser<R>(name, e);
  }

  /**
   * Greedily runs Parser p repeatedly for at least min times and collect the
   * result with the Accumulator object created by Accumulatable. Fails if p
   * fails and consumes some input or if p throws a pseudo-exception.
   * <p>
   * Accumulatable a r -> int -> int -> Parser a -> Parser r
   * 
   * @param name
   *          the name of the new Parser object.
   * @param accm
   *          the Accumulatable object.
   * @param min
   *          the minimum times to repeat.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <From, A extends From, R, To extends R> Parser<R> manyAccum(
      final String name, final Accumulatable<From, To> accm, final int min,
      final Parser<A> p) {
    if (min < 0)
      throw new IllegalArgumentException("min<0");
    return new ManyAccumMinParser<R, From, To, A>(name, accm, min, p);
  }

  /**
   * Greedily runs Parser p repeatedly for 0 or more times. and collect the
   * result with the Accumulator object created by Accumulatable. Fails if p
   * fails and consumes some input or if p throws a pseudo-exception.
   * <p>
   * Accumulatable a r -> int -> int -> Parser a -> Parser r
   * 
   * @param name
   *          the name of the new Parser object.
   * @param accm
   *          the Accumulatable object.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <From, A extends From, R, To extends R> Parser<R> manyAccum(
      final String name, final Accumulatable<From, To> accm, final Parser<A> p) {
    return new ManyAccumParser<R, From, To, A>(name, accm, p);
  }

  /**
   * Greedily runs Parser p repeatedly for at least min times and at most max
   * times, collect the result with the Accumulator object created by
   * Accumulatable. Fails if p fails and consumes some input or if p throws a
   * pseudo-exception.
   * <p>
   * Accumulatable a r -> int -> int -> Parser a -> Parser r
   * 
   * @param name
   *          the name of the new Parser object.
   * @param accm
   *          the Accumulatable object.
   * @param min
   *          the minimum times to repeat.
   * @param max
   *          the maximum times to repeat.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <From, A extends From, R, To extends R> Parser<R> someAccum(
      final String name, final Accumulatable<From, To> accm, final int min,
      final int max, final Parser<A> p) {
    if (min < 0 || max < 0 || min > max)
      throw new IllegalArgumentException();
    return new ManyAccumMinMaxParser<R, From, To, A>(name, max, p, min, accm);
  }

  /**
   * Greedily runs Parser p repeatedly for at most max times, collect the result
   * with the Accumulator object created by Accumulatable. Fails if p fails and
   * consumes some input or if p throws a pseudo-exception.
   * <p>
   * Accumulatable a r -> int -> int -> Parser a -> Parser r
   * 
   * @param name
   *          the name of the new Parser object.
   * @param accm
   *          the Accumulatable object.
   * @param max
   *          the maximum times to repeat.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <From, A extends From, R, To extends R> Parser<R> someAccum(
      final String name, final Accumulatable<From, To> accm, final int max,
      final Parser<A> p) {
    if (max < 0)
      throw new IllegalArgumentException("max<0");
    return new SomeAccumMaxParser<R, From, To, A>(name, max, accm, p);
  }

  private static final Parser<?> _eof = eof("EOF");

  /**
   * Asserts eof is met. Fails otherwise.
   * <p>
   * Parser ?
   * 
   * @return the Parser object.
   */
  public static Parser<?> eof() {
    return _eof;
  }

  /**
   * Asserts eof is met. Fails otherwise.
   * <p>
   * Parser ?
   * 
   * @param msg the error message if eof is not met.
   * @return the Parser object.
   */
  public static Parser<?> eof(final String msg) {
    return eof("eof", msg);
  }

  /**
   * Asserts eof is met. Fails otherwise.
   * <p>
   * Parser ?
   * 
   * @param name the name of the new Parser object.
   * @param msg the error message if eof is not met.
   * @return the Parser object.
   */
  public static Parser<?> eof(final String name, final String msg) {
    return new EofParser(name, msg);
  }

  /**
   * Succeeds if Parser p fails; Fails otherwise. Input consumption is undone.
   * <p>
   * Parser ? -> Parser ?
   * 
   * @param name the name of the new Parser object.
   * @param p the Parser to 'not'
   * @return the new Parser object.
   */
  public static Parser<?> not(final String name, final Parser<?> p) {
    return not(name, p, p.getName());
  }

  /**
   * Succeeds if Parser p fails; Fails otherwise. Input consumption is undone.
   * <p>
   * Parser ? -> Parser ?
   * 
   * @param name the name of the new Parser object.
   * @param p the Parser to 'not'
   * @param errmsg the error message if Parser p succeeds.
   * @return the new Parser object.
   */
  public static Parser<?> not(final String name, final Parser<?> p,
      final String errmsg) {
    return ifelse(name, p.peek(), unexpected(errmsg), one());
  }

  /** ****************** additional combinators ******************* */

  /**
   * Greedily runs Parser p repeatedly for at least min times and discard the
   * results.
   * <p>
   * int -> Parser ? -> Parser _
   * 
   * @param name
   *          the name of the new Parser object.
   * @param min
   *          the minimal times to run.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <R> Parser<_> many(final String name, final int min,
      final Parser<?> p) {
    return new ManyMinParser(name, p, min);
  }

  /**
   * Greedily runs Parser p repeatedly and discard the results.
   * <p>
   * Parser ? -> Parser _
   * 
   * @param name
   *          the name of the new Parser object.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static Parser<_> many(final String name, final Parser<?> p) {
    return new ManyParser(name, p);
  }

  /**
   * Greedily runs Parser p repeatedly for at least min times and collect the
   * result in an array whose element type is etype.
   * <p>
   * Class [a] -> int -> Parser a -> Parser [a]
   * 
   * @param name
   *          the name of the new Parser object.
   * @param etype
   *          the array element type.
   * @param min
   *          the maximal times to run.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <R> Parser<R[]> many(final String name, final Class<R> etype,
      final int min, final Parser<? extends R> p) {
    return many(name, ArrayFactories.typedFactory(etype), min, p);
  }

  /**
   * Greedily runs Parser p repeatedly and collect the result in an array whose
   * element type is etype.
   * <p>
   * Class [a] -> Parser a -> Parser [a]
   * 
   * @param name
   *          the name of the new Parser object.
   * @param etype
   *          the array element type.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <R> Parser<R[]> many(final String name, final Class<R> etype,
      final Parser<? extends R> p) {
    return many(name, ArrayFactories.typedFactory(etype), p);
  }

  /**
   * Greedily runs Parser p repeatedly for at least min times and collect the
   * result in an array created by ArrayFactory object.
   * <p>
   * ArrayFactory a -> int -> Parser a -> Parser [a]
   * 
   * @param name
   *          the name of the new Parser object.
   * @param af
   *          the ArrayFacory object.
   * @param min
   *          the maximal times to run.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <R> Parser<R[]> many(final String name,
      final ArrayFactory<R> af, final int min, final Parser<? extends R> p) {
    return manyAccum(name, getArrayAccumulatable(af), min, p);
  }

  /**
   * Greedily runs Parser p repeatedly and collect the result in an array
   * created by ArrayFactory object.
   * <p>
   * ArrayFactory a -> Parser a -> Parser [a]
   * 
   * @param name
   *          the name of the new Parser object.
   * @param af
   *          the ArrayFacory object.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <R> Parser<R[]> many(final String name,
      final ArrayFactory<R> af, final Parser<? extends R> p) {
    return manyAccum(name, getArrayAccumulatable(af), p);
  }

  /**
   * Greedily runs Parser p repeatedly for at least min times and at most max
   * time. The return values are discarded.
   * <p>
   * int -> int -> Parser ? -> Parser _
   * 
   * @param name
   *          the name of the new Parser object.
   * @param min
   *          the minimal number of times to run.
   * @param max
   *          the maximal number of times to run.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <R> Parser<_> some(final String name, final int min,
      final int max, final Parser<?> p) {
    if (min < 0 || max < 0 || min > max)
      throw new IllegalArgumentException();
    if (max == 0)
      return _retn_unit();
    return new SomeMinMaxParser(name, max, p, min);
  }

  /**
   * Greedily runs Parser p repeatedly for at most max time. The return values
   * are discarded.
   * <p>
   * int -> Parser ? -> Parser _
   * 
   * @param name
   *          the name of the new Parser object.
   * @param max
   *          the maximal number of times to run.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static Parser<_> some(final String name, final int max,
      final Parser<?> p) {
    if (max < 0)
      throw new IllegalArgumentException("max<0");
    if (max == 0)
      return _retn_unit();
    return new SomeMaxParser(name, max, p);
  }

  /**
   * Greedily runs Parser p repeatedly for at least min times and at most max
   * time. The results are collected and returned in an array created by
   * ArrayFactory object.
   * <p>
   * ArrayFactory a -> int -> int -> Parser a -> Parser [a]
   * 
   * @param name
   *          the name of the new Parser object.
   * @param af
   *          the ArrayFacory object.
   * @param min
   *          the minimal number of times to run.
   * @param max
   *          the maximal number of times to run.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <R> Parser<R[]> some(final String name,
      final ArrayFactory<R> af, final int min, final int max,
      final Parser<? extends R> p) {
    return someAccum(name, getArrayAccumulatable(af), min, max, p);
  }

  /**
   * Greedily runs Parser p repeatedly for at most max time. The results are
   * collected and returned in an array created by ArrayFactory object.
   * <p>
   * ArrayFactory a -> int -> Parser a -> Parser [a]
   * 
   * @param name
   *          the name of the new Parser object.
   * @param af
   *          the ArrayFacory object.
   * @param max
   *          the maximal number of times to run.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <R> Parser<R[]> some(final String name,
      final ArrayFactory<R> af, final int max, final Parser<? extends R> p) {
    return someAccum(name, getArrayAccumulatable(af), max, p);
  }

  /**
   * Greedily runs Parser p repeatedly for at least min times and at most max
   * times. The return values are collected and returned in an array whose
   * element type is etype.
   * <p>
   * Class -> int -> int -> Parser a -> Parser [Object]
   * 
   * @param name
   *          the name of the new Parser object.
   * @param etype
   *          the array element type.
   * @param min
   *          the minimal number of times to run.
   * @param max
   *          the maximal number of times to run.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <R> Parser<R[]> some(final String name, Class<R> etype,
      final int min, final int max, final Parser<? extends R> p) {
    return some(name, ArrayFactories.typedFactory(etype), min, max, p);
  }

  /**
   * Greedily runs Parser p repeatedly for at most max times. The return values
   * are collected and returned in an array whose element type is etype.
   * <p>
   * Class -> int -> Parser a -> Parser [Object]
   * 
   * @param name
   *          the name of the new Parser object.
   * @param etype
   *          the array element type.
   * @param max
   *          the maximal number of times to run.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <R> Parser<R[]> some(final String name, Class<R> etype,
      final int max, final Parser<? extends R> p) {
    return some(name, ArrayFactories.typedFactory(etype), max, p);
  }

  /**
   * Run 2 Parsers sequentially and transform the return values to a new value.
   * <p>
   * Parser a->Parser b->(a->b->r)->Parser r
   * 
   * @param p1
   *          1st parser.
   * @param p2
   *          2nd parser.
   * @param m2
   *          the transformer.
   * @return the new Parser object.
   */
  public static <A, B, R> Parser<R> map2(final Parser<A> p1,
      final Parser<B> p2, final Map2<? super A, ? super B, R> m2) {
    return map2("map2", p1, p2, m2);
  }

  /**
   * Run 2 Parsers sequentially and transform the return values to a new value.
   * <p>
   * Parser a->Parser b->(a->b->r)->Parser r
   * 
   * @param name
   *          the name of the new Parser object.
   * @param p1
   *          1st parser.
   * @param p2
   *          2nd parser.
   * @param m2
   *          the transformer.
   * @return the new Parser object.
   */
  public static <A, B, R> Parser<R> map2(final String name, final Parser<A> p1,
      final Parser<B> p2, final Map2<? super A, ? super B, R> m2) {
    return new Map2Parser<R, A, B>(name, m2, p1, p2);
  }

  /**
   * Run 3 Parsers sequentially and transform the return values to a new value.
   * <p>
   * Parser a->Parser b->Parser c->(a->b->c->r)->Parser r
   * 
   * @param p1
   *          1st parser.
   * @param p2
   *          2nd parser.
   * @param p3
   *          3rd parser.
   * @param m3
   *          the transformer.
   * @return the new Parser object.
   */
  public static <A, B, C, R> Parser<R> map3(final Parser<A> p1,
      final Parser<B> p2, final Parser<C> p3,
      final Map3<? super A, ? super B, ? super C, R> m3) {
    return map3("map3", p1, p2, p3, m3);
  }

  /**
   * Run 3 Parsers sequentially and transform the return values to a new value.
   * <p>
   * Parser a->Parser b->Parser c->(a->b->c->r)->Parser r
   * 
   * @param name
   *          the name of the new Parser object.
   * @param p1
   *          1st parser.
   * @param p2
   *          2nd parser.
   * @param p3
   *          3rd parser.
   * @param m3
   *          the transformer.
   * @return the new Parser object.
   */
  public static <A, B, C, R> Parser<R> map3(final String name,
      final Parser<A> p1, final Parser<B> p2, final Parser<C> p3,
      final Map3<? super A, ? super B, ? super C, R> m3) {
    return new Map3Parser<R, A, B, C>(name, m3, p2, p1, p3);
  }

  /**
   * Run 4 Parsers sequentially and transform the return values to a new value.
   * <p>
   * Parser a->Parser b->Parser c->Parser d->(a->b->c->d->r)->Parser r
   * 
   * @param p1
   *          1st parser.
   * @param p2
   *          2nd parser.
   * @param p3
   *          3rd parser.
   * @param p4
   *          4th parser.
   * @param m4
   *          the transformer.
   * @return the new Parser object.
   */
  public static <A, B, C, D, R> Parser<R> map4(final Parser<A> p1,
      final Parser<B> p2, final Parser<C> p3, final Parser<D> p4,
      final Map4<? super A, ? super B, ? super C, ? super D, R> m4) {
    return map4("map4", p1, p2, p3, p4, m4);
  }

  /**
   * Run 4 Parsers sequentially and transform the return values to a new value.
   * <p>
   * Parser a->Parser b->Parser c->Parser d->(a->b->c->d->r)->Parser r
   * 
   * @param name
   *          the name of the new Parser object.
   * @param p1
   *          1st parser.
   * @param p2
   *          2nd parser.
   * @param p3
   *          3rd parser.
   * @param p4
   *          4th parser.
   * @param m4
   *          the transformer.
   * @return the new Parser object.
   */
  public static <A, B, C, D, R> Parser<R> map4(final String name,
      final Parser<A> p1, final Parser<B> p2, final Parser<C> p3,
      final Parser<D> p4,
      final Map4<? super A, ? super B, ? super C, ? super D, R> m4) {
    return new Map4Parser<R, A, B, C, D>(name, m4, p3, p2, p1, p4);
  }

  /**
   * Run 5 Parsers sequentially and transform the return values to a new value.
   * <p>
   * Parser a->Parser b->Parser c->Parser d->Parser
   * e->(a->b->c->d->e->r)->Parser r
   * 
   * @param p1
   *          1st parser.
   * @param p2
   *          2nd parser.
   * @param p3
   *          3rd parser.
   * @param p4
   *          4th parser.
   * @param p5
   *          5th parser.
   * @param m5
   *          the transformer.
   * @return the new Parser object.
   */
  public static <A, B, C, D, E, R> Parser<R> map5(final Parser<A> p1,
      final Parser<B> p2, final Parser<C> p3, final Parser<D> p4,
      final Parser<E> p5,
      final Map5<? super A, ? super B, ? super C, ? super D, ? super E, R> m5) {
    return map5("map5", p1, p2, p3, p4, p5, m5);
  }

  /**
   * Run 5 Parsers sequentially and transform the return values to a new value.
   * <p>
   * Parser a->Parser b->Parser c->Parser d->Parser
   * e->(a->b->c->d->e->r)->Parser r
   * 
   * @param name
   *          the name of the new Parser object.
   * @param p1
   *          1st parser.
   * @param p2
   *          2nd parser.
   * @param p3
   *          3rd parser.
   * @param p4
   *          4th parser.
   * @param p5
   *          5th parser.
   * @param m5
   *          the transformer.
   * @return the new Parser object.
   */
  public static <A, B, C, D, E, R> Parser<R> map5(final String name,
      final Parser<A> p1, final Parser<B> p2, final Parser<C> p3,
      final Parser<D> p4, final Parser<E> p5,
      final Map5<? super A, ? super B, ? super C, ? super D, ? super E, R> m5) {
    return new Map5Parser<R, A, B, C, D, E>(name, p1, p3, p4, p2, m5, p5);
  }

  /**
   * Runs Parser p, if it fails with no input consumed, return default value v
   * instead. <br>
   * plus(p, retn(v))
   * <p>
   * a -> Parser a -> Parser a
   * 
   * @param v
   *          the default value.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <R> Parser<R> option(final R v, final Parser<R> p) {
    return option("option", v, p);
  }

  /**
   * Runs Parser p, if it fails with no input consumed, return default value v
   * instead. <br>
   * plus(name, p, retn(v))
   * <p>
   * a -> Parser a -> Parser a
   * 
   * @param name
   *          the name of the new Parser object.
   * @param v
   *          the default value.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <R> Parser<R> option(final String name, final R v,
      final Parser<R> p) {
    return plus(name, p, retn(v));
  }

  /**
   * Runs Parser p, if it fails with no input consumed, succeed anyway with null as the result. <br>
   * option(p, null)
   * <p>
   * Parser a -> Parser a
   * 
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <R> Parser<R> optional(final Parser<R> p) {
    return optional("optional", p);
  }

  /**
   * Runs Parser p, if it fails with no input consumed, succeed anyway with null as result. <br>
   * option(name, p, null)
   * <p>
   * Parser a -> Parser a
   * 
   * @param name
   *          the name of the new Parser object.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <R> Parser<R> optional(final String name, final Parser<R> p) {
    return plus(name, p, _retn_null);
  }

  /**
   * Runs a Parser that is between a pair of parsers. First run Parser open,
   * then run Parser p, finally run Parser close. The return value of p is
   * preserved as the return value. <br>
   * do {open; x<-p; close; return p}
   * <p>
   * Parser ? -> Parser a -> Parser ? -> Parser a
   * 
   * @param open
   *          the opening parser.
   * @param close
   *          the closing parser.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <R> Parser<R> between(final Parser<?> open,
      final Parser<?> close, final Parser<R> p) {
    return between("between", open, close, p);
  }

  /**
   * runs a Parser that is between a pair of parsers. First run Parser open,
   * then run Parser p, finally run Parser close. The return value of p is
   * preserved as the return value. <br>
   * do {open; x<-p; close; return p}
   * <p>
   * Parser ? -> Parser ? -> Parser a -> Parser a
   * 
   * @param name
   *          the name of the new Parser object.
   * @param open
   *          the opening parser.
   * @param close
   *          the closing parser.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <R> Parser<R> between(final String name, final Parser<?> open,
      final Parser<?> close, final Parser<R> p) {
    return seq(name, open, p.followedBy(close));
  }

  /**
   * run a series of Parser p pattern that is seperated by Parser sep pattern.
   * <p>
   * Parser p has to succeed at least once. <br>
   * For example: pattern "token, token, token, token" is sepBy1(comma, token).
   * <br>
   * The return values are discarded.
   * <p>
   * Parser ? -> Parser ? -> Parser _
   * 
   * @param sep
   *          the seperator.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static Parser<_> sepBy1(final Parser<?> sep, final Parser<?> p) {
    return sepBy1("sepBy1", sep, p);
  }

  /**
   * run a series of Parser p pattern that is seperated by Parser sep pattern.
   * <p>
   * Parser p has to succeed at least once. <br>
   * For example: pattern "token, token, token, token" is sepBy1(comma, token).
   * <br>
   * The return values are discarded.
   * <p>
   * Parser ? -> Parser ? -> Parser _
   * 
   * @param name
   *          the name of the new Parser object.
   * @param sep
   *          the seperator.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static Parser<_> sepBy1(final String name, final Parser<?> sep,
      final Parser<?> p) {
    // return sepBy1(name, ArrayFactories.defaultFactory(), sep, p);
    final Parser<?> sepp = seq(sep, p);
    return seq(name, p, sepp.many());
  }

  /**
   * <p>
   * Class -> Parser a -> Parser [Object]. <br>
   * run a series of Parser p pattern that is seperated by Parser sep pattern.
   * <p>
   * Parser p has to succeed at least once. <br>
   * For example: pattern "token, token, token, token" is sepBy1(comma, token).
   * <br>
   * The return values are collected in an array whose element type is etype.
   * 
   * @param etype
   *          the array element type.
   * @param sep
   *          the seperator.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <R> Parser<R[]> sepBy1(final Class<R> etype,
      final Parser<?> sep, final Parser<? extends R> p) {
    return sepBy1("sepBy1", etype, sep, p);
  }

  /**
   * <p>
   * Class -> Parser a -> Parser [Object]. <br>
   * run a series of Parser p pattern that is seperated by Parser sep pattern.
   * <p>
   * Parser p has to succeed at least once. <br>
   * For example: pattern "token, token, token, token" is sepBy1(comma, token).
   * <br>
   * The return values are collected in an array whose element type is etype.
   * 
   * @param name
   *          the name of the new Parser object.
   * @param etype
   *          the array element type.
   * @param sep
   *          the seperator.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <R> Parser<R[]> sepBy1(final String name, final Class<R> etype,
      final Parser<?> sep, final Parser<? extends R> p) {
    return sepBy1(name, ArrayFactories.typedFactory(etype), sep, p);
  }

  /**
   * run a series of Parser p pattern that is seperated by Parser sep pattern.
   * <p>
   * Parser p has to succeed at least once. <br>
   * For example: pattern "token, token, token, token" is sepBy1(comma, token).
   * <br>
   * The return values are collected in an array created by the ArrayFactory
   * object af.
   * <p>
   * ArrayFactory a -> Parser a -> Parser [a]
   * 
   * @param af
   *          the ArrayFacory object.
   * @param sep
   *          the seperator.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <R> Parser<R[]> sepBy1(final ArrayFactory<R> af,
      final Parser<?> sep, final Parser<? extends R> p) {
    return sepBy1("sepBy1", af, sep, p);
  }

  /**
   * run a series of Parser p pattern that is seperated by Parser sep pattern.
   * <p>
   * Parser p has to succeed at least once. <br>
   * For example: pattern "token, token, token, token" is sepBy1(comma, token).
   * <br>
   * The return values are collected in an array created by the ArrayFactory
   * object af.
   * <p>
   * ArrayFactory a -> Parser a -> Parser [a]
   * 
   * @param name
   *          the name of the new Parser object.
   * @param af
   *          the ArrayFacory object.
   * @param sep
   *          the seperator.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <R, A extends R> Parser<R[]> sepBy1(final String name,
      final ArrayFactory<R> af, final Parser<?> sep, final Parser<A> p) {
    final Parser<A> sepp = seq(sep, p);
    final ToParser<A, R[]> binder = new ToParser<A, R[]>() {
      public Parser<R[]> toParser(final A v) {
        return manyAccum("sepBy1", getArrayAccumulatable(af, v), sepp);
      }
    };
    return bind(name, p, binder);
  }

  /**
   * run a series of Parser p pattern that is seperated by Parser sep pattern.
   * <p>
   * Parser p can succeed 0 or more times. <br>
   * For example: pattern "token, token, token, token" is sepBy(comma, token).
   * <br>
   * The return values are discarded.
   * <p>
   * Parser ? -> Parser ? -> Parser _
   * 
   * @param sep
   *          the seperator.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static Parser<_> sepBy(final Parser<?> sep, final Parser<?> p) {
    return sepBy("sepBy", sep, p);
  }

  /**
   * run a series of Parser p pattern that is seperated by Parser sep pattern.
   * <p>
   * Parser p can succeed 0 or more times. <br>
   * For example: pattern "token, token, token, token" is sepBy(comma, token).
   * <br>
   * The return values are discarded.
   * <p>
   * Parser ? -> Parser ? -> Parser _
   * 
   * @param name
   *          the name of the new Parser object.
   * @param sep
   *          the seperator.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static Parser<_> sepBy(final String name, final Parser<?> sep,
      final Parser<?> p) { 
    return plus(name, sepBy1(sep, p), _retn_unit());
  }

  /**
   * run a series of Parser p pattern that is seperated by Parser sep pattern.
   * <p>
   * Parser p can succeed 0 or more times. <br>
   * For example: pattern "token, token, token, token" is sepBy(comma, token).
   * <br>
   * The return values are collected in an array whose element type is etype.
   * <p>
   * Class -> Parser a -> Parser [Object]
   * 
   * @param etype
   *          the array element type.
   * @param sep
   *          the seperator.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <R> Parser<R[]> sepBy(final Class<R> etype,
      final Parser<?> sep, final Parser<? extends R> p) {
    return sepBy("sepBy", etype, sep, p);
  }

  /**
   * run a series of Parser p pattern that is seperated by Parser sep pattern.
   * <p>
   * Parser p can succeed 0 or more times. <br>
   * For example: pattern "token, token, token, token" is sepBy(comma, token).
   * <br>
   * The return values are collected in an array created by the ArrayFactory
   * object af.
   * <p>
   * ArrayFactory a -> Parser a -> Parser [a]
   * 
   * @param af
   *          the ArrayFacory object.
   * @param sep
   *          the seperator.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <R> Parser<R[]> sepBy(final ArrayFactory<R> af,
      final Parser<?> sep, final Parser<? extends R> p) {
    return sepBy("sepBy", af, sep, p);
  }

  /**
   * run a series of Parser p pattern that is seperated by Parser sep pattern.
   * <p>
   * Parser p can succeed 0 or more times. <br>
   * For example: pattern "token, token, token, token" is sepBy(comma, token).
   * <br>
   * The return values are collected in an array created by the ArrayFactory
   * object af.
   * <p>
   * ArrayFactory a -> Parser a -> Parser [a]
   * 
   * @param name
   *          the name of the new Parser object.
   * @param af
   *          the ArrayFacory object.
   * @param sep
   *          the seperator.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <R> Parser<R[]> sepBy(final String name,
      final ArrayFactory<R> af, final Parser<?> sep, final Parser<? extends R> p) {
    return option(name, af.createArray(0), sepBy1(af, sep, p));
  }

  /**
   * run a series of Parser p pattern that is seperated by Parser sep pattern.
   * <p>
   * Parser p can succeed 0 or more times. <br>
   * For example: pattern "token, token, token, token" is sepBy(comma, token).
   * <br>
   * The return values are collected in an array whose element type is etype.
   * <p>
   * Class -> Parser a -> Parser [Object]
   * 
   * @param name
   *          the name of the new Parser object.
   * @param etype
   *          the array element type.
   * @param sep
   *          the seperator.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <R> Parser<R[]> sepBy(final String name, final Class<R> etype,
      final Parser<?> sep, final Parser<? extends R> p) {
    return sepBy(name, ArrayFactories.typedFactory(etype), sep, p);
  }

  /**
   * First run Parser p, then run Parser sep. The return value of Parser p is
   * preserved as the return value. <br>
   * do{x<-p;sep;return x}.
   * <p>
   * Parser ? -> Parser a -> Parser a
   * 
   * @param name
   *          the name of the new Parser object.
   * @param sep
   *          the following parser.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <R> Parser<R> followedBy(final String name,
      final Parser<?> sep, final Parser<R> p) {
    return new FollowedByParser<R>(name, sep, p);
  }

  /**
   * run a series of Parser p pattern ended by Parser sep pattern.
   * <p>
   * Parser p can succeed 0 or more times. <br>
   * For example: pattern "token; token; token; token;" is endBy(comma, token).
   * <br>
   * The return values are discarded.
   * <p>
   * Class -> Parser ? -> Parser _
   * 
   * @param sep
   *          the end seperator.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static Parser<_> endBy(final Parser<?> sep, final Parser<?> p) {
    return endBy("endBy", sep, p);
  }

  /**
   * run a series of Parser p pattern ended by Parser sep pattern.
   * <p>
   * Parser p can succeed 0 or more times. <br>
   * For example: pattern "token; token; token; token;" is endBy(comma, token).
   * <br>
   * The return values are discarded.
   * <p>
   * Parser ? -> Parser ? -> Parser _
   * 
   * @param name
   *          the name of the new Parser object.
   * @param sep
   *          the end seperator.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static Parser<_> endBy(final String name, final Parser<?> sep,
      final Parser<?> p) {
    return p.followedBy(sep).many(name);
  }

  /**
   * run a series of Parser p pattern ended by Parser sep pattern.
   * <p>
   * Parser p can succeed 0 or more times. <br>
   * For example: pattern "token; token; token; token;" is endBy(comma, token).
   * <br>
   * The return values are collected in an array whose element type is etype.
   * <p>
   * Class -> Parser a -> Parser [Object]
   * 
   * @param etype
   *          array element type.
   * @param sep
   *          the end seperator.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <R> Parser<R[]> endBy(final Class<R> etype,
      final Parser<?> sep, final Parser<? extends R> p) {
    return endBy("endBy", etype, sep, p);
  }

  /**
   * run a series of Parser p pattern ended by Parser sep pattern.
   * <p>
   * Parser p can succeed 0 or more times. <br>
   * For example: pattern "token; token; token; token;" is endBy(comma, token).
   * <br>
   * The return values are discarded.
   * <p>
   * ArrayFactory a -> Parser ? -> Parser a -> Parser a[]
   * 
   * @param af
   *          the ArrayFacory object.
   * @param sep
   *          the end seperator.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <R> Parser<R[]> endBy(final ArrayFactory<R> af,
      final Parser<?> sep, final Parser<? extends R> p) {
    return endBy("endBy", af, sep, p);
  }

  /**
   * run a series of Parser p pattern ended by Parser sep pattern.
   * <p>
   * Parser p can succeed 0 or more times. <br>
   * For example: pattern "token; token; token; token;" is endBy(comma, token).
   * <br>
   * The return values are collected in an array whose element type is etype.
   * <p>
   * Class -> Parser a -> Parser [Object]
   * 
   * @param name
   *          the name of the new Parser object.
   * @param etype
   *          array element type.
   * @param sep
   *          the end seperator.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <R> Parser<R[]> endBy(final String name, final Class<R> etype,
      final Parser<?> sep, final Parser<? extends R> p) {
    return many(name, etype, p.followedBy(sep));
  }

  /**
   * run a series of Parser p pattern ended by Parser sep pattern.
   * <p>
   * Parser p can succeed 0 or more times. <br>
   * For example: pattern "token; token; token; token;" is endBy(comma, token).
   * <br>
   * The return values are collected in an array created by the ArrayFactory
   * object af.
   * <p>
   * ArrayFactory a -> Parser a -> Parser [a]
   * 
   * @param name
   *          the name of the new Parser object.
   * @param af
   *          the ArrayFacory object.
   * @param sep
   *          the end seperator.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <R> Parser<R[]> endBy(final String name,
      final ArrayFactory<R> af, final Parser<?> sep, final Parser<? extends R> p) {
    return many(name, af, p.followedBy(sep));
  }

  /**
   * run a series of Parser p pattern ended by Parser sep pattern.
   * <p>
   * Parser p should succeed for at least once. <br>
   * For example: pattern "token; token; token; token;" is endBy(comma, token).
   * <br>
   * The return values are discarded.
   * <p>
   * Parser ? -> Parser ? -> Parser _
   * 
   * @param sep
   *          the end seperator.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static Parser<_> endBy1(final Parser<?> sep, final Parser<?> p) {
    return endBy1("endBy1", sep, p);
  }

  /**
   * run a series of Parser p pattern ended by Parser sep pattern.
   * <p>
   * Parser p should succeed for at least once. <br>
   * For example: pattern "token; token; token; token;" is endBy(comma, token).
   * <br>
   * The return values are discarded.
   * <p>
   * Parser ? -> Parser ? -> Parser _
   * 
   * @param name
   *          the name of the new Parser object.
   * @param sep
   *          the end seperator.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static Parser<_> endBy1(final String name, final Parser<?> sep,
      final Parser<?> p) {
    return p.followedBy(sep).many1(name);
  }

  /**
   * run a series of Parser p pattern ended by Parser sep pattern.
   * <p>
   * Parser p should succeed for at least once. <br>
   * For example: pattern "token; token; token; token;" is endBy(comma, token).
   * <br>
   * The return values are collected in an array whose element type is etype.
   * <p>
   * Class -> Parser a -> Parser [Object]
   * 
   * @param etype
   *          array element type.
   * @param sep
   *          the end seperator.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <R> Parser<R[]> endBy1(final Class<R> etype,
      final Parser<?> sep, final Parser<? extends R> p) {
    return endBy1("endBy1", etype, sep, p);
  }

  /**
   * run a series of Parser p pattern ended by Parser sep pattern.
   * <p>
   * Parser p should succeed for at least once. <br>
   * For example: pattern "token; token; token; token;" is endBy1(comma, token).
   * <br>
   * The return values are collected in an array created by the ArrayFactory
   * object af.
   * <p>
   * ArrayFactory a -> Parser a -> Parser [a]
   * 
   * @param af
   *          the ArrayFacory object.
   * @param sep
   *          the end seperator.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <R> Parser<R[]> endBy1(final ArrayFactory<R> af,
      final Parser<?> sep, final Parser<? extends R> p) {
    return endBy1("endBy1", af, sep, p);
  }

  /**
   * run a series of Parser p pattern ended by Parser sep pattern.
   * <p>
   * Parser p should succeed for at least once. <br>
   * For example: pattern "token; token; token; token;" is endBy1(comma, token).
   * <br>
   * The return values are collected in an array created by the ArrayFactory
   * object af.
   * <p>
   * ArrayFactory a -> Parser a -> Parser [a]
   * 
   * @param name
   *          the name of the new Parser object.
   * @param af
   *          the ArrayFacory object.
   * @param sep
   *          the end seperator.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <R> Parser<R[]> endBy1(final String name,
      final ArrayFactory<R> af, final Parser<?> sep, final Parser<? extends R> p) {
    return many(name, af, 1, p.followedBy(sep));
  }

  /**
   * run a series of Parser p pattern ended by Parser sep pattern.
   * <p>
   * Parser p should succeed for at least once. <br>
   * For example: pattern "token; token; token; token;" is endBy(comma, token).
   * <br>
   * The return values are collected in an array whose element type is etype.
   * <p>
   * Class -> Parser a -> Parser [Object]
   * 
   * @param name
   *          the name of the new Parser object.
   * @param etype
   *          array element type.
   * @param sep
   *          the end seperator.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <R> Parser<R[]> endBy1(final String name, final Class<R> etype,
      final Parser<?> sep, final Parser<? extends R> p) {
    return many(name, etype, 1, p.followedBy(sep));
  }

  /**
   * run a series of Parser p pattern that is seperated and optionally ended by
   * Parser sep pattern.
   * <p>
   * Parser p may succeed 0 or more times. <br>
   * For example: patterns "token; token; token; token" and "token;" are both
   * sepEndBy(semicolon, token). <br>
   * The return values are discarded.
   * <p>
   * Parser ? -> Parser ? -> Parser _
   * 
   * @param sep
   *          the end seperator.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static Parser<_> sepEndBy(final Parser<?> sep, final Parser<?> p) {
    return sepEndBy("sepEndBy", sep, p);
  }

  /**
   * run a series of Parser p pattern that is seperated and optionally ended by
   * Parser sep pattern.
   * <p>
   * Parser p may succeed 0 or more times. <br>
   * For example: patterns "token; token; token; token" and "token;" are both
   * sepEndBy(semicolon, token). <br>
   * The return values are collected in an array whose element type is etype.
   * <p>
   * Class -> Parser a -> Parser [Object]
   * 
   * @param etype
   *          the array element type.
   * @param sep
   *          the end seperator.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <R> Parser<R[]> sepEndBy(final Class<R> etype,
      final Parser<?> sep, final Parser<? extends R> p) {
    return sepEndBy("sepEndBy", etype, sep, p);
  }

  /**
   * run a series of Parser p pattern that is seperated and optionally ended by
   * Parser sep pattern.
   * <p>
   * Parser p may succeed 0 or more times. <br>
   * For example: patterns "token; token; token; token" and "token;" are both
   * sepEndBy(semicolon, token). <br>
   * The return values are collected in an array created by the ArrayFactory
   * object af.
   * <p>
   * ArrayFactory a -> Parser a -> Parser [a]
   * 
   * @param af
   *          the ArrayFacory object.
   * @param sep
   *          the end seperator.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <R> Parser<R[]> sepEndBy(final ArrayFactory<R> af,
      final Parser<?> sep, final Parser<? extends R> p) {
    return sepEndBy("sepEndBy", af, sep, p);
  }

  /**
   * run a series of Parser p pattern that is seperated and optionally ended by
   * Parser sep pattern.
   * <p>
   * Parser p may succeed 0 or more times. <br>
   * For example: patterns "token; token; token; token" and "token;" are both
   * sepEndBy(semicolon, token). <br>
   * The return values are discarded.
   * <p>
   * Parser ? -> Parser ? -> Parser _
   * 
   * @param name
   *          the name of the new Parser object.
   * @param sep
   *          the end seperator.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static Parser<_> sepEndBy(final String name, final Parser<?> sep,
      final Parser<?> p) {
    return plus(name, sepEndBy1(sep, p), _retn_unit());
  }

  /**
   * run a series of Parser p pattern that is seperated and optionally ended by
   * Parser sep pattern.
   * <p>
   * Parser p may succeed 0 or more times. <br>
   * For example: patterns "token; token; token; token" and "token;" are both
   * sepEndBy(semicolon, token). <br>
   * The return values are collected in an array whose element type is etype.
   * <p>
   * Class -> Parser a -> Parser [Object]
   * 
   * @param name
   *          the name of the new Parser object.
   * @param etype
   *          the array element type.
   * @param sep
   *          the end seperator.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <R> Parser<R[]> sepEndBy(final String name,
      final Class<R> etype, final Parser<?> sep, final Parser<? extends R> p) {
    return sepEndBy(name, ArrayFactories.typedFactory(etype), sep, p);
  }

  /**
   * run a series of Parser p pattern that is seperated and optionally ended by
   * Parser sep pattern.
   * <p>
   * Parser p may succeed 0 or more times. <br>
   * For example: patterns "token; token; token; token" and "token;" are both
   * sepEndBy(semicolon, token). <br>
   * The return values are collected in an array created by the ArrayFactory
   * object af.
   * <p>
   * ArrayFactory a -> Parser a -> Parser [a]
   * 
   * @param name
   *          the name of the new Parser object.
   * @param af
   *          the ArrayFacory object.
   * @param sep
   *          the end seperator.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <R> Parser<R[]> sepEndBy(final String name,
      final ArrayFactory<R> af, final Parser<?> sep, final Parser<? extends R> p) {
    return option(name, af.createArray(0), sepEndBy1(af, sep, p));
  }

  /**
   * run a series of Parser p pattern that is seperated and optionally ended by
   * Parser sep pattern.
   * <p>
   * Parser p should succeed at least once. <br>
   * For example: patterns "token; token; token; token" and "token;" are both
   * sepEndBy1(semicolon, token). <br>
   * The return values are discarded.
   * <p>
   * Parser ? -> Parser ? -> Parser _
   * 
   * @param sep
   *          the end seperator.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static Parser<_> sepEndBy1(final Parser<?> sep, final Parser<?> p) {
    return sepEndBy1("sepEndBy1", sep, p);
  }

  /**
   * run a series of Parser p pattern that is seperated and optionally ended by
   * Parser sep pattern.
   * <p>
   * Parser p should succeed at least once. <br>
   * For example: patterns "token; token; token; token" and "token;" are both
   * sepEndBy1(semicolon, token). <br>
   * The return values are discarded.
   * <p>
   * Parser ? -> Parser ? -> Parser _
   * 
   * @param name
   *          the name of the new Parser object.
   * @param sep
   *          the end seperator.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static Parser<_> sepEndBy1(final String name, final Parser<?> sep,
      final Parser<?> p) {
    // return sepEndBy1(name, ArrayFactories.defaultFactory(), sep, p);
    final _ end = new _();
    final Catch<_> catcher = new Catch1<_>(end);
    final Parser<?> x = seq(sep, sum(p, raise(end)));
    return seq(p, tryParser(x.many(name), catcher));
  }

  /**
   * run a series of Parser p pattern that is seperated and optionally ended by
   * Parser sep pattern.
   * <p>
   * Parser p should succeed at least once. <br>
   * For example: patterns "token; token; token; token" and "token;" are both
   * sepEndBy1(semicolon, token). <br>
   * The return values are collected in an array whose element type is etype.
   * <p>
   * Class -> Parser a -> Parser [Object]
   * 
   * @param etype
   *          the array element type.
   * @param sep
   *          the end seperator.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <R> Parser<R[]> sepEndBy1(final Class<R> etype,
      final Parser<?> sep, final Parser<? extends R> p) {
    return sepEndBy1("sepEndBy1", etype, sep, p);
  }

  /**
   * run a series of Parser p pattern that is seperated and optionally ended by
   * Parser sep pattern.
   * <p>
   * Parser p should succeed at least once. <br>
   * For example: patterns "token; token; token; token" and "token;" are both
   * sepEndBy1(semicolon, token). <br>
   * The return values are collected in an array created by the ArrayFactory
   * object af.
   * <p>
   * ArrayFactory a -> Parser a -> Parser [a]
   * 
   * @param af
   *          the ArrayFacory object.
   * @param sep
   *          the end seperator.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <R> Parser<R[]> sepEndBy1(final ArrayFactory<R> af,
      final Parser<?> sep, final Parser<? extends R> p) {
    return sepEndBy1("sepEndBy1", af, sep, p);
  }

  /**
   * run a series of Parser p pattern that is seperated and optionally ended by
   * Parser sep pattern.
   * <p>
   * Parser p should succeed at least once. <br>
   * For example: patterns "token; token; token; token" and "token;" are both
   * sepEndBy1(semicolon, token). <br>
   * The return values are collected in an array whose element type is etype.
   * <p>
   * Class -> Parser a -> Parser [Object]
   * 
   * @param name
   *          the name of the new Parser object.
   * @param etype
   *          the array element type.
   * @param sep
   *          the end seperator.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <R> Parser<R[]> sepEndBy1(final String name,
      final Class<R> etype, final Parser<?> sep, final Parser<? extends R> p) {
    return sepEndBy1(name, ArrayFactories.typedFactory(etype), sep, p);
  }

  /**
   * run a series of Parser p pattern that is seperated and optionally ended by
   * Parser sep pattern.
   * <p>
   * Parser p should succeed at least once. <br>
   * For example: patterns "token; token; token; token" and "token;" are both
   * sepEndBy1(semicolon, token). <br>
   * The return values are collected in an array created by the ArrayFactory
   * object af.
   * <p>
   * ArrayFactory a -> Parser ? -> Parser a -> Parser [a]
   * 
   * @param name
   *          the name of the new Parser object.
   * @param af
   *          the ArrayFacory object.
   * @param sep
   *          the end seperator.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <R> Parser<R[]> sepEndBy1(final String name,
      final ArrayFactory<R> af, final Parser<?> sep, final Parser<? extends R> p) {
    /*
     * final Object end = new Object(); final Catch catcher = new Catch1(end);
     * final Parser x = seq(sep, plus(p, raise(end))); return bind(name, p, new
     * ToParser(){ public Parser toParser(final Object v){ final Parser rep =
     * manyAccum(name, getArrayAccumulatable(af, v), x); return tryParser(rep,
     * catcher); } });
     */
    return bind(name, p, new ToParser<R, R[]>() {
      public Parser<R[]> toParser(final R v) {
        return delimitedBy(name, getArrayAccumulatable(af, v), sep, p);
      }
    });
  }

  private static <E1, E extends E1, R, R1 extends R> Parser<R> delimitedBy(
      final String name, final Accumulatable<E1, R1> accm,
      final Parser<?> delim, final Parser<E> p) {
    return new DelimitedByParser<R, E1, R1, E>(name, p, accm, delim);
  }

  /**
   * Runs Parser p for n times. Return values are discarded.
   * <p>
   * int -> Parser ? -> Parser _
   * 
   * @param name
   *          the name of the new Parser object.
   * @param n
   *          the number of times to run.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static Parser<_> repeat(final String name, final int n,
      final Parser<?> p) {
    return new RepeatParser(name, p, n);
  }

  /**
   * Runs Parser p for n times, collect the return values in an array whose
   * element type is etype.
   * <p>
   * Class -> int -> Parser a -> Parser [Object]
   * 
   * @param name
   *          the name of the new Parser object.
   * @param etype
   *          the array element type.
   * @param n
   *          the number of times to run.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <R> Parser<R[]> repeat(final String name, final Class<R> etype,
      final int n, final Parser<? extends R> p) {
    return repeat(name, ArrayFactories.typedFactory(etype), n, p);
  }

  /**
   * Runs Parser p for n times, collect the return values in an array created by
   * the ArrayFactory object.
   * <p>
   * ArrayFactory a -> int -> Parser a -> Parser [a]
   * 
   * @param name
   *          the name of the new Parser object.
   * @param af
   *          the ArrayFactory object.
   * @param n
   *          the number of times to run.
   * @param p
   *          the Parser object.
   * @return the new Parser object.
   */
  public static <R> Parser<R[]> repeat(final String name,
      final ArrayFactory<R> af, final int n, final Parser<? extends R> p) {
    return new RepeatArrayParser<R>(name, af, n, p);
  }

  /**
   * Runs Parser op for 0 or more times greedily. Then run Parser p. The Map
   * object returned from op are applied from right to left to the return value
   * of p. <br>
   * prefix(op, p) is equivalent to op* p in EBNF
   * <p>
   * Parser (a->a) -> Parser a -> Parser a
   * 
   * @param op
   *          the operator.
   * @param p
   *          the operand.
   * @return the new Parser object.
   */
  public static <T> Parser<T> prefix(
      final Parser<? extends Map<? super T, T>> op, final Parser<? extends T> p) {
    return prefix("prefix", op, p);
  }

  private static <T> T postfix_thread_maps(T a, final Map<? super T, T>[] ms) {
    for (int i = 0; i < ms.length; i++) {
      final Map<? super T, T> m = ms[i];
      a = m.map(a);
    }
    return a;
  }

  private static <T> T prefix_thread_maps(T a, final Map<? super T, T>[] ms) {
    for (int i = ms.length - 1; i >= 0; i--) {
      final Map<? super T, T> m = ms[i];
      a = m.map(a);
    }
    return a;
  }

  private static final Map2 _prefix_map2 = _get_prefix_map2();

  private static <T> Map2<Map<? super T, T>[], T, T> _get_prefix_map2() {
    return new Map2<Map<? super T, T>[], T, T>() {
      public T map(final Map<? super T, T>[] ops, final T a) {
        return prefix_thread_maps(a, ops);
      }
    };
  }

  /**
   * Runs Parser op for 0 or more times greedily. Then run Parser p. The Map
   * object returned from op are applied from right to left to the return value
   * of p. <br>
   * prefix(op, p) is equivalent to op* p in EBNF
   * <p>
   * Parser (a->a) -> Parser a -> Parser a
   * 
   * @param name
   *          the name of the new Parser object.
   * @param op
   *          the operator.
   * @param p
   *          the operand.
   * @return the new Parser object.
   */
  public static <T> Parser<T> prefix(final String name,
      final Parser<? extends Map<? super T, T>> op, final Parser<? extends T> p) {
    final Parser<Map> _op = op.convert();// because Map<...>.class is not
                                          // supported.
    return map2(name, _op.many("prefix", Map.class), p, _prefix_map2);
  }

  /**
   * Runs Parser p and then run Parser op for 0 or more times greedily. The Map
   * object returned from op are applied from left to right to the return value
   * of p. <br>
   * postfix(op, p) is equivalent to p op* in EBNF
   * <p>
   * Parser (a->a) -> Parser a -> Parser a
   * 
   * @param op
   *          the operator.
   * @param p
   *          the operand.
   * @return the new Parser object.
   */
  public static <T> Parser<T> postfix(
      final Parser<? extends Map<? super T, T>> op, final Parser<? extends T> p) {
    return postfix("postfix", op, p);
  }

  private static final Map2 _postfix_map2 = _get_postfix_map2();

  private static <T> Map2<T, Map<? super T, T>[], T> _get_postfix_map2() {
    return new Map2<T, Map<? super T, T>[], T>() {
      public T map(final T a, final Map<? super T, T>[] ops) {
        return postfix_thread_maps(a, ops);
      }
    };
  }

  /**
   * Runs Parser p and then run Parser op for 0 or more times greedily. The Map
   * object returned from op are applied from left to right to the return value
   * of p. <br>
   * postfix(op, p) is equivalent to p op* in EBNF
   * <p>
   * Parser (a->a) -> Parser a -> Parser a
   * 
   * @param name
   *          the name of the new Parser object.
   * @param op
   *          the operator.
   * @param p
   *          the operand.
   * @return the new Parser object.
   */
  public static <T> Parser<T> postfix(final String name,
      final Parser<? extends Map<? super T, T>> op, final Parser<? extends T> p) {
    final Parser<Map> _op = op.convert();
    final Parser ops = _op.many(name, Map.class);
    return map2(name, p, ops, _postfix_map2);
  }

  /**
   * Non-associative infix operator. Runs Parser p and then run Parser op and
   * Parser p optionally. The Map2 object returned from op is applied to the
   * return values of the two p pattern, if any. <br>
   * infixn(op, p) is equivalent to p (op p)? in EBNF
   * <p>
   * Parser (a->a->a) -> Parser a -> Parser a
   * 
   * @param op
   *          the operator.
   * @param operand
   *          the operand.
   * @return the new Parser object.
   */
  public static <T> Parser<T> infixn(
      final Parser<? extends Map2<? super T, ? super T, T>> op,
      final Parser<? extends T> operand) {
    return infixn("infixn", op, operand);
  }

  /**
   * Non-associative infix operator. Runs Parser p and then run Parser op and
   * Parser p optionally. The Map2 object returned from op is applied to the
   * return values of the two p pattern, if any. <br>
   * infixn(op, p) is equivalent to p (op p)? in EBNF
   * <p>
   * Parser (a->a->a) -> Parser a -> Parser a
   * 
   * @param name
   *          the name of the new Parser object.
   * @param op
   *          the operator.
   * @param operand
   *          the operand.
   * @return the new Parser object.
   */
  public static <T> Parser<T> infixn(final String name,
      final Parser<? extends Map2<? super T, ? super T, T>> op,
      final Parser<? extends T> operand) {
    return operand.bind(name, new ToParser<T, T>() {
      public Parser<T> toParser(final T a) {
        final Parser<T> shift = op.and(operand,
            new Map2<Map2<? super T, ? super T, T>, T, T>() {
              public T map(final Map2<? super T, ? super T, T> m2, final T b) {
                return m2.map(a, b);
              }
            });
        return Parsers.plus(shift, retn(a));
      }
    });
  }

  /**
   * Left associative infix operator. Runs Parser p and then run Parser op and
   * Parser p for 0 or more times greedily. The Map object returned from op are
   * applied from left to right to the return value of p. <br>
   * for example: a+b+c+d is evaluated as (((a+b)+c)+d). <br>
   * infixl(op, p) is equivalent to p (op p)* in EBNF.
   * <p>
   * Parser (a->a->a) -> Parser a -> Parser a
   * 
   * @param op
   *          the operator.
   * @param p
   *          the operand.
   * @return the new Parser object.
   */
  public static <T> Parser<T> infixl(
      final Parser<? extends Map2<? super T, ? super T, T>> op,
      final Parser<? extends T> p) {
    return infixl("infixl", op, p);
  }

  /**
   * Left associative infix operator. Runs Parser p and then run Parser op and
   * Parser p for 0 or more times greedily. The Map object returned from op are
   * applied from left to right to the return values of p. <br>
   * for example: a+b+c+d is evaluated as (((a+b)+c)+d). <br>
   * infixl(op, p) is equivalent to p (op p)* in EBNF.
   * <p>
   * Parser (a->a->a) -> Parser a -> Parser a
   * 
   * @param name
   *          the name of the new Parser object.
   * @param op
   *          the operator.
   * @param p
   *          the operand.
   * @return the new Parser object.
   */
  public static <T> Parser<T> infixl(final String name,
      final Parser<? extends Map2<? super T, ? super T, T>> op,
      final Parser<? extends T> p) {
    final Parser op_and_rhs = op_rhs(op, p);
    return bind(name, p, new ToParser<T, T>() {
      public Parser<T> toParser(final T a) {
        return manyAccum("infixl", lassocAccumulatable(a), op_and_rhs);
      }
    });
    // return postfix(name, op_rhs(op, p), p); //this uses a temporary array.
  }

  /**
   * Right associative infix operator. Runs Parser p and then run Parser op and
   * Parser p for 0 or more times greedily. The Map object returned from op are
   * applied from right to left to the return values of p. <br>
   * for example: a+b+c+d is evaluated as a+(b+(c+d)). <br>
   * infixr(op, p) is equivalent to p (op p)* in EBNF.
   * <p>
   * Parser (a->a->a) -> Parser a -> Parser a
   * 
   * @param op
   *          the operator.
   * @param p
   *          the operand.
   * @return the new Parser object.
   */
  public static <T> Parser<T> infixr(
      final Parser<? extends Map2<? super T, ? super T, T>> op,
      final Parser<? extends T> p) {
    return infixr("infixr", op, p);
  }

  // 1+ 1+ 1+ ..... 1
  private static final class Rhs<T> {
    final Map2<? super T, ? super T, T> op;

    final T rhs;

    Rhs(final Map2<? super T, ? super T, T> op, final T rhs) {
      this.op = op;
      this.rhs = rhs;
    }
  }

  private static final Map2 _infixr_map2 = _get_infixr_map2();

  private static <T> Map2<Map2<? super T, ? super T, T>, T, Rhs<T>> _get_infixr_map2() {
    return new Map2<Map2<? super T, ? super T, T>, T, Rhs<T>>() {
      public Rhs<T> map(final Map2<? super T, ? super T, T> m2, final T b) {
        return new Rhs<T>(m2, b);
      }
    };
  }

  private static final Map2 _calc_infixr = _get_calc_infixr();

  private static final <T> Map2<T, Rhs<T>[], T> _get_calc_infixr() {
    return new Map2<T, Rhs<T>[], T>() {
      public T map(final T a, final Rhs<T>[] rhss) {
        if (rhss.length == 0)
          return a;
        T o2 = rhss[rhss.length - 1].rhs;
        for (int i = rhss.length - 1; i > 0; i--) {
          final Map2<? super T, ? super T, T> m2 = rhss[i].op;
          final T o1 = rhss[i - 1].rhs;
          o2 = m2.map(o1, o2);
        }
        return rhss[0].op.map(a, o2);
      }
    };
  }

  /**
   * Right associative infix operator. Runs Parser p and then run Parser op and
   * Parser p for 0 or more times greedily. The Map object returned from op are
   * applied from right to left to the return values of p. <br>
   * for example: a+b+c+d is evaluated as a+(b+(c+d)). <br>
   * infixr(op, p) is equivalent to p (op p)* in EBNF.
   * <p>
   * Parser (a->a->a) -> Parser a -> Parser a
   * 
   * @param name
   *          the name of the new Parser object.
   * @param op
   *          the operator.
   * @param p
   *          the operand.
   * @return the new Parser object.
   */
  public static <T> Parser<T> infixr(final String name,
      final Parser<? extends Map2<? super T, ? super T, T>> op,
      final Parser<? extends T> p) {
    final Parser<Rhs> op_rhs2 = map2(op, p, _infixr_map2);
    final Parser<Rhs[]> tmp = op_rhs2.many(name, Rhs.class);// doesn't like
                                                            // Rhs<T>.class?
    return map2(p, tmp, _calc_infixr);
  }

  /**
   * Consumes a token. The token is used as the return value of the parser.
   * <p>
   * Parser Token
   * 
   * @return the Parser object.
   */
  public static Parser<Tok> anyToken() {
    return anyToken("anyToken");
  }

  private static final FromToken<Tok> _any_tok = new FromToken<Tok>() {
    public Tok fromToken(final Tok tok) {
      return tok;
    }
  };

  /**
   * Consumes a token. The {@link Tok} is used as the return value
   * of the parser.
   * <p>
   * Parser Tok
   * 
   * @param name
   *          the name of the Parser object.
   * @return the Parser object.
   */
  public static Parser<Tok> anyToken(final String name) {
    return token(name, _any_tok);
  }

  private static <T> Accumulatable<Map<? super T, ? extends T>, T> lassocAccumulatable(
      final T init) {
    return new Accumulatable<Map<? super T, ? extends T>, T>() {
      public Accumulator<Map<? super T, ? extends T>, T> getAccumulator() {
        return new Accumulator<Map<? super T, ? extends T>, T>() {
          private T a = init;

          public void accumulate(final Map<? super T, ? extends T> m) {
            a = m.map(a);
          }

          public T getResult() {
            return a;
          }
        };
      }
    };
  }

  private static final Map2 _op_rhs_map2 = _getOperatorRhsMap2();

  private static <A, B, R> Map2<Map2<A, B, R>, B, Map<A, R>> _getOperatorRhsMap2() {
    return new Map2<Map2<A, B, R>, B, Map<A, R>>() {
      public Map<A, R> map(final Map2<A, B, R> op, final B b) {
        return new Map<A, R>() {
          public R map(final A a) {
            return op.map(a, b);
          }
        };
      }
    };
  }

  // Parser Map
  private static <B, R, Op extends Map2<? super B, ? super B, R>> Parser/* <Map<A,R>> */op_rhs(
      final Parser<Op> op, final Parser<B> rhs) {
    return map2(op, rhs, _op_rhs_map2);
  }

  private static <From> boolean runToParser(final ToParser<? super From, ?> p,
      final ParseContext state) {
    return p.toParser((From) state.getReturn()).parse(state);
  }

  private static <From> boolean runNext(final ParseContext s,
      final ToParser<? super From, ?> p) {
    return runToParser(p, s);
  }

  private static <E, T extends E> Accumulatable<T, E[]> getArrayAccumulatable(
      final ArrayFactory<E> af) {
    return new Accumulatable<T, E[]>() {
      public Accumulator<T, E[]> getAccumulator() {
        return new ArrayAccumulator<E, T>(af, new java.util.ArrayList<T>());
      }
    };
  }

  private static <E, T extends E> Accumulatable<T, E[]> getArrayAccumulatable(
      final ArrayFactory<E> af, final T init) {
    return new Accumulatable<T, E[]>() {
      public Accumulator<T, E[]> getAccumulator() {
        final java.util.ArrayList<T> a = new java.util.ArrayList<T>();
        a.add(init);
        return new ArrayAccumulator<E, T>(af, a);
      }
    };
  }

  private static final Object[] _empty_array = new Object[0];
  private static final Parser _retn_null = retn(null);
  private static final Parser<_> _retn_unit(){
    return _retn_null;
  }
}
