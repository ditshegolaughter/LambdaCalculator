package gui;

import jfun.parsec.ParserException;
import lambda.parser.LambdaTermParser;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.util.ArrayList;

/**
 * Highlighting erros.
 */
public class ErrorHighlighter extends Thread implements DocumentListener {
  private JTextComponent textComponent;

  private static Color color = new Color(255, 83, 83);
  private Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(color);

  private Highlighter highlighter;
  private ArrayList<Object> highlights = new ArrayList<Object>();

  public ErrorHighlighter(JTextComponent textArea) {
    this.textComponent = textArea;
  }

  public void clearHighlights() {
    if (highlighter != null) {
      final ArrayList<Object> highlightsRemove = highlights;
      highlights = new ArrayList<Object>();
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          for(Object highlight : highlightsRemove) highlighter.removeHighlight(highlight);
        }
      });
    }
  }

  public void run() {
    setPriority(Thread.MIN_PRIORITY);
    textComponent.getDocument().addDocumentListener(this);

    highlighter = textComponent.getHighlighter();
    while(true) {
      String text = textComponent.getText();
      try {
        LambdaTermParser.parse(text);
        clearHighlights();
      } catch(ParserException e) {
        int index = getPos(text, e.getLineNo()-1, e.getColumnNo());
        try {
          clearHighlights();
          highlights.add(highlighter.addHighlight(index, index + 1, painter));
        } catch (BadLocationException e1) { }
      } catch(Exception e) { }

      synchronized(this) {
        try {
          wait();
        } catch (InterruptedException e) { }
      }
    }
  }

  private int getPos(String string, int line, int column) {
    int index = 0;
    int length = string.length();
    if(line > 0) {
      while(index < length) {
        if(string.charAt(index) == '\n') if(--line == 0) break;
        index++;
      }
    }
    return index + column;
  }

  public void insertUpdate(DocumentEvent e) {
    synchronized(this) { notifyAll(); }
  }

  public void removeUpdate(DocumentEvent e) {
    synchronized(this) { notifyAll(); }
  }

  public void changedUpdate(DocumentEvent e) {
    synchronized(this) { notifyAll(); }
  }
}
