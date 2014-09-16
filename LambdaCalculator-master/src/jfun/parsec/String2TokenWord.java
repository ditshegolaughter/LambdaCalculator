package jfun.parsec;

import jfun.parsec.tokens.TokenType;
import jfun.parsec.tokens.Tokens;
import jfun.parsec.tokens.TypedToken;

/**
 * This implementation of FromString creates a {@link jfun.parsec.tokens.TypedToken} instance
 * from the string.
 * <p>
 * @author Ben Yu
 * Mar 29, 2006 9:54:23 PM
 */
public class String2TokenWord implements FromString<TypedToken<TokenType>> {
  public TypedToken<TokenType> fromString(int from, int len, String s) {
    return Tokens.word(s);
  }
  public String toString(){
    return getClass().getName();
  }
  private String2TokenWord(){}
  private static final String2TokenWord singleton = new String2TokenWord();
  /**
   * Get an instance of this implementation.
   */
  public static FromString<TypedToken<TokenType>> instance(){
    return singleton;
  }
}
