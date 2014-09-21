package lambda.utils;

import java.util.ArrayList;

/**
 * Position in a term.
 */
public class Position {
  private ArrayList<Integer> positions = new ArrayList<Integer>();

  public Position copy() {
    Position position = new Position();
    position.positions = (ArrayList<Integer>) positions.clone();
    return position;
  }

  public void add(int argument) {
    positions.add(argument);
  }

  public void append(Position position) {
    positions.addAll(0, position.positions);
  }
  
  public int get() {
    return positions.get(positions.size() - 1);
  }

  public int remove() {
    return positions.remove(positions.size() - 1);
  }

  public int length() {
    return positions.size();
  }

  public String toString() {
    String string = "<";
    for(int p : positions) string += p + ", ";
    string += ">";
    return string;
  }

  public boolean equals(Object obj) {
    if(obj == null || !(obj instanceof Position)) return false;
    return positions.equals(((Position) obj).positions);
  }
}
