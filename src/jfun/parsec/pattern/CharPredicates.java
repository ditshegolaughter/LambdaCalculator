/*****************************************************************************
 * Copyright (C) Zephyr Business Solutions Corp. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/
/*
 * Created on Dec 11, 2004
 *
 * Author Ben Yu
 */
package jfun.parsec.pattern;

/**
 * This class provides some common CharPredicate implementations.
 * @author Ben Yu
 *
 * Dec 11, 2004
 */
public final class CharPredicates {
  /**
   * == a.
   */
  public static CharPredicate isChar(final char a){
    return new CharPredicate(){
      public boolean isChar(final char c){
        return c==a;
      }
    };
  }
  /**
   * != a.
   */
  public static CharPredicate notChar(final char a){
    return new CharPredicate(){
      public boolean isChar(final char c){
        return c!=a;
      }
    };
  } 
  /**
   * between a and b inclusive.
   */
  public static CharPredicate range(final char a, final char b){
    return new CharPredicate(){
      public boolean isChar(final char c){
        return c>=a && c <= b;
      }
    };
  }
  /**
   * between 0 and 9.
   */
  public static CharPredicate isDigit(){
    return range('0','9');
  }
  /**
   * not between a and b inclusive.
   */
  public static CharPredicate notRange(final char a, final char b){
    return new CharPredicate(){
      public boolean isChar(final char c){
        return !(c>=a && c <= b);
      }
    };
  }
  /**
   * among chars.
   */
  public static CharPredicate among(final char[] chars){
    return new CharPredicate(){
      public boolean isChar(final char c){
        return charAmong(c, chars);
      }
    };
  }
  /**
   * not among chars.
   */
  public static CharPredicate notAmong(final char[] chars){
    return new CharPredicate(){
      public boolean isChar(final char c){
        return !charAmong(c, chars);
      }
    };
  }
  static boolean charAmong(final char c, final char[] chars){
    for(int i=0; i<chars.length; i++){
      if(c==chars[i]) return true;
    }
    return false;
  }
  /**
   * is hex digit.
   */
  public static CharPredicate isHexDigit(){
    return new CharPredicate(){
      public boolean isChar(final char c){
        return c>='0' && c <= '9' || c >='a' && c <='f' || c>='A' && c <= 'F';
      }
    };
  }
  /**
   * [A-Z].
   */
  public static CharPredicate isUppercase(){
    return new CharPredicate(){
      public boolean isChar(char c){return Character.isUpperCase(c);}
    };
  }
  /**
   * [a-z].
   */
  public static CharPredicate isLowercase(){
    return new CharPredicate(){
      public boolean isChar(char c){return Character.isLowerCase(c);}
    };
  }
  /**
   * is white space.
   */
  public static CharPredicate isWhitespace(){
    return new CharPredicate(){
      public boolean isChar(char c){return Character.isWhitespace(c);}
    };
  }
  /**
   * [a-zA-Z].
   */
  public static CharPredicate isAlpha(){
    return new CharPredicate(){
      public boolean isChar(final char c){
        return c <='z' && c>='a' || c <='Z' && c >= 'A';
      }
    };
  }
  /**
   * [a-zA-Z_].
   */
  public static CharPredicate isAlpha_(){
    return new CharPredicate(){
      public boolean isChar(final char c){
        return c=='_' || c <='z' && c>='a' || c <='Z' && c >= 'A';
      }
    };
  }
  /**
   * is letter.
   */
  public static CharPredicate isLetter(){
    return new CharPredicate(){
      public boolean isChar(final char c){
        return Character.isLetter(c);
      }
    };
  }
  /**
   * [a-zA-Z0-9_]
   */
  public static CharPredicate isAlphaNumeric(){
    return new CharPredicate(){
      public boolean isChar(final char c){
        return c=='_' || c>='A' && c <= 'Z' || c>='a' && c<='z' || c>='0' && c<='9';
      }
    };
  }
  /**
   * Negate a CharPredicate object.
   */
  public static CharPredicate not(final CharPredicate cp){
    return new CharPredicate(){
      public boolean isChar(final char c){
        return !cp.isChar(c);
      }
    };
  }
  /**
   * Logical and of two CharPredicate objects.
   */
  public static CharPredicate and(final CharPredicate cp1, final CharPredicate cp2){
    return new CharPredicate(){
      public boolean isChar(final char c){
        return cp1.isChar(c) && cp2.isChar(c);
      }
    };
  }
  /**
   * Logical or of two CharPredicate objects.
   */
  public static CharPredicate or(final CharPredicate cp1, final CharPredicate cp2){
    return new CharPredicate(){
      public boolean isChar(final char c){
        return cp1.isChar(c) || cp2.isChar(c);
      }
    };
  }

  /**
   * Logical and of an array of CharPredicate objects.
   */
  public static CharPredicate and(final CharPredicate... cps){
    if(cps.length==0) return always();
    else if(cps.length==1) return cps[0];
    return new CharPredicate(){
      public boolean isChar(final char c){
        for(int i=0;i<cps.length;i++){
          if(!cps[i].isChar(c)) return false;
        }
        return true;
      }
    };
  }
  /**
   * Logical or of an array of CharPredicate objects.
   */
  public static CharPredicate or(final CharPredicate... cps){
    if(cps.length==0) return never();
    else if(cps.length==1) return cps[0];
    return new CharPredicate(){
      public boolean isChar(final char c){
        for(int i=0;i<cps.length;i++){
          if(cps[i].isChar(c)) return true;
        }
        return false;
      }
    };
  }
  /**
   * A predicate that always returns false.
   */
  public static CharPredicate never(){
    return _never;
  }
  /**
   * A predicate that always returns true.
   */
  public static CharPredicate always(){
    return _always;
  }
  private static final CharPredicate _never = new CharPredicate(){
    public boolean isChar(final char c){return false;}
  };
  private static final CharPredicate _always = new CharPredicate(){
    public boolean isChar(final char c){return true;}
  };
}
