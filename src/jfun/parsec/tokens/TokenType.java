package jfun.parsec.tokens;

/**
 * This represents all pre-built token types.
 * <p>
 * @author Ben Yu
 * Apr 27, 2006 8:59:13 PM
 */
public enum TokenType {
  /**
   * reserved word
   */
  Reserved, 
  /**
   * regular word
   */
  Word, 
  /**
   * integral number literal
   */
  Integer, 
  /**
   * decimal number literal
   */
  Decimal
}
