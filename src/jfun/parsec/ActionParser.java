package jfun.parsec;

final class ActionParser extends Parser<Object> {
  private final Runnable action;
  boolean apply(ParseContext ctxt) {
    action.run();
    return true;
  }
  ActionParser(String n, Runnable action) {
    super(n);
    this.action = action;
  }
}
