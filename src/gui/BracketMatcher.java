package gui;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.*;
import java.awt.*;
import java.util.ArrayList;

public class BracketMatcher implements CaretListener {
  private static Color colorGood = new Color(153, 204, 255);
  private static Color colorBad = new Color(255, 153, 153);

  private ArrayList<Object> highlights = new ArrayList<Object>();

  private Highlighter highlighter;
  private Highlighter.HighlightPainter goodPainter;
  private Highlighter.HighlightPainter badPainter;

  /**
   * Highlights using a good painter for matched parens, and a bad painter for unmatched parens.
   */
  public BracketMatcher(Highlighter.HighlightPainter goodHighlightPainter, Highlighter.HighlightPainter badHighlightPainter) {
    this.goodPainter = goodHighlightPainter;
    this.badPainter = badHighlightPainter;
  }

  /**
   * A BracketMatcher with the default highlighters.
   */
  public BracketMatcher() {
    this(new DefaultHighlighter.DefaultHighlightPainter(colorGood), new DefaultHighlighter.DefaultHighlightPainter(colorBad));
  }

  public synchronized void clearHighlights() {
    if (highlighter != null) {
      for(Object highlight : highlights) highlighter.removeHighlight(highlight);
      highlights.clear();
      highlighter = null;
    }
  }

  /**
   * Returns the character at position p in the document
   */
  public static char getCharAt(Document doc, int p) throws BadLocationException {
    return doc.getText(p, 1).charAt(0);
  }

  /**
   * Returns the position of the matching parenthesis.
   *
   * @return the position of the matching paren, or -1 if none is found
   */
  public static int findMatchingParen(Document doc, int pos, int direction) throws BadLocationException {
    int parenCount = -direction;
    int i = pos + direction;

    for (; direction < 0 ? i >= 0 : i < doc.getLength(); i += direction) {
      char c = getCharAt(doc, i);

      switch (c) {
        case')': case'}': case']': parenCount++; break;
        case'(': case'{': case'[': parenCount--; break;
      }
      if (parenCount == 0) return i;
    }

    return -1;
  }

  private synchronized void highlightMatchingParen(Document doc, int pos, char c, int direction) throws BadLocationException {
    int otherParen = findMatchingParen(doc, pos, direction);
    if (otherParen >= 0) {
      char c2 = getCharAt(doc, otherParen);

      Highlighter.HighlightPainter painter = replace(c) == replace(c2) ? goodPainter : badPainter;
      highlights.add(highlighter.addHighlight(otherParen, otherParen + 1, painter));
      highlights.add(highlighter.addHighlight(pos, pos + 1, painter));
    } else {
      highlights.add(highlighter.addHighlight(pos, pos + 1, badPainter));
    }
  }

  private char replace(char c) {
    switch (c) {
      case')': return '(';
      case'}': return '{';
      case']': return '[';
    }
    return c;
  }

  /**
   * Called whenever the caret moves, it updates the highlights
   */
  public void caretUpdate(CaretEvent e) {
    clearHighlights();

    JTextComponent source = (JTextComponent) e.getSource();
    highlighter = source.getHighlighter();
    Document doc = source.getDocument();
    if (e.getDot() == 0) return;

    try {
      int leftPos = e.getDot() - 1;
      char left = getCharAt(doc, leftPos);
      if (left == ')' || left == ']' || left == '}') highlightMatchingParen(doc, leftPos, left, -1);
    } catch (BadLocationException badLocatoin) {  }

    try {
      int rightPos = e.getDot();
      char right = getCharAt(doc, rightPos);
      if (right == '(' || right == '[' || right == '{') highlightMatchingParen(doc, rightPos, right, 1);
    } catch (BadLocationException badLocatoin) {  }
  }
}

