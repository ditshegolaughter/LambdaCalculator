package jfun.parsec.tokens;

import jfun.parsec.Tokenizer;

/**
 * The facade class for all pre-built Tokenizer implementations.
 * <p>
 * @author Ben Yu
 * Apr 27, 2006 7:40:16 PM
 * @since version 0.6
 */
public class Tokenizers {

  /**
   * Create a tokenizer that tokenizes the recognized character range
   * to a TypedToken object.
   * @param type the token type.
   * @return the Tokenizer object.
   */
  public static <T> Tokenizer forTypedToken(final T type){
    return new Tokenizer(){
      public Object toToken(CharSequence cs, int from, int len) {
        return Tokens.createTypedToken(cs.subSequence(from, from+len).toString(), type);
      }
    };
  }
  private static final Tokenizer reserved = forTypedToken(TokenType.Reserved);
  private static final Tokenizer word = forTypedToken(TokenType.Word);
  private static final Tokenizer integer = forTypedToken(TokenType.Integer);
  private static final Tokenizer decimal = forTypedToken(TokenType.Decimal);
  /**
   * Get the Tokenizer object that creates a reserved word token.
   */
  public static Tokenizer forReservedWord(){
    return reserved;
  }
  /**
   * Get the Tokenizer object that creates a word token.
   */
  public static Tokenizer forWord(){
    return word;
  }
  /**
   * Get the Tokenizer object that creates an integer literal token.
   */
  public static Tokenizer forInteger(){
    return integer;
  }
  /**
   * Get the Tokenizer object that creates a decimal number literal token.
   */
  public static Tokenizer forDecimal(){
    return decimal;
  }
  /**
   * Creates a tokenizer that's gonna tokenize a single quoted character literal possibly with escape character '\'
   * @return the tokenizer instance.
   */
  public static Tokenizer forChar(){
    return TokenCharLiteral.getTokenizer();
  }
  /**
   * Get the Tokenizer object that creates a string object.
   */
  public static Tokenizer forString(){
    return TokenString.getTokenizer();
  }
  /**
   * Get the Tokenizer object that interprets the recognized character range
   * as a decimal integer and translate it to a long value.
   */
  public static Tokenizer forDecLong(){
    return TokenLong.getDecTokenizer();
  }
  /**
   * Get the Tokenizer object that interprets the recognized character range
   * as a hex integer and translate it to a long value.
   */
  public static Tokenizer forHexLong(){
    return TokenLong.getHexTokenizer();
  }
  /**
   * Get the Tokenizer object that interprets the recognized character range
   * as a oct integer and translate it to a long value.
   */
  public static Tokenizer forOctLong(){
    return TokenLong.getOctTokenizer();
  }
  /**
   * Get the Tokenizer object that converts a string literal quoted by '"'
   * to a string object.
   * back-slash character is escaped.
   */
  public static Tokenizer forSimpleStringLiteral(){
    return TokenStringLiteral.getDoubleQuoteTokenizer();
  }
  /**
   * Get the Tokenizer object that converts a sql string literal quoted by single quote
   * to a string object.
   * double single quote is interpreted as one single quote.
   */
  public static Tokenizer forSqlStringLiteral(){
    return TokenStringLiteral.getSqlTokenizer();
  }
  /**
   * Get the Tokenizer object that converts a string literal quoted by a pair of
   * opening and closing characters.
   * The Tokenizer result is a {@link TokenQuoted} object.
   */
  public static Tokenizer forQuotedString(char open, char close){
    return TokenQuoted.getTokenizer(open, close);
  }
  /**
   * Get the Tokenizer object that converts a string literal quoted by a pair of
   * opening and closing strings.
   * The Tokenizer result is a {@link TokenQuoted} object.
   */
  public static Tokenizer forQuotedString(String open, String close){
    return TokenQuoted.getTokenizer(open, close);
  }
}
