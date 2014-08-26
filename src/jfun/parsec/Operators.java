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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import jfun.parsec.tokens.Tokens;

/**
 * @author Ben Yu
 *
 * Dec 19, 2004
 */
final class Operators implements java.io.Serializable{
  static WordsData instance(final String[] names){
    final HashMap<String,Object> operators = new HashMap<String,Object>();
    final String[] ops = sort(names);
    final Parser[] lxs = new Parser[ops.length];
    for(int i=0; i<ops.length; i++){
      final String s = ops[i];
      final Parser<?> scanner = s.length()==1?
          Scanners.isChar(s.charAt(0)): Scanners.isString(s);
      final Object tok = Tokens.reserved(s);
      operators.put(s, tok);
      final Parser<Tok> lx = Lexers.lexer(scanner, ConstTokenizer.instance(tok));
      lxs[i] = lx;
    }
    return new WordsData(Maps.jmap(operators), lxs);
  }
  private static final Comparator<Object> com = new Comparator<Object>(){
    public int compare(final Object a, final Object b){
      if(a==b) return 0;
      if(a==null) return -1;
      if(b==null) return 1;
      return a.toString().length() - b.toString().length();
    }
  };
  private static final class Suite{
    private final ArrayList<String> list = new ArrayList<String>();
    //contained are behined containers.
    Suite(String s){
      if(s.length()>0)
        list.add(s);
    }
    boolean add(final String v){
      if(v.length()==0) return true;
      for(int i=list.size()-1; i>=0; i--){
        final String s = (String)list.get(i);
        if(s.startsWith(v)){
          if(s.length()==v.length()) return true;  // ignore duplicates
          list.add(i+1, v);
          return true;
        }
      }
      return false;
    }
    int size(){return list.size();}
    String get(int i){return (String)list.get(i);}
  }
  private static final class Suites{
    private final ArrayList<Suite> list = new ArrayList<Suite>();
    //bigger suite first
    void add(final String v){
      for(int i=0; i<list.size(); i++){
        final Suite suite = (Suite)list.get(i);
        if(suite.add(v)) return;
      }
      list.add(new Suite(v));
    }
    String[] toArray(){
      final ArrayList<String> result = new ArrayList<String>();
      for(int i=list.size()-1;i>=0;i--){
        final Suite suite = (Suite)list.get(i);
        for(int j=0; j<suite.size(); j++){
          result.add(suite.get(j));
        }
      }
      final String[] ret = new String[result.size()];
      result.toArray(ret);
      return ret;
    }
  }
  public static String[] sort(final String[] names){
    //short name first, unless it is fully contained in a longer name
    final String[] _names = (String[])names.clone();
    Arrays.sort(_names, com);
    final Suites suites = new Suites();
    for(int i=_names.length-1; i>=0; i--){
      suites.add(_names[i]);
    }
    return suites.toArray();
  }
}
