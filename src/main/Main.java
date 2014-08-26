package main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.*;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.ButtonGroup;
import javax.swing.JMenuBar;
import javax.swing.KeyStroke;
import javax.swing.ImageIcon;
 
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JFrame;
import lambda.LambdaTerm;
import lambda.Variable;
import lambda.actions.HeadRedex;
import lambda.actions.Rewrite;
import lambda.gui.VisitorCollapseDefined;
import lambda.parser.Definition;
import lambda.parser.Definitions;
import lambda.parser.LambdaTermParser;
import lambda.utils.Position;
 

public class Main implements ActionListener  {
    JTextArea output;
    JScrollPane scrollPane;
    
    private JButton goButton = new JButton("Start");
    private JButton headStep = new JButton("Next Step");
    private JTextField input = new JTextField("Init = (\\x.\\y.y (x x y))(\\x.\\y.y (x x y)) a;");
    private LambdaTerm term = new Variable("Lambda Calculator");
    private Definitions definitions = new Definitions(new Definition[0]);
 
    public JMenuBar createMenuBar() {
        JMenuBar menuBar;
        JMenu menu, submenu;
        JMenuItem menuItem;
        JRadioButtonMenuItem rbMenuItem;
        JCheckBoxMenuItem cbMenuItem;
 
        //Create the menu bar.
        menuBar = new JMenuBar();
 
        //Build the first menu.
        menu = new JMenu("App");
        menu.setMnemonic(KeyEvent.VK_A);
        menuBar.add(menu);
 
        //a group of JMenuItems
        menuItem = new JMenuItem("Restart");
        menu.add(menuItem); 
        menuItem = new JMenuItem("Quit");
        menu.add(menuItem);
        //Build second menu in the menu bar.
        menu = new JMenu("Help");
        menu.setMnemonic(KeyEvent.VK_N);
        menu.getAccessibleContext().setAccessibleDescription("Help Content");
        menuBar.add(menu);
 
        return menuBar;
    }
 
    public Container createContentPane() {
        //Create the content-pane-to-be.
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setOpaque(true);
        
        //create topPanel for input and controls
        JPanel topPanel = new JPanel(new BorderLayout());
        
        JPanel buttonPanel = new JPanel(new FlowLayout());

        input.setPreferredSize(new Dimension(250, input.getPreferredSize().height));
        buttonPanel.add(input);
        buttonPanel.add(goButton);
        headStep.setEnabled(false);
        //buttonPanel.add(spacer());
        buttonPanel.add(headStep);
        
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
 
        //Create input textbox
        JTextField input = new JTextField();
        input.setSize(2, 10);
        contentPane.add(topPanel,BorderLayout.NORTH);
        
        goButton.addActionListener(this);
        headStep.addActionListener(this);
        //Create a scrolled text area.
        output = new JTextArea(5, 30);
        output.setEditable(false);
        scrollPane = new JScrollPane(output);
 
        
        //Add the text area to the content pane.
        contentPane.add(scrollPane, BorderLayout.CENTER);
 
        return contentPane;
    }
    public void actionPerformed(ActionEvent actionEvent) {
        if(actionEvent.getSource() == goButton){
            parse();
            headStep.setEnabled(true);
        }
        if(actionEvent.getSource() == headStep){
            int count = 1;
            for(int i = 0; i < count; i++) {
                Position position = term.visit(new HeadRedex(), null);
                if(position != null) {
                    setTerm(term.visit(new Rewrite(), position.copy()), definitions, false);
                    output.append(term.toString(definitions));
                    output.append("\n");
                }
            }
        }
    }
    public void setTerm(LambdaTerm term, Definitions definitions, boolean collapse) {
        this.term = term;
        this.definitions = definitions;
     
      }
    public void parse(){
        try{
            setTerm(new Variable("Lambda Calculator"), new Definitions(new Definition[] {}), true);
            term = new Variable("Lambda Calculator");
            Definitions definitions = LambdaTermParser.parse(input.getText());

            output.setText(definitions.get("Init").toString(definitions));
            output.append("\n");

            setTerm(definitions.get("Init"), definitions, true);
            
        }
        catch(Exception e){
            output.setText(e.toString());
        }
    }
 
     /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("LAMBDA Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Create and set up the content pane.
        Main demo = new Main();
        frame.setJMenuBar(demo.createMenuBar());
        frame.setContentPane(demo.createContentPane());
 
        //Display the window.
        frame.setSize(450, 260);
        frame.setVisible(true);
    }
 
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
