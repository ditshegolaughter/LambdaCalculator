/*****************************************************************************
 * Copyright (C) Zephyr Business Solutions Corp. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/
/*
 * Created on Dec 17, 2004
 *
 * Author Ben Yu
 */
package jfun.parsec;

import jfun.parsec.pattern.CharPredicate;
import jfun.parsec.pattern.CharPredicates;
import jfun.parsec.pattern.Pattern;
import jfun.parsec.pattern.Patterns;


/**
 * Scanners class provides basic character level parsers
 * and the combinators that only work on scanners.
 * <p>
 * All scanners return null. "_" is used as type parameter to indicate this.
 * <p>
 * @author Ben Yu
 *
 * Dec 17, 2004
 */
public final class Scanners {

  /**
   * Scans greedily for 0 or more characters
   * that satisfies the given CharPredicate.
   * @param cp the predicate object.
   * @return the Parser object.
   */
  public static Parser<_> many(CharPredicate cp){return many("many", cp);}
  /**
   * Scans greedily for 1 or more characters
   * that satisfies the given CharPredicate.
   * @param cp the predicate object.
   * @return the Parser object.
   */
  public static Parser<_> many1(CharPredicate cp){return many1("many1", cp);}
  /**
   * Scans greedily for 0 or more occurrences of the given pattern.
   * @param pp the pattern object.
   * @return the Parser object.
   */
  public static Parser<_> many(Pattern pp){return many("many", pp);}
  /**
   * Scans greedily for 1 or more occurrences of the given pattern.
   * @param pp the pattern object.
   * @return the Parser object.
   */
  public static Parser<_> many1(Pattern pp){return many1("many1", pp);}
  /**
   * Scans greedily for 0 or more characters
   * that satisfies the given CharPredicate.
   * @param name the name of the Parser object.
   * @param cp the predicate object.
   * @return the Parser object.
   */
  public static Parser<_> many(final String name, final CharPredicate cp){
    return isPattern(name, Patterns.many(cp), "many");
  }
  /**
   * Scans greedily for 1 or more characters
   * that satisfies the given CharPredicate.
   * @param name the name of the Parser object.
   * @param cp the predicate object.
   * @return the Parser object.
   */
  public static Parser<_> many1(final String name, final CharPredicate cp){
    return isPattern(name, Patterns.many1(cp), "many1");
  }
  /**
   * Scans greedily for 0 or more occurrences of the given pattern.
   * @param name the name of the Parser object.
   * @param pp the pattern object.
   * @return the Parser object.
   */
  public static Parser<_> many(final String name, final Pattern pp){
    return isPattern(name, pp.many(), "many");
  }
  /**
   * Scans greedily for 1 or more occurrences of the given pattern.
   * @param name the name of the Parser object.
   * @param pp the pattern object.
   * @return the Parser object.
   */
  public static Parser<_> many1(final String name, final Pattern pp){
    return isPattern(name, pp.many1(), "many1");
  }

  /**
   * matches the input against the specified string.
   * @param str the string to match
   * @return the scanner.
   */
  public static Parser<_> isString(final String str){
    return isString(str, str);
  }
  /**
   * matches the input against the specified string.
   * @param str the string to match
   * @param err the error message if fails.
   * @return the scanner.
   */
  public static Parser<_> isString(final String str, final String err){
    return isString("=="+str, str, err);
  }
  /**
   * matches the input against the specified string.
   * @param name the scanner name.
   * @param str the string to match
   * @param err the error message if fails.
   * @return the scanner.
   */
  public static Parser<_> isString(final String name, final String str, final String err){
    return isPattern(name, Patterns.isString(str), err);
  }
  /**
   * Scans greedily for 1 or more whitespace characters.
   * @return the Parser object.
   */
  public static Parser<_> isWhitespaces(){
    return isWhitespaces("isWhitespaces");
  }
  /**
   * Scans greedily for 1 or more whitespace characters.
   * @param name the Parser object name.
   * @return the Parser object.
   */
  public static Parser<_> isWhitespaces(final String name){
    return isWhitespaces(name, "whitespaces");
  }
  /**
   * Scans greedily for 1 or more whitespace characters.
   * @param name the Parser object name.
   * @param err the expected message when fails.
   * @return the Parser object.
   */
  public static Parser<_> isWhitespaces(final String name, final String err){
    return isPattern(name, Patterns.many1(CharPredicates.isWhitespace()), err);
  }
  /**
   * Scans the input for an occurrence of a string pattern.
   * @param pp the pattern object.
   * @param err the expected message when fails.
   * @return the Parser object.
   */
  public static Parser<_> isPattern(final Pattern pp, final String err){
    return isPattern("isPattern", pp, err);
  }
  /**
   * Scans the input for an occurrence of a string pattern.
   * @param name the Parser object name.
   * @param pp the pattern object.
   * @param err the expected message when fails.
   * @return the Parser object.
   */
  public static Parser<_> isPattern(final String name, final Pattern pp, final String err){
    return new Parser<_>(name){
      boolean apply(final ParseContext ctxt){
        final int at = ctxt.getAt();
        final CharSequence src = ctxt.getSource();
        final int mlen = pp.match(src, src.length(), at);
        if(mlen < 0){
          return setErrorExpecting(err, ctxt);
        }
        ctxt.next(mlen);
        return nothing(ctxt);
      }
    };
  }

  //case insensitive
  /**
   * matches the input against the specified string case insensitively.
   * @param str the string to match
   * @param err the error message if fails.
   * @return the scanner.
   */
  public static Parser<_> isStringCI(final String str, final String err){
    return isStringCI("is " + str, str, err);
  }
  /**
   * matches the input against the specified string case insensitively.
   * @param name the scanner name.
   * @param str the string to match
   * @param err the error message if fails.
   * @return the scanner.
   */
  public static Parser<_> isStringCI(final String name, final String str, final String err){
    return isPattern(name, Patterns.isStringCI(str), err);
  }
  /**
   * matches the input against the specified string case insensitively.
   * @param str the string to match
   * @return the scanner.
   */
  public static Parser<_> isStringCI(final String str){
    return isStringCI(str, str);
  }
  /**
   * matches any character in the input.
   * Different from one(), it fails on EOF. Also it consumes the current character in the input.
   * @return the scanner.
   */
  public static Parser<_> anyChar(){
    return anyChar("any character");
  }
  /**
   * matches any character in the input.
   * Different from one(), it fails on EOF. Also it consumes the current character in the input.
   * @param err the error message if fails.
   * @return the scanner.
   */
  public static Parser<_> anyChar(final String err){
    return anyChar("anyChar", err);
  }
  /**
   * matches any character in the input.
   * Different from one(), it fails on EOF. Also it consumes the current character in the input.
   * @param name the scanner name.
   * @param err the error message if fails.
   * @return the scanner.
   */
  public static Parser<_> anyChar(final String name, final String err){
    return new Parser<_>(name){
      boolean apply(final ParseContext ctxt){
        if(ctxt.isEof()){
          return setErrorExpecting(err, ctxt);
        }
        ctxt.next();
        return nothing(ctxt);
      }
    };
  }

  /**
   * succeed and consume the current character if it satisfies the given CharPredicate.
   * @param cp the predicate.
   * @return the scanner.
   */
  public static Parser<_> isChar(final CharPredicate cp){
    return isChar("isChar", cp);
  }
  /**
   * succeed and consume the current character if it satisfies the given CharPredicate.
   * @param name the scanner name.
   * @param cp the predicate.
   * @return the scanner.
   */
  public static Parser<_> isChar(final String name, final CharPredicate cp){
    return isChar(name, cp, "" + cp);
  }
  /**
   * succeed and consume the current character if it satisfies the given CharPredicate.
   * @param cp the predicate.
   * @param err the error message.
   * @return the scanner.
   */
  public static Parser<_> isChar(final CharPredicate cp, final String err){
    return isChar("isChar", cp, err);
  }
  /**
   * succeed and consume the current character if it satisfies the given CharPredicate.
   * @param name the scanner name.
   * @param cp the predicate.
   * @param err the error message.
   * @return the scanner.
   */
  public static Parser<_> isChar(final String name, final CharPredicate cp, final String err){
    return new Parser<_>(name){
      boolean apply(final ParseContext ctxt){
        if(ctxt.isEof()){
          return setErrorExpecting(err, ctxt);
        }
        final char c = ctxt.peekChar();
        if(cp.isChar(c)){
          ctxt.next();
          return nothing(ctxt);
        }
        else{
          return setErrorExpecting(err, ctxt);
        }
      }
    };
  }

  /**
   * succeed and consume the current character if it is equal to ch.
   * @param ch the expected character.
   * @param err the error message.
   * @return the scanner.
   */
  public static Parser<_> isChar(final char ch, final String err){
    return isChar("=="+ch, ch, err);
  }
  /**
   * succeed and consume the current character if it is equal to ch.
   * @param name the scanner name.
   * @param ch the expected character.
   * @param err the error message.
   * @return the scanner.
   */
  public static Parser<_> isChar(final String name, final char ch, final String err){
    return isChar(name, CharPredicates.isChar(ch), err);
  }
  /**
   * succeed and consume the current character if it is equal to ch.
   * @param ch the expected character.
   * @return the scanner.
   */
  public static Parser<_> isChar(final char ch){
    return isChar("=="+ch, ch);
  }
  /**
   * succeed and consume the current character if it is equal to ch.
   * @param name the scanner name.
   * @param ch the expected character.
   * @return the scanner.
   */
  public static Parser<_> isChar(final String name, final char ch){
    return isChar(name, ch, CharEncoder.encode(ch));
  }
  /**
   * succeed and consume the current character if it is equal to ch.
   * @param ch the expected character.
   * @param err the error message.
   * @return the scanner.
   */
  public static Parser<_> notChar(final char ch, final String err){
    return notChar("!="+ch, ch, err);
  }
  /**
   * succeed and consume the current character if it is not equal to ch.
   * @param name the scanner name.
   * @param ch the expected character.
   * @param err the error message.
   * @return the scanner.
   */
  public static Parser<_> notChar(final String name, final char ch, final String err){
    return isChar(name, CharPredicates.notChar(ch), err);
  }
  /**
   * succeed and consume the current character if it is not equal to ch.
   * @param ch the expected character.
   * @return the scanner.
   */
  public static Parser<_> notChar(final char ch){
    return notChar("!="+ch, ch);
  }
  /**
   * succeed and consume the current character if it is not equal to ch.
   * @param name the scanner name.
   * @param ch the expected character.
   * @return the scanner.
   */
  public static Parser<_> notChar(final String name, final char ch){
    return notChar(name, ch, "^"+CharEncoder.encode(ch));
  }

  private static char[] copy(final char[] chars){
    return (char[])chars.clone();
  }
  private static StringBuffer toString(final StringBuffer buf, final char[] chars){
    buf.append('[');
    if(chars.length >0){
      buf.append(CharEncoder.encode(chars[0]));
      for(int i=1; i<chars.length; i++){
        buf.append(',').append(CharEncoder.encode(chars[i]));
      }
    }
    buf.append(']');
    return buf;
  }
  private static Parser<_> _among(final String name, final char[] chars, final String err){
    return isChar(name, CharPredicates.among(chars), err);
  }
  private static Parser<_> _notAmong(final String name, final char[] chars, final String err){
    return isChar(name, CharPredicates.notAmong(chars), err);
  }
  /**
   * succeed and consume the current character if it equals to one of the given characters.
   * 
   * @param name the scanner name.
   * @param chars the characters.
   * @param err the error message when the character is not among the given values.
   * @return the scanner.
   */
  public static Parser<_> among(final String name, final char[] chars, final String err){
    if(chars.length==0)return Parsers.zero();
    if(chars.length==1)return isChar(name, chars[0], err);
    return _among(name, copy(chars), err);
  }
  /**
   * succeed and consume the current character if it equals to one of the given characters.
   * 
   * @param name the scanner name.
   * @param chars the characters.
   * @return the scanner.
   */
  public static Parser<_> among(final String name, final char[] chars){
    final StringBuffer buf = new StringBuffer();
    toString(buf, chars);
    return among(name, chars, buf.toString());
  }
  /**
   * succeed and consume the current character if it equals to one of the given characters.
   * 
   * @param chars the characters.
   * @param err the error message when the character is not among the given values.
   * @return the scanner.
   */
  public static Parser<_> among(final char[] chars, final String err){
    return among("among", chars, err);
  }
  /**
   * succeed and consume the current character if it equals to one of the given characters.
   * 
   * @param chars the characters.
   * @return the scanner.
   */
  public static Parser<_> among(final char[] chars){
    return among("among", chars);
  }
  /**
   * succeed and consume the current character if it is not equal to any of the given characters.
   * 
   * @param name the scanner name.
   * @param chars the characters.
   * @param err the error message when the character is among the given values.
   * @return the scanner.
   */
  public static Parser<_> notAmong(final String name, final char[] chars, final String err){
    if(chars.length==0)return anyChar();
    if(chars.length==1)return notChar(name, chars[0], err);
    return _notAmong(name, copy(chars), err);
  }
  /**
   * succeed and consume the current character if it is not equal to any of the given characters.
   * 
   * @param name the scanner name.
   * @param chars the characters.
   * @return the scanner.
   */
  public static Parser<_> notAmong(final String name, final char[] chars){
    final StringBuffer buf = new StringBuffer();
    buf.append("^");
    toString(buf, chars);
    return notAmong(name, chars, buf.toString());
  }
  /**
   * succeed and consume the current character if it is not equal to any of the given characters.
   * 
   * @param chars the characters.
   * @param err the error message when the character is not among the given values.
   * @return the scanner.
   */
  public static Parser<_> notAmong(final char[] chars, final String err){
    return among("notAmong", chars, err);
  }
  /**
   * succeed and consume the current character if it is not equal to any of the given characters.
   * 
   * @param chars the characters.
   * @return the scanner.
   */
  public static Parser<_> notAmong(final char[] chars){
    return notAmong("notAmong", chars);
  }

  /**
   * if the current input starts with the given string, succeed and consumes all the characters until the end of line '\n character.
   * It does not consume the end of line character.
   * @param start the start string.
   * @return the scanner.
   */
  public static Parser<_> isLineComment(final String start){
    return isLineComment("isLineComment", start);
  }
  /**
   * if the current input starts with the given string, succeed and consumes all the characters until the end of line '\n character.
   * It does not consume the end of line character.
   * @param name the scanner name.
   * @param start the start string.
   * @return the scanner.
   */
  public static Parser<_> isLineComment(final String name, final String start){
    return isPattern(name, Patterns.isLineComment(start), start);
  }
  /**
   * scanner for c++/java style line comment.
   * @return the scanner.
   */
  public static Parser<_> javaLineComment(){
    return isLineComment("//");
  }
  /**
   * scanner for T-SQL style line comment.
   * @return the scanner.
   */
  public static Parser<_> sqlLineComment(){
    return isLineComment("--");
  }
  /**
   * scanner for c++/java style block comment. 
   * @return the scanner.
   */
  public static Parser<_> javaBlockComment(){
    //return isBlockComment("/*", "*/");
    return Parsers.seq(isString("/*"), 
        /*many(p_javaBlockCommented())*/
        p_javaBlockCommented(), isString("*/"));
    //isNestableBlockComment("/*", "*/");
  }
  /**
   * scanner for haskell style block comment. ({- -})
   * @return the scanner.
   */
  public static Parser<_> haskellBlockComment(){
    return 
    //isBlockCommentWith("{-", "-}");
    Parsers.seq(isString("{-"), p_haskellBlockCommented(), isString("-}"));
  }
  /**
   * scanner for haskell style line comment. (--)
   * @return the scanner.
   */
  public static Parser<_> haskellLineComment(){
    return isLineComment("--");
  }
  /**
   * scanner for non-nested block comment.
   * @param start the start string of a block comment.
   * @param end the end string of a block comment. 
   * @return the scanner.
   */
  public static Parser<_> isBlockComment(final String start, final String end){
    return isBlockComment("isBlockComment", start, end);
  }
  /**
   * scanner for non-nested block comment.
   * @param name the scanner name.
   * @param start the start string of a block comment.
   * @param end the end string of a block comment. 
   * @return the scanner.
   */
  public static Parser<_> isBlockComment(
      final String name, final String start, final String end){
    final Pattern opening = Patterns.isString(start).seq(Patterns.notString(end).many());
    return isPattern(opening, start).seq(name, isString(end));
  }
  /**
   * Scans a non-nestable block comment.
   * @param open the opening string.
   * @param close the closing string.
   * @param commented the commented pattern.
   * @return the Scanner for the block comment.
   */
  public static Parser<_> isBlockComment(final String open, final String close,
      final Pattern commented){
    final Pattern opening = Patterns.isString(open)
    .seq(Patterns.isString(close).not().seq(commented).many());
    return isPattern(opening, open).seq(isString(close));
  }
  /**
   * Scans a non-nestable block comment.
   * @param open the opening pattern.
   * @param close the closing pattern.
   * @param commented the commented pattern.
   * @return the Scanner for the block comment.
   */
  public static Parser<_> isBlockComment(final Parser<_> open,
      final Parser<_> close, final Parser<?> commented){
    return isBlockComment("isBlockComment", open, close, commented);
  }
  /**
   * Scans a non-nestable block comment.
   * @param name the name of the block comment scanner.
   * @param open the opening pattern.
   * @param close the closing pattern.
   * @param commented the commented pattern.
   * @return the Scanner for the block comment.
   */
  public static Parser<_> isBlockComment(final String name,
      final Parser<_> open, final Parser<_> close, final Parser<?> commented){
    return Parsers.seq(name, open, close.not().seq(commented).many(), close);
  }
  /**
   * Scans a nestable block comment.
   * Nested comments and any other characters can be in the comment body.
   * @param open the opening pattern.
   * @param close the closing pattern.
   * @return the block comment scanner.
   */
  public static Parser<_> isNestableBlockComment(final String open, final String close){
    return isNestableBlockComment(open, close, anyChar());
  }
  /**
   * Scans a nestable block comment.
   * Nested comments and some commented pattern can be in the comment body.
   * @param open the opening string.
   * @param close the closing string.
   * @param commented the commented pattern except for nested comments.
   * @return the block comment scanner.
   */
  public static Parser<_> isNestableBlockComment(final String open, final String close,
      final Parser<?> commented){
    return isNestableBlockComment(isString(open), isString(close), commented);
  }
  /**
   * Scans a nestable block comment.
   * Nested comments and some commented pattern can be in the comment body.
   * @param open the opening pattern.
   * @param close the closing pattern.
   * @param commented the commented pattern except for nested comments.
   * @return the block comment scanner.
   */
  public static Parser<_> isNestableBlockComment(
      final Parser<?> open, final Parser<?> close, 
      final Parser<?> commented){
    return isNestableBlockComment("isNestableBlockComment",
        open, close, commented);
  }
  /**
   * Scans a nestable block comment.
   * Nested comments and some commented pattern can be in the comment body.
   * @param name the name of the block comment scanner.
   * @param open the opening pattern.
   * @param close the closing pattern.
   * @param commented the commented pattern except for nested comments.
   * @return the block comment scanner.
   */
  public static Parser<_> isNestableBlockComment(
      final String name, final Parser<?> open, final Parser<?> close,
      final Parser<?> commented){
    return new Parser<_>(name){
      boolean apply(final ParseContext ctxt){
        if(!open.parse(ctxt)) return false;
        for(int level=1; level>0;){
          final int at = ctxt.getAt();
          if(close.parse(ctxt)){
            if(at == ctxt.getAt())
              throw new IllegalParserStateException("closing comment scanner not consuming input.");
            level--;
            continue;
          }
          if(at != ctxt.getAt()) return false;
          if(open.parse(ctxt)){
            if(at == ctxt.getAt())
              throw new IllegalParserStateException("opening comment scanner not consuming input.");
            level++;
            continue;
          }
          if(at != ctxt.getAt()) return false;
          if(commented.parse(ctxt)){
            if(at == ctxt.getAt())
              throw new IllegalParserStateException("commented scanner not consuming input.");
            continue;
          }
          return false;
        }
        return nothing(ctxt);
      }
    };
  }
  /**
   * a scanner with a pattern for sql server string literal.
   * a sql server string literal is a string quoted by single quote, 
   * a single quote character is escaped by 2 single quotes.
   * @return the scanner.
   */
  public static Parser<_> isSqlString(){
    return isSqlString("isSqlString");
  }
  /**
   * a scanner with a pattern for sql server string literal.
   * a sql server string literal is a string quoted by single quote, 
   * a single quote character is escaped by 2 single quotes.
   * @param name the name of the scanner.
   * @return the scanner.
   */
  public static Parser<_> isSqlString(final String name){
    /*
    final Pattern open = Patterns.isChar('\'').seq(
        Patterns.or(Patterns.notChar('\''), Patterns.isString("''"))
        .many()
    );
    return isPattern(open, "'").seq(name, isChar('\''));
    */
    final Parser<_> q = isChar('\'');
    final Parser<_> qs = isPattern(
        Patterns.regex("(('')|[^'])*"), "quoted string");
    return Parsers.between(name, q, q, qs);
  }
  /**
   * a scanner with a pattern for double quoted string literal.
   * backslash '\' is used as escape character. 
   * @param name the name of the scanner.
   * @return the scanner.
   */
  public static Parser<_> isQuotedString(final String name){
    /*
    final Pattern q = Patterns.isChar('"');
    final Pattern open = q.seq(quoted().many());
    return isPattern(open, "\"").seq(name, isPattern(q, "\""));
    */
    final Parser<_> q = Scanners.isChar('"');
    final Parser<_> qc = isPattern(
       quoted_str ,"quoted string");
    return Parsers.between(name, q, q, qc);
  }
  private static final Pattern quoted_str = 
    Patterns.regex("((\\\\.)|[^\"\\\\])*");
  /**
   * a scanner with a pattern for double quoted string literal.
   * backslash '\' is used as escape character.
   * @return the scanner.
   */
  public static Parser<_> isQuotedString(){
    return isQuotedString("isQuotedString");
  }
  /*
  private static Pattern quoted(){
    return Patterns.or(
            Patterns.isEscaped(),
            Patterns.notChar('"')
        );
  }*/
  private static final Pattern quoted_char =
    Patterns.regex("(\\\\.)|[^'\\\\]"); 
  /**
   * scanner for a c/c++/java style character literal. such as 'a' or '\\'.
   * @param name the name of the scanner.
   * @return the scanner.
   */
  public static Parser<_> isQuotedChar(final String name){
    //final Scanner q = isChar('\'');
    /*
    final Pattern q = Patterns.isChar('\'');
    final Pattern qc = Patterns.or(
        Patterns.isString("\\'"),
        Patterns.notChar('\'')
    );
    return isPattern(q.seq(qc), "'").seq(name, isPattern(q, "'"));
    */
    final Parser<_> q = Scanners.isChar('\'');
    final Parser<_> qc = isPattern(
       quoted_char ,"quoted char");
    return Parsers.between(name, q, q, qc);
  }
  /**
   * scanner for a c/c++/java style character literal. such as 'a' or '\\'.
   * @return the scanner.
   */
  public static Parser<_> isQuotedChar(){
    return isQuotedChar("isQuotedChar");
  }
  /**
   * scans a quoted string that is opened by c1 and closed by c2. 
   * @param c1 the opening character.
   * @param c2 the closing character.
   * @return the scanner.
   */
  public static Parser<_> quoted(final char c1, final char c2){
    return quoted("quoted", c1, c2);
  }
  /**
   * scans a quoted string that is opened by c1 and closed by c2.
   * @param name the scanner name. 
   * @param c1 the opening character.
   * @param c2 the closing character.
   * @return the scanner.
   */
  public static Parser<_> quoted(final String name, final char c1, final char c2){
    return isPattern(name, Patterns.isChar(c1)
            .seq(Patterns.many(CharPredicates.notChar(c2))),
            ""+c1).seq(isChar(c2));
  }
  /**
   * scans a quoted string that is opened by c1 and closed by c2.
   * @param open the opening character.
   * @param close the closing character.
   * @return the scanner.
   */
  public static Parser<_> quoted(final Parser<_> open, final Parser<_> close, final Parser<?> s){
    return quoted("quoted", open, close, s);
  }
  /**
   * scans a quoted string that is opened by pattern represented by Scanner open and closed by pattern represented by Scanner close.
   * @param name the scanner name. 
   * @param open the scanner for the opening pattern.
   * @param close the scanner for the closing pattern.
   * @return the scanner.
   */
  public static Parser<_> quoted(
      final String name, final Parser<_> open, final Parser<_> close, final Parser<?> s){
    return Parsers.seq(name, open, s.many(), close);
  }
  /**
   * the c++/java style delimiter of tokens. 
   * whitespace, line comment, block comment.
   * @return the scanner.
   */
  public static Parser<_> javaDelimiter(){
    return javaDelimiter("javaDelimiter");
  }
  /**
   * the c++/java style delimiter of tokens. 
   * whitespace, line comment, block comment.
   * @param name the scanner name.
   * @return the scanner.
   */
  public static Parser<_> javaDelimiter(final String name){
    return Parsers.sum(
        isWhitespaces(), javaLineComment(), javaBlockComment()).many(name);
  }
  /**
   * the haskell style delimiter of tokens. 
   * whitespace, line comment, block comment.
   * @return the scanner.
   */
  public static Parser<_> haskellDelimiter(){
    return haskellDelimiter("haskellDelimiter");
  }
  /**
   * the haskell style delimiter of tokens. 
   * whitespace, line comment, block comment.
   * @param name the scanner name.
   * @return the scanner.
   */
  public static Parser<_> haskellDelimiter(final String name){
    return Parsers.sum(
        isWhitespaces(), haskellBlockComment(), haskellLineComment()).many(name);
  }
  /**
   * the T-SQL style delimiter of tokens. 
   * whitespace and line comment.
   * @return the scanner.
   */
  public static Parser<_> sqlDelimiter(){
    return sqlDelimiter("sqlDelimiter");
  }
  /**
   * the T-SQL style delimiter of tokens. 
   * whitespace and line comment.
   * @param name the scanner name.
   * @return the scanner.
   */
  public static Parser<_> sqlDelimiter(final String name){
    return Parsers.sum(isWhitespaces(), sqlLineComment(), javaBlockComment())
      .many(name);
  }
  /**
   * Any delimiter with whitespace, non-nested block comment and line comment.
   * @param name the scanner name.
   * @param lcomment line comment starting string.
   * @param openc block comment opening string.
   * @param closec block comment closing string.
   * @return the scanner.
   */
  public static Parser<_> stdDelimiter(final String name, 
      final String lcomment, final String openc, final String closec){
    return Parsers.sum(isWhitespaces(), isLineComment(lcomment), isBlockComment(openc, closec))
      .many(name);
  }
  /**
   * Any delimiter with whitespace, non-nested block comment and line comment.
   * @param lcomment line comment starting string.
   * @param openc block comment opening string.
   * @param closec block comment closing string.
   * @return the scanner.
   */
  public static Parser<_> stdDelimiter(final String lcomment, final String openc, final String closec){
    return stdDelimiter("stdDelimiter", lcomment, openc, closec);
  }

  /**
   * If a string is not followed by a alphanumeric character, it is well-delimited.
   * delimited() make sure the pattern represented by scanner s is delimited.
   * @param s the scanner for the to-be-delimited pattern.
   * @param err the error message if it is not delimited.
   * @return the new scanner.
   */
  public static <R> Parser<R> delimited(final Parser<R> s, final String err){
    return delimited("delimited", s, err);
  }
  /**
   * If a string is not followed by a alphanumeric character, it is well-delimited.
   * delimited() make sure the pattern represented by scanner s is delimited.
   * @param name the new scanner name.
   * @param s the scanner for the to-be-delimited pattern.
   * @param err the error message if it is not delimited.
   * @return the new scanner.
   */
  public static <R> Parser<R> delimited(final String name, final Parser<R> s, final String err){
    return s.followedBy(name, isChar(CharPredicates.isAlphaNumeric(), err).not());
  }
  /**
   * If a string is not followed by a alphanumeric character, it is well-delimited.
   * delimited() make sure the pattern represented by scanner s is delimited.
   * @param s the scanner for the to-be-delimited pattern.
   * @return the new scanner.
   */
  public static <R> Parser<R> delimited(final Parser<R> s){
    return delimited("delimited", s);
  }
  /**
   * If a string is not followed by a alphanumeric character, it is well-delimited.
   * delimited() make sure the pattern represented by scanner s is delimited.
   * @param name the new scanner name.
   * @param s the scanner for the to-be-delimited pattern.
   * @return the new scanner.
   */
  public static <R> Parser<R> delimited(final String name, final Parser<R> s){
    return delimited(name, s, "delimiter");
  }
  /**
   * After character level parser p succeeds,
   * subsequently feed the recognized characters to the Parser scanner
   * for a nested scanning.
   * @param p the first parser object to identify the characters.
   * @param scanner the second parser object to scan the characters again.
   * @param module the module name.
   * @return the new Parser object.
   */
  public static Parser<_> scanChars(final Parser<?> p, final Parser<_> scanner, 
      final String module){
    return scanChars("scanChars", p, scanner, module);
  }
  /**
   * After character level parser p succeeds,
   * subsequently feed the recognized characters to the Parser scanner
   * for a nested scanning.
   * @param name the name of the new Parser object.
   * @param p the first parser object to identify the characters.
   * @param scanner the second parser object to scan the characters again.
   * @param module the module name.
   * @return the new Parser object.
   */
  public static Parser<_> scanChars(final String name,
      final Parser<?> p, final Parser<_> scanner, final String module){
    return new Parser<_>(name){
      boolean apply(final ParseContext ctxt){
        final int from = ctxt.getAt();
        if(!p.parse(ctxt)) return false;
        final ScannerState s0 = 
          new ScannerState(ctxt.getSource(), from, 
              module, ctxt.getPositionMap(), ctxt.getAt()-from,
              ctxt.getReturn(), ctxt.getUserState());
        return ParserInternals.cont(ctxt, s0, scanner);
      }
    };
  }
  /**
   * Matches a character if the input has at least 1 character; 
   * and if the input has at least 2 characters, 
   * the first 2 characters are not c1 and c2.
   * @return the Pattern object.
   */
  private static Pattern notChar2(final char c1, final char c2){
    return new Pattern(){
      public int match(final CharSequence src, final int len, final int from){
        if(from >= len-1) return Pattern.MISMATCH;
        if(src.charAt(from) == c1 && src.charAt(from+1) == c2) return Pattern.MISMATCH;
        return 1;
      }
    };
  }
  
  private static Parser<_> p_javaBlockCommented(){
    return isPattern(notChar2('*', '/').many(), "commented block");
  }
  /*
  private static final Parser<_> p_javaBlockCommented =
    isPattern(Patterns.regex("([^*]|(\\*[^/]))*"), "block content");
  private static final Parser<_> p_haskellBlockCommented =
    isPattern(Patterns.regex("([^-]|(\\*[^}]))*"), "block content");
  */
  private static Parser<_> p_haskellBlockCommented(){
    return isPattern(notChar2('-', '}').many(), "commented block");
  }
  private static boolean setErrorExpecting(final String msg, final ParseContext ctxt){
    return ParserInternals.setErrorExpecting(msg, ctxt);
  }
  private static boolean nothing(ParseContext ctxt){
    ctxt.setReturn(null);
    return true;
  }
  /**
   * Greedily runs Parser s repeatedly,
   * and ignores the pattern recognized by Parser delim before and after each s.
   * Parser s has to be a lexer object that returns a Tok object.
   * The result Tok objects are collected and returned in a Tok[] array.
   * @param delim the delimiter Parser object.
   * @param s the Parser object.
   * @return the new Parser object.
   */
  @Deprecated
  public static Parser<Tok[]> lexeme(final Parser<?> delim, final Parser<Tok> s){
    return Lexers.lexeme(delim, s);
  }
  /**
   * Greedily runs Parser s repeatedly,
   * and ignores the pattern recognized by Parser delim before and after each s.
   * Parser s has to be a lexer object that returns a Tok object.
   * The result Tok objects are collected and returned in a Tok[] array.
   * @param name the name of the new Parser object.
   * @param delim the delimiter Parser object.
   * @param s the Parser object.
   * @return the new Parser object.
   */
  @Deprecated
  public static Parser<Tok[]> lexeme(final String name, 
      final Parser<?> delim, final Parser<Tok> s){
    return Lexers.lexeme(name, delim, s);
  }
  /**
   * Transform the recognized character range of scanner s to a token object
   * with a Tokenizer. 
   * If the Tokenizer.toToken() returns null, scan fails.
   * @param s the scanner to transform.
   * @param tn the Tokenizer object.
   * @return the new Scanner.
   */
  @Deprecated
  public static Parser<Tok> lexer(final Parser<?> s, final Tokenizer tn){
    return Lexers.lexer(s,tn);
  }
  /**
   * Transform the recognized character range of scanner s to a token object
   * with a Tokenizer. 
   * If the Tokenizer.toToken() returns null, scan fails.
   * @param s the scanner to transform.
   * @param tn the Tokenizer object.
   * @param err the error message when the tokenizer returns null.
   * @return the new Scanner.
   */
  @Deprecated
  public static Parser<Tok> lexer(final Parser<?> s, final Tokenizer tn, final String err){
    return Lexers.lexer(s, tn, err);
  }
  /**
   * Transform the recognized character range of scanner s to a token object
   * with a Tokenizer. 
   * If the Tokenizer.toToken() returns null, scan fails.
   * @param name the name of the new Scanner.
   * @param tn the Tokenizer object.
   * @param s the scanner to transform.
   * @return the new Scanner.
   */
  @Deprecated
  public static Parser<Tok> lexer(final String name, final Parser<?> s, final Tokenizer tn){
    return Lexers.lexer(name, s, tn);
  }
  /**
   * Transform the recognized character range of scanner s to a token object
   * with a Tokenizer. 
   * If the Tokenizer.toToken() returns null, scan fails.
   * @param name the name of the new Scanner.
   * @param s the scanner to transform.
   * @param tn the Tokenizer object.
   * @param err the error message when the tokenizer returns null.
   * @return the new Scanner.
   */
  @Deprecated
  public static Parser<Tok> lexer(final String name, final Parser<?> s, final Tokenizer tn, 
      final String err){
    return Lexers.lexer(name, s, tn, err);
  }
  
}
