/**
 * 
 */
package jfun.parsec;

import jfun.parsec.tokens.TypedToken;

abstract class IsTokenOfType<R> implements FromToken<R> {
  private final FromString<R> f;

  IsTokenOfType(FromString<R> f) {
    super();
    this.f = f;
  }

  public R fromToken(final Tok ptok){
    final Object t = ptok.getToken();
    if(t instanceof TypedToken){
      final TypedToken c = (TypedToken)t;
      if(!isOfType(c.getType()))
        return null;
      return f.fromString(ptok.getIndex(), ptok.getLength(), c.getName());
    }
    else return null;
  }
  public abstract boolean isOfType(Object type);
}