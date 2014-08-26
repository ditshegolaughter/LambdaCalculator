package jfun.parsec;

import java.util.ArrayList;
import java.util.List;

class ParserInternals {

  static boolean _most(final IntOrder ord, final Parser<?>[] ps,
      final int ind, final ParseContext state, final Object ustate,
      final Object ret, final int step, final int at,
      final AbstractParsecError err) {
    if (ind >= ps.length)
      return true;
    int most = state.getAt();
    int mstep = state.getStep();
    Object mret = state.getReturn();
    Object mustate = state.getUserState();
    AbstractParsecError merr = state.getError();
    for (int i = ind; i < ps.length; i++) {
      state.set(step, at, ret, ustate, err);
      final boolean ok = ps[i].parse(state);
      if (state.hasException())
        return false;
      if (ok) {
        final int at2 = state.getAt();
        if (ord.compare(at2, most)) {
          most = at2;
          mstep = state.getStep();
          mret = state.getReturn();
          mustate = state.getUserState();
          merr = state.getError();
        }
      }
    }
    state.set(mstep, most, mret, mustate, merr);
    return true;
  }

  static boolean run_repeat(final int n, final Parser<?> p,
      final ParseContext ctxt) {
    for (int i = 0; i < n; i++) {
      if (!p.parse(ctxt))
        return false;
    }
    return true;
  }

  static boolean run_many(final Parser<?> p, final ParseContext ctxt) {
    int at = ctxt.getAt();
    for (;;) {
      if (p.parse(ctxt)) {
        int at2 = ctxt.getAt();
        if (ParserInternals.isInfiniteLoop(at, at2)) {
          return true;
        }
        at = at2;
        continue;
      } else if (ctxt.hasException())
        return false;
      else if (ctxt.getAt() - at >= 1)
        return false;
      else
        return true;
    }
  }

  static boolean run_some(final int max, final Parser<?> p,
      final ParseContext ctxt) {
    for (int i = 0; i < max; i++) {
      final int at = ctxt.getAt();
      if (p.parse(ctxt))
        continue;
      else if (ctxt.hasException())
        return false;
      else if (ctxt.getAt() - at >= 1)
        return false;
      else
        return true;
    }
    return true;
  }

  static <A, To> boolean accm_repeat(
      final Accumulator<? super A, To> acc, final int n, final Parser<A> p,
      final ParseContext ctxt) {
    for (int i = 0; i < n; i++) {
      if (p.parse(ctxt)) {
        acc.accumulate(p.getReturn(ctxt));
        continue;
      } else if (ctxt.hasException()) {
        ctxt.setReturn(acc.getResult());
        return false;
      } else
        return false;
    }
    return true;
  }

  static <A, To> boolean accm_some(
      final Accumulator<? super A, To> acc, final int max, final Parser<A> p,
      final ParseContext ctxt) {
    for (int i = 0; i < max; i++) {
      final int at = ctxt.getAt();
      if (p.parse(ctxt)) {
        acc.accumulate(p.getReturn(ctxt));
        continue;
      } else if (ctxt.hasException()) {
        ctxt.setReturn(acc.getResult());
        return false;
      } else if (ctxt.getAt() - at >= 1) {
        return false;
      } else {
        break;
      }
    }
    return true;
  }

  static <A, To> boolean accm_many(
      final Accumulator<? super A, To> acc, final Parser<A> p,
      final ParseContext ctxt) {
    int at = ctxt.getAt();
    for (;;) {
      if (p.parse(ctxt)) {
        int at2 = ctxt.getAt();
        if (ParserInternals.isInfiniteLoop(at, at2)) {
          break;
        }
        at = at2;
        acc.accumulate(p.getReturn(ctxt));
        continue;
      } else if (ctxt.hasException()) {
        ctxt.setReturn(acc.getResult());
        return false;
      } else if (ctxt.getAt() - at >= 1)
        return false;
      else
        break;
    }
    return true;
  }

  static boolean recover(final int look_ahead, final Parser p,
      final ParseContext state, final int step, final int at, final Object ret,
      final Object ustate, final AbstractParsecError error) {
    if (state.getAt() != at && state.getStep() - step >= look_ahead)
      return false;
    if (state.hasException())
      return false;
    state.set(step, at, ret, ustate, error);
    if (p.parse(state))
      return true;
    state.setError(AbstractParsecError.mergeError(error, state.getError()));
    return false;
  }

  // change the infinite loop handling. don't fail. just stop looping.
  static boolean isInfiniteLoop(final int at0, final int at1) {
    return (at0 == at1);
    /*
     * if(at0 == at1){ throw new ParserException("parser accepts an empty
     * input", null, "parsec", null); }
     */
  }

  static ParsecError errExpecting(final String msg,
      final ParseContext state) {
    return ParsecError.raiseExpecting(state.getIndex(), msg);
  }

  static boolean raiseUnexpected(final String msg,
      final ParseContext state) {
    state.setError(ParsecError.raiseUnexpected(state.getIndex(), msg));
    return false;
  }

  static boolean raiseRaw(final String msg, final ParseContext state) {
    state.setError(ParsecError.raiseRaw(state.getIndex(), msg));
    return false;
  }

  static ParsecError newException(final Object e,
      final ParseContext state) {
    return ParsecError.throwException(state.getIndex(), e);
  }

  static boolean returnValue(final Object v, final ParseContext state) {
    state.setError(ParsecError.noError());
    state.setReturn(v);
    return true;
  }

  /** ****************** derived combinators ******************* */
  
  static boolean setErrorExpecting(final String s, final ParseContext state) {
    final AbstractParsecError err = state.getError();
    if (err == null) {
      state.setError(errExpecting(s, state));
    } else
      state.setError(err.setExpecting(s));
    return false;
  }

  // performance of this method is insignificant.
  static String toErrorStr(final ParseContext state) {
    final AbstractParsecError aerr = state.getError();
    if (aerr == null) {
      return "";
    }
    final ParsecError err = aerr.render();
    if (err.getException() != null) {
      return ("User exception: " + err.getException().toString());
    } else
      return "";
  }

  static <E, T extends E> E[] getArrayResult(final ArrayList<T> l,
      final ArrayFactory<E> af) {
    final E[] arr = af.createArray(l.size());
    try {
      l.toArray(arr);
    } catch (ArrayStoreException e) {
      final Class etype = arr.getClass().getComponentType();
      for (int i = 0; i < l.size(); i++) {
        final Object elem = l.get(i);
        if (elem != null && !etype.isInstance(elem)) {
          final String msg = "cannot cast the #" + i + " element: <" + elem
              + "> of type " + elem.getClass().getName() + " to "
              + etype.getName();
          throw new ClassCastException(msg);
        }
      }
      throw e;
    }
    return arr;
  }

  static Tok[] toTokens(Object obj) {
    if (obj instanceof Tok[]) {
      return (Tok[]) obj;
    } else if (obj instanceof List) {
      final List<?> l = (List) obj;
      final Tok[] arr = new Tok[l.size()];
      l.toArray(arr);
      return arr;
    } else
      throw new IllegalParserStateException(
          "an array of Tok objects expected for token level parsing.");
  }

  static boolean cont(final ParseContext ctxt, final ParseContext s0,
      final Parser<?> p) {
    ctxt.setError(null);
    if (!p.parse(s0)) {
      ctxt.set(ctxt.getStep(), s0.getIndex(), null, s0.getUserState(), s0
          .getError());
      return false;
    } else {
      ctxt.set(ctxt.getStep() + s0.getStep(), ctxt.getAt(), s0.getReturn(), s0
          .getUserState(), s0.getError());
      return true;
    }
  }

  static <R> R runParser(final ParseContext ctxt, final Parser<R> p,
      final PositionMap pmap) {
    if (!ParserInternals.parseIt(ctxt, p, pmap)) {
      final AbstractParsecError err = ctxt.getError();
      final int ind = err != null ? err.getIndex() : -1;
      throw new ParserException(toErrorStr(ctxt), (err == null ? null : err
          .render()), ctxt.getModuleName(), pmap.getPos(ind >= 0 ? ind : ctxt
          .getIndex()));
    }
    return p.getReturn(ctxt);
  }

  static boolean parseIt(final ParseContext ctxt, final Parser<?> p,
      final PositionMap pmap) {
    try {
      return p.parse(ctxt);
    } catch (UserException e) {
      final int ind = e.getInd();
      throw new ParserException(e.getMessage(), null, ctxt.getModuleName(),
          pmap.getPos(ind >= 0 ? ind : ctxt.getIndex()));
    }
  }

}
