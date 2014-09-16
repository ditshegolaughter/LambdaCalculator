package jfun.parsec;


/**
 * This class carries the position information of a token.
 * @deprecated Use {@link Tok} instead.
 * @author Ben Yu
 *
 * 2004-11-7
 */
public class PositionedToken implements java.io.Serializable{
  private final int ind;
  private final int len;
  private final Object tok;
  
  /**
   * Gets the length of the token.
   * @return the length of the token.
   */
  public int getLength(){
    return len;
  }
  /**
   * gets the index of the token in the orginal CharSequence.
   * @return the index.
   */
  public int getIndex(){return ind;}
  /**
   * gets the token.
   * @return the token.
   */
  public Object getToken(){return tok;}

  /**
   * Create a PositionedToken object.
   * @param i the starting index.
   * @param l the length of the token.
   * @param tok the token.
   */
  public PositionedToken(final int i, final int l, final Object tok) {
    this.ind = i;
    this.len = l;
    this.tok = tok;
  }
  public String toString(){
    return tok.toString();
  }
}
