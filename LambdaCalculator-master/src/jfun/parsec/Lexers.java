/*****************************************************************************
 * Copyright (C) Zephyr Business Solutions Corp. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/
/*
 * Created on Dec 19, 2004
 *
 * Author Ben Yu
 */
package jfun.parsec;

import jfun.parsec.pattern.Patterns;
import jfun.parsec.tokens.Tokenizers;


/**
 * Provides some predefined basic lexer objects.
 * A lexer is a character level parser that returns a token
 * based on the recognized character range.
 * @author Ben Yu
 *
 * Dec 19, 2004
 */
public final class Lexers {
  private static final Parser<Tok> _charLiteral = charLiteral("charLiteral");
  /**
   * returns the lexer that's gonna parse single quoted character literal (escaped by '\'),
   * and then converts the character to a Character.
   * @return the lexer.
   */
  public static Parser<Tok> charLiteral(){
    return _charLiteral;
  }
  /**
   * returns the lexer that's gonna parse single quoted character literal (escaped by '\'),
   * and then converts the character to a Character.
   * @param name the lexer name.
   * @return the lexer.
   */
  public static Parser<Tok> charLiteral(final String name){
    return Lexers.lexer(name, 
        Scanners.isQuotedChar(),
          Tokenizers.forChar());
  }
  private static final Parser<Tok> _stringLiteral = stringLiteral("stringLiteral");
  /**
   * returns the lexer that's gonna parse double quoted string literal (escaped by '\'),
   * and convert the string to a String token.
   * @return the lexer.
   * @deprecated Use {@link #lexSimpleStringLiteral()}
   */
  public static Parser<Tok> stringLiteral(){
    return lexSimpleStringLiteral();
  }
  /**
   * returns the lexer that's gonna parse double quoted string literal (escaped by '\'),
   * and convert the string to a String token.
   * @return the lexer.
   */
  public static Parser<Tok> lexSimpleStringLiteral(){
    return _stringLiteral;
  }
  /**
   * returns the lexer that's gonna parse double quoted string literal (escaped by '\'),
   * and convert the string to a String token.
   * @param name the lexer name.
   * @return the lexer.
   * @deprecated Use {@link #lexSimpleStringLiteral(String)}
   */
  public static Parser<Tok> stringLiteral(final String name){
    return lexSimpleStringLiteral(name);
  }
  /**
   * returns the lexer that's gonna parse double quoted string literal (escaped by '\'),
   * and convert the string to a String token.
   * @param name the lexer name.
   * @return the lexer.
   */
  public static Parser<Tok> lexSimpleStringLiteral(final String name){
    return Lexers.lexer(name,
        Scanners.isQuotedString(),
        Tokenizers.forSimpleStringLiteral()
    );
  }
  private static final Parser<Tok> _sqlStringLiteral = sqlStringLiteral("string quoted by '");
  /**
   * returns the lexer that's gonna parse single quoted string literal (single quote is escaped with another single quote),
   * and convert the string to a String token.
   * @return the lexer.
   */
  public static Parser<Tok> sqlStringLiteral(){
    return _sqlStringLiteral;
  }
  /**
   * returns the lexer that's gonna parse single quoted string literal (single quote is escaped with another single quote),
   * and convert the string to a String token.
   * @param name the lexer name.
   * @return the lexer.
   */
  public static Parser<Tok> sqlStringLiteral(final String name){
    return Lexers.lexer(name,
        Scanners.isSqlString(),
        Tokenizers.forSqlStringLiteral()
    );
  }
  private static final Parser<Tok> _decimal = decimal("decimal");
  /**
   * returns the lexer that's gonna parse a decimal number (valid patterns are: 1, 2.3, 000, 0., .23),
   * and convert the string to a decimal typed token.
   * @return the lexer.
   */
  public static Parser<Tok> decimal(){
    return _decimal;
  }
  /**
   * returns the lexer that's gonna parse a decimal number (valid patterns are: 1, 2.3, 000, 0., .23),
   * and convert the string to a decimal typed token.
   * @param name the lexer name.
   * @return the lexer.
   */
  public static Parser<Tok> decimal(final String name){
    return Lexers.lexer(name,
        Scanners.delimited(Scanners.isPattern(Patterns.isDecimal(), "decimal number")),
        Tokenizers.forDecimal()
    );
  }
  private static final Parser<Tok> _integer = integer("integer");
  /**
   * returns the lexer that's gonna parse a integer number (valid patterns are: 0, 00, 1, 10),
   * and convert the string to an integer typed token.
   * The difference between integer() and decInteger() is that decInteger does not allow a number starting with 0.
   * @return the lexer.
   */
  public static Parser<Tok> integer(){
    return _integer;
  }
  /**
   * returns the lexer that's gonna parse a integer number (valid patterns are: 0, 00, 1, 10),
   * and convert the string to an integer typed token.
   * The difference between integer() and decInteger() is that decInteger does not allow a number starting with 0.
   * @param name the lexer name.
   * @return the lexer.
   */
  public static Parser<Tok> integer(final String name){
    return Lexers.lexer(name, Scanners.delimited(
        Scanners.isPattern(Patterns.isInteger(), "integer")),
        Tokenizers.forInteger());
  }
  /**
   * returns the lexer that's gonna parse a decimal integer number (valid patterns are: 1, 10, 123),
   * and convert the string to a Long token.
   * The difference between integer() and decInteger() is that decInteger does not allow a number starting with 0.
   * @return the lexer.
   * @deprecated Use {@link #lexDecLong()}.
   */
  
  public static Parser<Tok> decInteger(){
    return lexDecLong();
  }
  /**
   * returns the lexer that's gonna parse a decimal integer number (valid patterns are: 1, 10, 123),
   * and convert the string to a Long token.
   * The difference between integer() and decInteger() is that decInteger does not allow a number starting with 0.
   * @param name the lexer name.
   * @return the lexer.
   * @deprecated Use {@link #lexDecLong(String)}.
   */
  public static Parser<Tok> decInteger(final String name){
    return lexDecLong(name);
  }
  
  /**
   * returns the lexer that's gonna parse a octal integer number (valid patterns are: 0, 07, 017, 0371 etc.),
   * and convert the string to a Long token.
   * an octal number has to start with 0.
   * @return the lexer.
   * @deprecated Use {@link #lexOctLong()}.
   */
  public static Parser<Tok> octInteger(){
    return lexOctLong();
  }
  /**
   * returns the lexer that's gonna parse a octal integer number (valid patterns are: 0, 07, 017, 0371 etc.),
   * and convert the string to a Long token.
   * an octal number has to start with 0.
   * @param name the lexer name.
   * @return the lexer.
   * @deprecated Use {@link #lexOctLong(String)}.
   */
  public static Parser<Tok> octInteger(final String name){
    return lexOctLong(name);
  }
  /**
   * returns the lexer that's gonna parse a hex integer number (valid patterns are: 0x1, 0Xff, 0xFe1 etc.),
   * and convert the string to a Long token.
   * an hex number has to start with either 0x or 0X.
   * @return the lexer.
   * @deprecated Use {@link #lexHexLong()}.
   */
  public static Parser<Tok> hexInteger(){
    return lexHexLong();
  }
  /**
   * returns the lexer that's gonna parse a hex integer number (valid patterns are: 0x1, 0Xff, 0xFe1 etc.),
   * and convert the string to a Long token.
   * an hex number has to start with either 0x or 0X.
   * @param name the lexer name.
   * @return the lexer.
   * @deprecated Use {@link #lexHexLong(String)}.
   */
  public static Parser<Tok> hexInteger(final String name){
    return lexHexLong(name);
  }
  /**
   * returns the lexer that's gonna parse decimal, hex, and octal numbers
   * and convert the string to a Long token.
   * @return the lexer.
   * @deprecated Use {@link #lexLong()}.
   */
  public static Parser<Tok> allInteger(){
    return lexLong();
  }
  /**
   * returns the lexer that's gonna parse decimal, hex, and octal numbers
   * and convert the string to a Long token.
   * @param name the lexer name.
   * @return the lexer.
   * @deprecated Use {@link #lexLong(String)}.
   */
  public static Parser<Tok> allInteger(final String name){
    return lexLong(name);
  }
  
  private static final Parser<Tok> _decLong = lexDecLong("decLong");
  /**
   * returns the lexer that's gonna parse a decimal integer number (valid patterns are: 1, 10, 123),
   * and convert the string to a Long token.
   * The difference between integer() and decInteger() is that decInteger does not allow a number starting with 0.
   * @return the lexer.
   */
  public static Parser<Tok> lexDecLong(){
    return _decLong;
  }
  /**
   * returns the lexer that's gonna parse a decimal integer number (valid patterns are: 1, 10, 123),
   * and convert the string to a Long token.
   * The difference between integer() and decInteger() is that decInteger does not allow a number starting with 0.
   * @param name the lexer name.
   * @return the lexer.
   */
  public static Parser<Tok> lexDecLong(final String name){
    return Lexers.lexer(name,
        Scanners.delimited(Scanners.isPattern(Patterns.isDecInteger(),
            "decLong")), Tokenizers.forDecLong());
  }
  
  private static final Parser<Tok> _octLong = lexOctLong("octLong");
  /**
   * returns the lexer that's gonna parse a octal integer number (valid patterns are: 0, 07, 017, 0371 etc.),
   * and convert the string to a Long token.
   * an octal number has to start with 0.
   * @return the lexer.
   */
  public static Parser<Tok> lexOctLong(){
    return _octLong;
  }
  /**
   * returns the lexer that's gonna parse a octal integer number (valid patterns are: 0, 07, 017, 0371 etc.),
   * and convert the string to a Long token.
   * an octal number has to start with 0.
   * @param name the lexer name.
   * @return the lexer.
   */
  public static Parser<Tok> lexOctLong(final String name){
    return Lexers.lexer(name,
        Scanners.delimited(Scanners.isPattern(
            Patterns.isOctInteger(), "octLong")), Tokenizers.forOctLong());
  }
  private static final Parser<Tok> _hexLong = lexHexLong("hexLong");
  /**
   * returns the lexer that's gonna parse a hex integer number (valid patterns are: 0x1, 0Xff, 0xFe1 etc.),
   * and convert the string to a Long token.
   * an hex number has to start with either 0x or 0X.
   * @return the lexer.
   */
  public static Parser<Tok> lexHexLong(){
    return _hexLong;
  }
  /**
   * returns the lexer that's gonna parse a hex integer number (valid patterns are: 0x1, 0Xff, 0xFe1 etc.),
   * and convert the string to a Long token.
   * an hex number has to start with either 0x or 0X.
   * @param name the lexer name.
   * @return the lexer.
   */
  public static Parser<Tok> lexHexLong(final String name){
    return Lexers.lexer(name,
        Scanners.delimited(Scanners.isPattern(Patterns.isHexInteger(), "hexLong"))
        , Tokenizers.forHexLong());
  }
  private static final Parser<Tok> _allLong = lexLong("allLong");
  /**
   * returns the lexer that's gonna parse decimal, hex, and octal numbers
   * and convert the string to a Long token.
   * @return the lexer.
   */
  public static Parser<Tok> lexLong(){
    return _allLong;
  }
  /**
   * returns the lexer that's gonna parse decimal, hex, and octal numbers
   * and convert the string to a Long token.
   * @param name the lexer name.
   * @return the lexer.
   */
  public static Parser<Tok> lexLong(final String name){
    return Parsers.plus(name, lexHexLong(), lexDecLong(), lexOctLong());
  }
  private static final Parser<Tok> _word = word("word");
  /**
   * returns the lexer that's gonna parse any word.
   * and convert the string to a TokenWord.
   * A word starts with an alphametic character, followed by 0 or more alphanumeric characters.
   * @return the lexer.
   */
  public static Parser<Tok> word(){
    return _word;
  }
  /**
   * returns the lexer that's gonna parse any word.
   * and convert the string to a TokenWord.
   * A word starts with an alphametic character, followed by 0 or more alphanumeric characters.
   * @param name the lexer name.
   * @return the lexer.
   */
  public static Parser<Tok> word(final String name){
    return Lexers.lexer(name, Scanners.delimited(
        Scanners.isPattern(Patterns.isWord(), "word")),
        Tokenizers.forWord());
  }
  /**
   * Create a lexer that parsers a string literal quoted by open and close,
   * and then converts it to a TokenQuoted token instance.
   * @param name the lexer name.
   * @param open the opening character.
   * @param close the closing character.
   * @return the lexer.
   */
  public static Parser<Tok> quoted(final String name, final char open, final char close){
    return Lexers.lexer(name, Scanners.quoted(name, open, close), 
        Tokenizers.forQuotedString(open, close));
  }
  /**
   * Create a lexer that parsers a string literal quoted by open and close,
   * and then converts it to a TokenQuoted token instance.
   * @param open the opening character.
   * @param close the closing character.
   * @return the lexer.
   */
  public static Parser<Tok> quoted(final char open, final char close){
    return quoted("quoted", open, close);
  }

  /**
   * Creates a Words object for lexing the operators with names specified in ops.
   * Operators are lexed as TokenReserved.
   * @param ops the operator names.
   * @return the Words instance.
   */
  public static Words getOperators(final String... ops){
    return Words.getOperators(ops);
  }
  /**
   * Creates a Words object for lexing the operators with names specified in ops,
   * and for lexing the keywords case insensitively.
   * Keywords and operators are lexed as TokenReserved.
   * Words that are not among the keywords are lexed as TokenWord. 
   * A word is defined as an alpha numeric string that starts with [_a-zA-Z],
   * with 0 or more [0-9_a-zA-Z] following. 
   * @param ops the operator names.
   * @param keywords the keyword names.
   * @return the Words instance.
   */
  public static Words getCaseInsensitive(
      final String[] ops, final String[] keywords){
    return Words.getCaseInsensitive(ops, keywords);
  }

  /**
   * Creates a Words object for lexing the operators with names specified in ops,
   * and for lexing the keywords case sensitively. 
   * Keywords and operators are lexed as TokenReserved.
   * Words that are not among the keywords are lexed as TokenWord. 
   * A word is defined as an alpha numeric string that starts with [_a-zA-Z],
   * with 0 or more [0-9_a-zA-Z] following.
   * @param ops the operator names.
   * @param keywords the keyword names.
   * @return the Words instance.
   */
  public static Words getCaseSensitive(
      final String[] ops, final String[] keywords){
    return Words.getCaseSensitive(ops, keywords);
  }
  /**
   * Creates a Words object for lexing the operators with names specified in ops,
   * and for lexing the keywords case insensitively.
   * Keywords and operators are lexed as TokenReserved.
   * Words that are not among the keywords are lexed as TokenWord. 
   * @param wscanner the scanner for a word in the language.
   * @param ops the operator names.
   * @param keywords the keyword names.
   * @return the Words instance.
   */
  public static Words getCaseInsensitive(final Parser<?> wscanner, 
      final String[] ops, final String[] keywords){
    return Words.getCaseInsensitive(wscanner, ops, keywords);
  }
  /**
   * Creates a Words object for lexing the operators with names specified in ops,
   * and for lexing the keywords case sensitively. 
   * Keywords and operators are lexed as TokenReserved.
   * Words that are not among the keywords are lexed as TokenWord. 
   * @param wscanner the scanner for a word in the language.
   * @param ops the operator names.
   * @param keywords the keyword names.
   * @return the Words instance.
   */
  public static Words getCaseSensitive(final Parser<?> wscanner,
      final String[] ops, final String[] keywords){
    return Words.getCaseSensitive(wscanner, ops, keywords);
  }
  /**
   * Creates a Words object for lexing the operators with names specified in ops,
   * and for lexing the keywords case insensitively.
   * Keywords and operators are lexed as TokenReserved.
   * Words that are not among the keywords are lexed as TokenWord. 
   * @param wscanner the scanner for a word in the language.
   * @param ops the operator names.
   * @param keywords the keyword names.
   * @param toWord the FromString object used to create a token for non-key words recognized by wscanner.
   * @return the Words instance.
   */
  public static Words getCaseInsensitive(final Parser<?> wscanner, 
      final String[] ops, final String[] keywords, FromString<?> toWord){
    return Words.getCaseInsensitive(wscanner, ops, keywords, toWord);
  }
  /**
   * Creates a Words object for lexing the operators with names specified in ops,
   * and for lexing the keywords case sensitively. 
   * Keywords and operators are lexed as TokenReserved.
   * Words that are not among the keywords are lexed as TokenWord. 
   * @param wscanner the scanner for a word in the language.
   * @param ops the operator names.
   * @param keywords the keyword names.
   * @param toWord the FromString object used to create a token for non-key words recognized by wscanner.
   * @return the Words instance.
   */
  public static Words getCaseSensitive(final Parser<?> wscanner,
      final String[] ops, final String[] keywords, FromString<?> toWord){
    return Words.getCaseSensitive(wscanner, ops, keywords, toWord);
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
  public static Parser<Tok> lexer(final String name, final Parser<?> s, final Tokenizer tn){
    return lexer(name, s, tn, "lexer error");
  }
  /**
   * Transform the recognized character range of scanner s to a token object
   * with a Tokenizer. 
   * If the Tokenizer.toToken() returns null, scan fails.
   * @param s the scanner to transform.
   * @param tn the Tokenizer object.
   * @return the new Scanner.
   */
  public static Parser<Tok> lexer(final Parser<?> s, final Tokenizer tn){
    return lexer("lexer", s, tn);
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
  public static Parser<Tok> lexer(final Parser<?> s, final Tokenizer tn, final String err){
    return lexer("lexer", s, tn, err);
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
  public static Parser<Tok> lexer(final String name, final Parser<?> s, final Tokenizer tn, 
      final String err){
    return new Parser<Tok>(name){
      boolean apply(final ParseContext ctxt){
        final int ind = ctxt.getIndex();
        final int from = ctxt.getAt();
        final Object ret = ctxt.getReturn();
        final int at = ctxt.getAt();
        final int step = ctxt.getStep();
        final Object ustate = ctxt.getUserState();
        //final AbstractParsecError error = ctxt.getError();
        if(!s.parse(ctxt)) return false;
        final int len = ctxt.getAt() - from;
        final Object tok = tn.toToken(ctxt.getSource(), from, len);
        if(tok == null){
          ctxt.set(step, at, ret, ustate, ParsecError.raiseExpecting(ind, err));
          return false;
        }
        final Tok ptok = new Tok(at, len, tok);
        ctxt.setStep(step+1);
        ctxt.setReturn(ptok);
        return true;
      }
    };
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
  public static Parser<Tok[]> lexeme(final String name, 
      final Parser<?> delim, final Parser<Tok> s){
    return delim.optional().seq(name, Parsers.sepEndBy(name, Tok.class, delim, s));
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
  public static Parser<Tok[]> lexeme(final Parser<?> delim, final Parser<Tok> s){
    return lexeme("lexeme", delim, s);
  }
}
