/**
 * 
 */
package jfun.parsec;

final class ToReturn<T> implements ToParser<T, T> {
  private final String name;

  ToReturn(String name) {
    super();
    this.name = name;
  }

  public Parser<T> toParser(T v) {
    return Parsers.retn(name, v);
  }
}