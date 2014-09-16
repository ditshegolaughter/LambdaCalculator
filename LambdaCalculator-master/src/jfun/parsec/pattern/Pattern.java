/*****************************************************************************
 * Copyright (C) Zephyr Business Solutions Corp. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/
/*
 * Created on Dec 16, 2004
 *
 * Author Ben Yu
 */
package jfun.parsec.pattern;

/**
 * A Pattern object encapsulates an algorithm
 * to recognize certain string pattern.
 * When fed with a character range,
 * a Pattern object either fails to match,
 * or matches with the match length returned.
 * <p>
 * Pattern object is used for terminal level parsers. 
 * A Pattern differs from a Parser in that it does not return object,
 * and it simply reports mismatch whenenver fails.
 * While Parser cannot be implemented directly, 
 * Pattern can be implemented directly by user code.
 * @author Ben Yu
 *
 * Dec 16, 2004
 */
public abstract class Pattern implements java.io.Serializable{
  /**
   * returned by match() method when match fails.
   */
  public static final int MISMATCH = -1;
  /**
   * The actual length of the pattern string is len - from.
   * @param src the source string.
   * @param len the length of the sequence.
   * NOTE: the range is [from, len], not [from,from+len].
   * @param from the starting index in the sequence.
   * @return the number of characters matched. MISMATCH otherwise.
   */
  public abstract int match(CharSequence src, int len, int from);
  /**
   * First matches this pattern. 
   * If succeed, match the remaining input against Pattern p2.
   * Fails if either this or p2 fails.
   * Succeed with the entire match length,
   * which is the sum of the match length of this and p2.
   * @param p2 the next Pattern object to match.
   * @return the new Pattern object.
   */
  public final Pattern seq(final Pattern p2){
    return Patterns.seq(this, p2);
  }
  /**
   * Match with 0 length even if this pattern mismatches.
   * @return the new Pattern object.
   */
  public final Pattern optional(){
    return Patterns.optional(this);
  }
  /**
   * Matches this pattern for 0 or more times.
   * Return the total match length.
   * @return the new Pattern object.
   */
  public final Pattern many(){
    return Patterns.many(this);
  }
  /**
   * Matches this pattern for at least min times.
   * Return the total match length.
   * @param min the minimal number of times to match.
   * @return the new Pattern object.
   */
  public final Pattern many(int min){
    return Patterns.many(min, this);
  }
  /**
   * Matches this pattern for 1 or more times.
   * Return the total match length.
   * @return the new Pattern object.
   */
  public final Pattern many1(){
    return Patterns.many(1, this);
  }
  /**
   * Matches this pattern for up to max times.
   * Return the total match length.
   * @param max the maximal number of times to match.
   * @return the new Pattern object.
   */
  public final Pattern some(final int max){
    return Patterns.some(max, this);
  }
  /**
   * Matches this pattern for at least min times
   * and at most max times.
   * Return the total match length.
   * @param min the minimal number of times to match.
   * @param max the maximal number of times to match.
   * @return the new Pattern object.
   */
  public final Pattern some(final int min, final int max){
    return Patterns.some(min, max, this);
  }
  /**
   * If this pattern matches, return mismatch.
   * return match length 0 otherwise.
   * @return the new Pattern object.
   */
  public final Pattern not(){
    return Patterns.not(this);
  }
  /**
   * Matches with match length 0 if this Pattern object matches.
   * Mismatch otherwise.
   * @return the new Pattern object.
   */
  public final Pattern peek(){
    return Patterns.peek(this);
  }
  
  /**
   * If this pattern matches,
   * match the remaining input against Pattern object yes.
   * Otherwise, match the input against Pattern object no.
   * @param yes the true Pattern.
   * @param no the false Pattern.
   * @return the new Pattern object.
   */
  public final Pattern ifelse(final Pattern yes, final Pattern no){
    return Patterns.ifelse(this, yes, no);
  }
  /**
   * Matches the input against this pattern for n times.
   * @param n the number of times to match.
   * @return the new Pattern object.
   */
  public final Pattern repeat(final int n){
    return Patterns.repeat(n, this);
  }
}
