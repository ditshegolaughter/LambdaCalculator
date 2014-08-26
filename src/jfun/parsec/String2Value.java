package jfun.parsec;

import jfun.util.Misc;

/**
 * This implementation of FromString simply returns an object for any parameter.
 * <p>
 * @author Ben Yu
 * Mar 29, 2006 9:57:03 PM
 */
public class String2Value implements FromString {
  private final Object val;
  
  /**
   * Create a String2Value instance.
   * @param val the value to be returned by {@link #fromString(int, int, String)}
   */
  public String2Value(Object val) {
    this.val = val;
  }
  public Object fromString(int from, int len, String s) {
    return val;
  }
  public String toString(){
    return ""+val;
  }
  public boolean equals(Object obj) {
    if(obj instanceof String2Value){
      final String2Value other = (String2Value)obj;
      return Misc.equals(val, other.val);
    }
    else return false;
  }
  public int hashCode() {
    return Misc.hashcode(val);
  }
  
}
