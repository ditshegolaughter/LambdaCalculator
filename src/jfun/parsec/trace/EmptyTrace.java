package jfun.parsec.trace;

/**
 * This class provides an empty implementation of Trace.
 * <p>
 * @author Ben Yu
 * @since version 1.1
 * May 9, 2006 7:18:31 PM
 */
public abstract class EmptyTrace<T> implements Trace<T>{
  /**
   * Override this method when only error tracing is needed.
   * @param except the pseudo exception object. null if no exception.
   * @param src the text being parsed.
   * @param index the index where the parser terminates.
   * @param steps the logical steps consumed.
   * @param offset the physical offset consumed.
   */
  public void onError(Object except, CharSequence src, int index, int steps, int offset){}
  
  /**
   * Override this method when tracing is only needed when the parser succeeds.
   * @param result the parser result.
   * @param src the text being parsed.
   * @param index the index where the parser terminates.
   * @param steps the logical steps consumed.
   * @param offset the physical offset consumed.
   */
  public void onSuccess(T result, CharSequence src, int index, int steps, int offset){}
}
