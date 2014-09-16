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



/**
 * This helper class provides convenient api's to build lexer
 * and parsers for keywords and operators.
 * @author Ben Yu
 *
 * Dec 19, 2004
 */
public final class Words implements java.io.Serializable{
  private final Map<String, Object> words;
  private final Parser<Tok> lexer;
  private Words(final Map<String, Object> map, final Parser<Tok> lexer){
    this.words = map;
    this.lexer = lexer;
  }

  /**
   * gets the token object identified by the token text. 
   * This text is the operator or the keyword.
   * @param name the token text.
   * @return the token object. 
   * @exception IllegalArgumentException if the token object does not exist.
   */
  public Object getToken(final String name){
    final Object p = words.map(name);
    if(p==null) throw new IllegalArgumentException("token " + name + " unavailable");
    return p;
  }
  /**
   * gets the lexer object.
   * @return the lexer object.
   */
  public Parser<Tok> getLexer(){
    return lexer;
  }
  private static boolean same(final String a, final String b, final boolean cs){
    if(cs){
      return a.equals(b);
    }
    else
      return a.equalsIgnoreCase(b);
  }
  private static void checkDup(final String[] a, final String[] b, boolean cs){
    for(int i=0; i<a.length; i++){
      final String s1 = a[i];
      for(int j=0; j<b.length; j++){
        final String s2 = b[j];
        if(same(s1, s2, cs)) throw new IllegalArgumentException(s1 + " duplicated");
      }
    }
  }
  /**
   * Creates a Words object for lexing the operators with names specified in ops.
   * Operators are lexed as TypedToken with TokenType.Reserved type.
   * @param ops the operator names.
   * @return the Words instance.
   */
  static Words getOperators(final String[] ops){
    final WordsData data = Operators.instance(ops);
    //return toWords(data);
    return new Words(data.getTokens(), Parsers.plus(data.getLexers()));
  }
  private static final Parser<?> default_word = Scanners.isPattern(Patterns.isWord(), "word");
  /**
   * Creates a Words object for lexing the operators with names specified in ops,
   * and for lexing the keywords case insensitively.
   * Keywords and operators are lexed as TypedToken with TokenType.Reserved type.
   * Words that are not among the keywords are lexed as TokenWord. 
   * A word is defined as an alpha numeric string that starts with [_a-zA-Z],
   * with 0 or more [0-9_a-zA-Z] following. 
   * @param ops the operator names.
   * @param keywords the keyword names.
   * @return the Words instance.
   */
  static Words getCaseInsensitive(
      final String[] ops, final String[] keywords){
    return instance(default_word, ops, keywords, false, String2TokenWord.instance());
  }
  /**
   * Creates a Words object for lexing the operators with names specified in ops,
   * and for lexing the keywords case sensitively. 
   * Keywords and operators are lexed as TypedToken with TokenType.Reserved type.
   * Words that are not among the keywords are lexed as TokenWord. 
   * A word is defined as an alpha numeric string that starts with [_a-zA-Z],
   * with 0 or more [0-9_a-zA-Z] following.
   * @param ops the operator names.
   * @param keywords the keyword names.
   * @return the Words instance.
   */
  static Words getCaseSensitive(
      final String[] ops, final String[] keywords){
    return instance(default_word, ops, keywords, true, String2TokenWord.instance());
  }
  /**
   * Creates a Words object for lexing the operators with names specified in ops,
   * and for lexing the keywords case insensitively.
   * Keywords and operators are lexed as TypedToken with TokenType.Reserved type.
   * Words that are not among the keywords are lexed as TokenWord. 
   * @param wscanner the scanner for a word in the language.
   * @param ops the operator names.
   * @param keywords the keyword names.
   * @return the Words instance.
   */
  static Words getCaseInsensitive(final Parser<?> wscanner, 
      final String[] ops, final String[] keywords){
    return instance(wscanner, ops, keywords, false, String2TokenWord.instance());
  }
  /**
   * Creates a Words object for lexing the operators with names specified in ops,
   * and for lexing the keywords case sensitively. 
   * Keywords and operators are lexed as TypedToken with TokenType.Reserved type.
   * Words that are not among the keywords are lexed as TokenWord. 
   * @param wscanner the scanner for a word in the language.
   * @param ops the operator names.
   * @param keywords the keyword names.
   * @return the Words instance.
   */
  static Words getCaseSensitive(final Parser<?> wscanner,
      final String[] ops, final String[] keywords){
    return instance(wscanner, ops, keywords, true, String2TokenWord.instance());
  }
  
  static Words getCaseSensitive(final Parser<?> wscanner, String[] ops, final String[] keywords, 
      FromString<?> toWord){
    return instance(wscanner, ops, keywords, true, toWord);
  }
  static Words getCaseInsensitive(final Parser<?> wscanner, String[] ops, String[] keywords, 
      FromString<?> toWord){
    return instance(wscanner, ops, keywords, false, toWord);
  }
  private static Words instance(final Parser<?> wscanner, 
      final String[] ops, final String[] keywords, final boolean cs, 
      FromString<?> toWord){
    checkDup(ops, keywords, true);
    final WordsData data = combine(Operators.instance(ops),
        Keywords.getInstance("word", wscanner, keywords, cs, toWord));
    return toWords(data);
  }
  private static Words toWords(WordsData data){
    return new Words(data.getTokens(), Parsers.plus(data.getLexers()));
  }
  private static WordsData combine(final WordsData w1, final WordsData w2){
    final Map<String, Object> map1 = w1.getTokens();
    final Map<String, Object> map2 = w2.getTokens();
    final Parser<Tok>[] l1 = w1.getLexers();
    final Parser<Tok>[] l2 = w2.getLexers();
    final Map<String, Object> map = new Map<String, Object>(){
      public Object map(final String k){
        final Object r = map1.map(k);
        if(r!=null) return r;
        return map2.map(k);
      }
    };
    final Parser<Tok>[] l = new Parser[l1.length+l2.length];
    /*for(int i=0; i<l1.length; i++){
      l[i] = l1[i];
    }
    for(int i=0; i<l2.length; i++){
      l[i+l1.length] = l2[i];
    }*/
    System.arraycopy(l1, 0, l, 0, l1.length);
    System.arraycopy(l2, 0, l, l1.length, l2.length);
    return new WordsData(map, l);
  }
}
