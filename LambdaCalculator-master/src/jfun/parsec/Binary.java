package jfun.parsec;

/**
 * This interface represents a binary operation on the same type.
 * <p>
 * Implement this interface for binary operator instead of {@link Map2} to save keystrokes.
 * <p>
 * @since version 1.0
 * @author Ben Yu
 * Apr 6, 2006 7:20:24 PM
 * @param <T>
 */
public interface Binary<T> extends Map2<T,T,T>{

}
