package main;

import gui.*;
import lambda.Variable;
import lambda.parser.Definition;
import lambda.parser.Definitions;
import lambda.parser.LambdaTermParser;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Lambda calculator.
 */
public class Calculator extends JApplet implements ActionListener, ChangeListener, DocumentListener {
  private JTextArea input = new JTextArea(
                  "Init = Y a;\n" +
                  "\n" +
                  "Y = (\\x.\\y.y (x x y))(\\x.\\y.y (x x y));"
/*
                  "Init = Y (\\Morse.P A (zip (inv Morse) (tail Morse)));\n" +
                  "\n" +
                  "zip = Y (\\z.\\l.\\r.P (l A) (P (r A) (z (l B) (r B))));\n" +
                  "inv = Y (\\i.\\s. P (s A B A) (i (s B)));\n" +
                  "tail = \\s.s B;\n" +
                  "\n" +
                  "P = \\x.\\y.\\c.c x y; A = \\x.\\y.x; B = \\x.\\y.y;\n" +
                  "\n" +
                  "Y = (\\x.\\y.y (x x y))(\\x.\\y.y (x x y));"
*/
  );
  private JButton goButton = new JButton("Start");
  private JButton headStep = new JButton("Head steps");
  private JTextField headNr = new JTextField("1");

  private JTextArea log = new JTextArea();

  private JSplitPane topSplitPane, bottomSplitPane;

  private LambdaPanel lambdaPanel = new LambdaPanel(log);
  private JEditorPane help = new JEditorPane("text/html",
                  "<html><ul>" +
                          "<li><b>redex positions</b> are marked <b><font color=red>red</font></b></li>" +
                          "<li><b>left click</b> on a redex position for <b>reducing</b></li>" +
                          "<li><b>right click</b> for hiding subterms</li>" +
                          "<li><b>mouse wheel</b> for <b>zooming</b></li>" +
                          "<li><b>drag&drop</b> for <b>moving</b> (left/right/up/down)</li>" +
                  "</ul></html>"
  );

  public void init() {
    Container cp = getContentPane();
    cp.setLayout(new BorderLayout());

    JPanel topPanel = new JPanel(new BorderLayout());
    topPanel.add(new JScrollPane(input), BorderLayout.CENTER);

    JPanel buttonPanel = new JPanel(new FlowLayout());

    buttonPanel.add(goButton);
    buttonPanel.add(spacer());
    buttonPanel.add(headStep);
    headNr.setPreferredSize(new Dimension(70, headNr.getPreferredSize().height));
    buttonPanel.add(headNr);
    topPanel.add(buttonPanel, BorderLayout.SOUTH);

    JSplitPane middleSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, lambdaPanel, help);
    middleSplitPane.setOneTouchExpandable(true);
    middleSplitPane.setDividerLocation(0.8);
    middleSplitPane.setResizeWeight(0.8);
    bottomSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, middleSplitPane, new JScrollPane(log));
    bottomSplitPane.setOneTouchExpandable(true);
    topSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, topPanel, bottomSplitPane);
    topSplitPane.setOneTouchExpandable(true);
    cp.add(topSplitPane, BorderLayout.CENTER);

    goButton.addActionListener(this);
    headStep.addActionListener(this);
    input.setFont(new Font("MONOSPACED", 0, 12));
    input.addCaretListener(new BracketMatcher());
    input.getDocument().addDocumentListener(this);
  }

  private JPanel spacer() {
    JPanel spacer = new JPanel();
    spacer.setPreferredSize(new Dimension(30,30));

    return spacer;
  }

  public void start() {
    bottomSplitPane.setDividerLocation(0.7);
    bottomSplitPane.setResizeWeight(0.7);
    topSplitPane.setDividerLocation(0.2);
    topSplitPane.setResizeWeight(0.2);

    ErrorHighlighter errorHighlighter = new ErrorHighlighter(input);
    errorHighlighter.start();

    parse();
  }

  public void actionPerformed(ActionEvent actionEvent) {
    if(actionEvent.getSource() == goButton) parse();
    if(actionEvent.getSource() == headStep) {
      int count = Integer.parseInt(headNr.getText());
      for(int i = 0; i < count; i++) lambdaPanel.headStep();
    }
  }

  public void stateChanged(ChangeEvent e) {
  }

  public void parse() {
    try {
      lambdaPanel.setTerm(new Variable("Lambda Calculator"), new Definitions(new Definition[] {}), true);

      Definitions definitions = LambdaTermParser.parse(input.getText());

      log.setText(definitions.get("Init").toString(definitions));
      log.append("\n");

      lambdaPanel.setTerm(definitions.get("Init"), definitions, true);
      lambdaPanel.x = Math.max(250,lambdaPanel.getWidth()/2);
      lambdaPanel.y = 25;
    } catch(Exception e) {
      log.setText(e.toString());
    }
  }

  public void insertUpdate(DocumentEvent e) {
    parse();
  }

  public void removeUpdate(DocumentEvent e) {
    parse();
  }

  public void changedUpdate(DocumentEvent e) {
    parse();
  }
}
