package jfun.parsec.trace;

import java.io.PrintWriter;

/**
 * This class provides some common trace implementations.
 * <p>
 * @author Ben Yu
 * @since version 1.1
 * May 9, 2006 7:25:19 PM
 */
public class Traces {
  private static final int LDEADING = 32;
  private static String getLeadingChars (CharSequence src, int ind, int n) {
    int len = src.length();
    if (ind >= len) {
      return "<EOF>";
    }
    if (n + ind >= len) {
      n = len - ind;
      return src.subSequence(ind, ind+n).toString();
    }
    return src.subSequence (ind, ind+n).toString() + "...";
  }
  private static void printStatus(final PrintWriter out, CharSequence src, int ind, int steps, int offset){
    out.print('[');
    out.print(getLeadingChars (src, ind, LDEADING));
    out.println(']');
    out.println("steps="+steps+", offset="+offset);
  }
  private static void printErrorTrace(
      final int min_steps, final String name, final PrintWriter out, Object exception, CharSequence src, int ind, int steps, int offset) {
    if(steps < min_steps) return;
    out.print(name);
    out.print(": ");
    if(exception != null){
      out.println("exception raised.");
    }
    printStatus(out, src, ind, steps, offset);
    out.flush();
  }
  private static void printResultTrace(final String name, final PrintWriter out, Object result, CharSequence src, int ind, int steps, int offset) {
    out.println(name + " => "+ result);
    printStatus(out, src, ind, steps, offset);
    out.flush();
  }
  /**
   * Create a Trace object that prints error message to output.
   * @param name the name in the trace message.
   * @param out the writer for the output.
   * @param min_steps the minimal logical steps consumed to trigger the trace message.
   * @return the Trace object.
   */
  public static Trace<Object> printError (final String name, final PrintWriter out, final int min_steps) {
    return new EmptyTrace<Object>(){
      @Override
      public void onError(Object exception, CharSequence src, int ind, int steps, int offset){
        printErrorTrace(min_steps, name, out, exception, src, ind, steps, offset);
      }
    };
  }
  /**
   * Create a Trace object that prints trace message to output when parser succeeds.
   * @param name the name in the trace message.
   * @param out the writer for the output.
   * @return the Trace object.
   */
  public static Trace<Object> printResult(final String name, final PrintWriter out){
    return new EmptyTrace<Object>(){
      @Override
      public void onSuccess(Object result, CharSequence src, int ind, int steps, int offset){
        printResultTrace(name, out, result, src, ind, steps, offset);
      }
    };
  }
  /**
   * Create a Trace object that prints trace message to output.
   * The minimal logical steps to trigger an error message is 1.
   * @param name the name in the trace message.
   * @param out the writer for the output.
   * @return the Trace object.
   */
  public static Trace<Object> printTrace(final String name, final PrintWriter out){
    return new Trace<Object>(){
      public void onError(Object exception, CharSequence src, int ind, int steps, int offset){
        printErrorTrace(1, name, out, exception, src, ind, steps, offset);
      }
      public void onSuccess(Object result, CharSequence src, int ind, int steps, int offset){
        printResultTrace(name, out, result, src, ind, steps, offset);
      }
    };
  }
}
