/*****************************************************************************
 * Copyright (C) Zephyr Business Solutions Corp. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/
/*
 * Created on 2004-11-12
 *
 * Author Ben Yu
 */
package jfun.parsec;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Stack;

/**
 * ParserException is thrown when a grammar error happens.
 * <p>
 * @author Ben Yu
 *
 * 2004-11-12
 */
public class ParserException extends RuntimeException {
  private final ParseError err;
  private final Pos pos;
  private final String module;
  private final Stack frames = new Stack();
  void pushFrame(ParsingFrame frame){
    frames.push(frame);
  }
  /**
   * Get the parsing trace.
   * @return the parsing trace
   * with objects of {@link ParsingFrame} as the elements.
   */
  public Stack getParsingTrace(){
    return frames;
  }
  
  /**
   * Print the parsing trace.
   * @param out the output stream.
   */
  public void printParsingTrace(PrintStream out){
    printParsingTrace(new java.io.PrintWriter(out, true));
  }
  /**
   * Print the resultion trace.
   * @param out the output writer.
   */
  public void printParsingTrace(java.io.PrintWriter out){
    final int size = frames.size();
    for(int i=0; i<size; i++){
      out.println(frames.get(i));
    }
  }
  /**
   * Prints the parsing trace to the standard error output.
   */
  public void printParsingTrace(){
    printParsingTrace(System.err);
  }
  public void printStackTrace(PrintStream s) {
    printParsingTrace(s);
    super.printStackTrace(s);
  }
  public void printStackTrace(PrintWriter s) {
    printParsingTrace(s);
    super.printStackTrace(s);
  }

  /**
   * Create a ParserException object.
   * @param err the ParseError object.
   * @param mname the module name.
   * @param pos the position.
   */
  public ParserException(final ParseError err, 
      final String mname, final Pos pos) {
    this.err = err;
    this.pos = pos;
    this.module = mname;
  }

  /**
   * Create a ParserException object.
   * @param message the error message.
   * @param err the ParseError object.
   * @param mname the module name.
   * @param pos the position.
   */
  public ParserException(String message, final ParseError err, 
      final String mname, final Pos pos) {
    super(message);
    this.err = err;
    this.pos = pos;
    this.module = mname;
  }


  /**
   * @param cause the exception that causes this.
   * @param err the ParseError object.
   * @param mname the module name.
   * @param pos the position.
   */
  public ParserException(Throwable cause, final ParseError err, 
      final String mname, final Pos pos) {
    super(cause);
    this.err = err;
    this.pos = pos;
    this.module = mname;
  }

  /**
   * @param message the error message.
   * @param cause the exception that causes this.
   * @param err the ParseError object.
   * @param mname the module name.
   * @param pos the position.
   */
  public ParserException(String message, Throwable cause, final ParseError err, 
      final String mname, final Pos pos) {
    super(message, cause);
    this.err = err;
    this.pos = pos;
    this.module = mname;
  }

  /**
   * Get the ParseError object.
   * @return Returns the err.
   */
  public final ParseError getError() {
    return err;
  }
  /**
   * Get the default formatted error message.
   * @see java.lang.Throwable#getMessage()
   */
  public String getMessage(){
    return getErrorMessage();
  }
  private String getErrorMessage(){
    final String msg = super.getMessage();
    final StringBuffer buf = new StringBuffer();
    if(msg != null) buf.append(msg).append("\n");
    if(module != null)
      buf.append(module).append(" - ");
    buf.append(DefaultShowError.show(err, pos));
    return buf.toString();
  }
  
  /**
   * Gets the module name.
   * @return the module name.
   */
  public String getModuleName() {
    return module;
  }
  /**
   * Gets the line number of the error.
   * @return the line number.
   */
  public int getLineNo() {
    return pos.getLineNo();
  }
  /**
   * Gets the column number of the error.
   * @return the column number.
   */
  public int getColumnNo(){
    return pos.getColumnNo();
  }
}
