package jfun.parsec.trace;
/**
 * This interface is used to trace parser result.
 * <p>
 * @author Ben Yu
 * @since version 1.1
 * May 9, 2006 7:18:31 PM
 */
public interface Trace<T> {
  /**
   * This method is called when parser failed.
   * @param except the pseudo exception object. null if no exception.
   * @param src the text being parsed.
   * @param index the index where the parser terminates.
   * @param steps the logical steps consumed.
   * @param offset the physical offset consumed.
   */
  public void onError(Object except, CharSequence src, int index, int steps, int offset);
  
  /**
   * This method is called when parser succeeded.
   * @param result the parser result.
   * @param src the text being parsed.
   * @param index the index where the parser terminates.
   * @param steps the logical steps consumed.
   * @param offset the physical offset consumed.
   */
  public void onSuccess(T result, CharSequence src, int index, int steps, int offset);
}
