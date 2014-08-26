package jfun.parsec.tokens;
/**
 * Represents a token associated with a token type.
 * <p>
 * @author Ben Yu
 * Apr 27, 2006 8:01:19 PM
 * @since version 1.1
 */
public class TypedToken<Type> extends NamedToken {
  private final Type type;

  public TypedToken(String name, Type type) {
    super(name);
    this.type = type;
  }
  /**
   * Get the text of the token. Equivalent to {@link #getName()}.
   * @since version 0.6
   */
  public final String getText(){
    return getName();
  }
  /**
   * To get the data type of the token.
   */
  public Type getType() {
    return type;
  }
  public boolean equals(TypedToken other){
    return type.equals(other.type) && super.equals(other);
  }
  public boolean equals(Object obj) {
    if(obj instanceof TypedToken){
      return equals((TypedToken)obj);
    }
    else return false;
  }

  public int hashCode() {
    return type.hashCode() * 31 + super.hashCode();
  }
}
