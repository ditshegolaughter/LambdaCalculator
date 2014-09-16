package jfun.parsec;

import jfun.util.Misc;

/**
 * This class represents an error frame during parsing.
 * <p>
 * @author Ben Yu
 * Dec 4, 2005 2:25:47 PM
 */
public class ParsingFrame {
  private final String module;
  private final int ind;
  private final Pos pos;
  private final Parser parser;
  /**
   * To create a ParsingFrame object.
   * @param module the module name.
   * @param ind the index of the character within the source. 
   * @param pos the position of the character.
   * @param parser the parser executed.
   */
  public ParsingFrame(String module, int ind, Pos pos,
      Parser parser) {
    this.ind = ind;
    this.module = module;
    this.parser = parser;
    this.pos = pos;
  }
  /**
   * Get the index of the character within the source.
   */
  public int getIndex() {
    return ind;
  }
  /**
   * Get the module name.
   */
  public String getModule() {
    return module;
  }
  /**
   * Get the Parser object executed.
   */
  public Parser getParser() {
    return parser;
  }
  /**
   * Get the position within the source.
   */
  public Pos getPosition() {
    return pos;
  }
  public String toString(){
    return module+" - " + pos+": "+parser.getName();
  }
  public boolean equals(Object obj) {
    if(obj instanceof ParsingFrame){
      final ParsingFrame other = (ParsingFrame)obj;
      return ind==other.ind &&
        Misc.equals(module, other.module);
    }
    else return false;
  }
  public int hashCode() {
    return Misc.hashcode(module)*31+ind;
  }
}
