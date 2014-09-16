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

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

import jfun.parsec.tokens.Tokens;


/**
 * @author Ben Yu
 *
 * Dec 19, 2004
 */
final class Keywords implements java.io.Serializable{
  private interface StringCase {
    Comparator<String> getComparator();
    String getKey(String k);
    jfun.parsec.Map<String,Object> getMap(java.util.Map<String,Object> m);
  }
  private static final StringCase case_sensitive = new CaseSensitive();
  private static final StringCase case_insensitive = new CaseInsensitive();
  private static final class CaseSensitive implements StringCase{
    private static final Comparator<String> comparator = new Comparator<String>(){
      public int compare(String a, String b){
        if(a==b) return 0;
        else if(a==null) return -1;
        else if(b==null) return 1;
        else return a.compareTo(b);
      }
    };
    public Comparator<String> getComparator(){return comparator;}
    public String getKey(final String k){return k;}
    public jfun.parsec.Map<String,Object> getMap(final java.util.Map<String,Object> m){
      return Maps.jmap(m);
    }
  };
  private static final class CaseInsensitive implements StringCase{
    private static final Comparator<String> comparator = new Comparator<String>(){
      public int compare(String a, String b){
        if(a==b) return 0;
        else if(a==null) return -1;
        else if(b==null) return 1;
        else return a.compareToIgnoreCase(b);
      }
    };
    public Comparator getComparator(){return comparator;}
    public String getKey(final String k){return k.toLowerCase();}
    public jfun.parsec.Map<String,Object> getMap(final java.util.Map<String,Object> m){
      return new jfun.parsec.Map<String,Object>(){
        public Object map(final String key){
          return m.get(key.toLowerCase());
        }
      };
    }
  };
  private static StringCase getStringCase(boolean cs){
    return cs?case_sensitive:case_insensitive;
  }
  private static String[] nub(final String[] names, Comparator<String> c){
    final TreeSet<String> set = new TreeSet<String>(c);
    set.addAll(Arrays.asList(names));
    final String[] _names = new String[set.size()];
    set.toArray(_names);
    return _names;
  }
  /*
  //this method tries each keyword sequentially. while getInstance() scans a word and then lookup in a hash table.
  static WordsData instance(final String[] names, boolean cs){
    final StringCase scase = getStringCase(cs);
    final String[] _names = nub(names, scase.getComparator());
    final HashMap map = new HashMap();
    final Lexer[] lxs = new Lexer[_names.length];
    for(int i=0; i<_names.length; i++){
      final String n = _names[i];
      final Token tok = new TokenReserved(n);
      map.put(scase.getKey(n), tok);
      final Parser kw = cs?Scanners.isString(n):Scanners.isStringCI(n);
      final Lexer lx = Lexers.toLexer(
          Scanners.delimited(kw), ConstTokenizer.instance(tok)
      );
      lxs[i] = lx;
    }
    return new WordsData(scase.getMap(map), lxs);
  }*/
  static WordsData getWordsInstance(final Parser<?> wscanner, final String[] keywords, boolean cs){
    return getWordsInstance("words", wscanner, keywords, cs);
  }
  static WordsData getKeywordsInstance(final Parser<?> wscanner, final String[] keywords, boolean cs){
    return getKeywordsInstance("keywords", wscanner, keywords, cs);
  }
  private static final FromString _unknown_keyword = new String2Value(null);
  static WordsData getWordsInstance(final String name, final Parser<?> wscanner, final String[] keywords, boolean cs){
    return getInstance(name, wscanner, keywords, cs, String2TokenWord.instance());
  }
  static WordsData getKeywordsInstance(final String name, final Parser<?> wscanner, final String[] keywords, boolean cs){
    return getInstance(name, wscanner, keywords, cs, _unknown_keyword);
  }
  static <T> WordsData getInstance(final String name, final Parser<?> wscanner, final String[] names, boolean cs,
      final FromString<T> stok){
    final StringCase scase = getStringCase(cs);
    final String[] _names = nub(names, scase.getComparator());
    final HashMap<String,Object> map = new HashMap<String,Object>();
    for(int i=0; i<_names.length; i++){
      final String n = _names[i];
      final Object tok = Tokens.reserved(n);
      map.put(scase.getKey(n), tok);
    }
    final jfun.parsec.Map<String,Object> fmap = scase.getMap(map);
    final Tokenizer tn = new Tokenizer(){
      public Object toToken(final CharSequence cs,
          final int from, final int len){
        final String txt = cs.subSequence(from, from+len).toString();
        final Object t = fmap.map(txt);
        if(t!=null) return t;
        else return stok.fromString(from, len, txt);
      }
    };
    final Parser<Tok> lx = Lexers.lexer(name, wscanner, tn);
    return new WordsData(fmap, new Parser[]{lx});    
  }
}
