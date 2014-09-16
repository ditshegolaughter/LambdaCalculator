package jfun.parsec;

/**
 * This interface represents a unary operation on the same type.
 * <p>
 * Implement this interface for unary operator instead of {@link Map} to save keystrokes.
 * <p>
 * @since version 1.0
 * @author Ben Yu
 * Apr 6, 2006 7:20:47 PM
 * @param <T>
 */
public interface Unary<T> extends Map<T,T> {

}
