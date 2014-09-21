package main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.*;
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
    public static String BetaReduction = "";  //beta reduction redex
    public static String AlphaReduction = ""; //alpha reduction redex
    JTextArea output;                         //displays output to user
    JScrollPane scrollPane;
    
    private JButton startButton = new JButton("Start"); 
    private JButton headStep = new JButton("Next Step"); //next reduction step
    private JButton markerButton = new JButton("Marker"); //switches between marker and calculator functionality
    private JButton check = new JButton("Check");
    private JTextField input = new JTextField("(\\x.\\y.y (x x y))(\\x.\\y.y (x x y)) a");
    private LambdaTerm term = new Variable("Lambda Calculator");
    //private LambdaTerm term2 = new Variable("Marker parsing");
    private Definitions definitions = new Definitions(new Definition[0]);
    
    private String[] solution;  //holds the user's list of steps of the solution
 
    /**
     * creates the menu bar for the app.
     * @return menu bar
     */
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
        menuBar.add(menu);
 
        //a group of JMenuItems
        menuItem = new JMenuItem("Marker");
        menu.add(menuItem); 
        menuItem = new JMenuItem("Quit");
        menuItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent arg0) {
                System.exit(0);
             }
        });
        menu.add(menuItem);
        
        //Build second menu in the menu bar.
        menu = new JMenu("Help");
        menuBar.add(menu);
        
        //a group of JMenuItems
        menuItem = new JMenuItem("Help Content");
        menu.add(menuItem); 
        menuItem = new JMenuItem("About");
        menu.add(menuItem);
 
        return menuBar;
        
    }
    /**
     * creates the main container for the gui.
     * @return content pane
     */
    public Container createContentPane() {
        //Create the content-pane-to-be.
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setOpaque(true);
        
        //create topPanel for input and controls
        JPanel topPanel = new JPanel(new BorderLayout());
        
        JPanel buttonPanel = new JPanel(new FlowLayout());

        input.setPreferredSize(new Dimension(250, input.getPreferredSize().height));
        buttonPanel.add(input);
        
        buttonPanel.add(startButton);
        
        headStep.setVisible(false);
        buttonPanel.add(headStep);
        
        check.setVisible(false);
        buttonPanel.add(check);
        
        buttonPanel.add(markerButton);
        
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
 
        contentPane.add(topPanel,BorderLayout.NORTH);
        
        //add actionlisteners for controlls
        
        startButton.addActionListener(this);
        headStep.addActionListener(this);
        check.addActionListener(this);
        markerButton.addActionListener(this);
        input.addActionListener(this);
        
        //Create a scrolled text area for output.
        output = new JTextArea(5, 30);
        output.setEditable(false);
        scrollPane = new JScrollPane(output);
        Font font = new Font( "Serif", Font.BOLD + Font.ITALIC, 18 );
        output.setFont(font);
 
        
        //Add the text area to the content pane.
        contentPane.add(scrollPane, BorderLayout.CENTER);
 
        return contentPane;
    }
    
    public void actionPerformed(ActionEvent actionEvent) {
        //calulator started
        if(actionEvent.getSource() == startButton ||actionEvent.getSource() == input){ 
            output.setText(parse()+"\n");
            headStep.setVisible(true);
        }
        //next reduction step
        if(actionEvent.getSource() == headStep){ 
            
            Position position = term.visit(new HeadRedex(), null);
            if(position != null) {
                setTerm(term.visit(new Rewrite(), position.copy()), definitions, false);
                output.append(" => ");
                output.append(term.toString(definitions));
                output.append(BetaReduction);
                output.append("\n");
                if(!AlphaReduction.equals("")){
                    output.append(" => ");
                    output.append(AlphaReduction);
                    output.append("\n");
                    AlphaReduction = "";
                }
            }
            
        }
        //toggle between marker and calculator functionality
        if(actionEvent.getSource() == markerButton){
            
            if(markerButton.getText().equals("Marker")){
                output.setEditable(true);
                markerButton.setText("Calculator");
                input.setVisible(false);
                startButton.setVisible(false);
                headStep.setVisible(false);
                check.setVisible(true);
            }
            else{
                output.setEditable(false);
                markerButton.setText("Marker");
                input.setVisible(true);
                startButton.setVisible(true);
                check.setVisible(false);
            }
            output.setText("");
        }
        
        //evaluate user's solution
        if(actionEvent.getSource() == check){
            if(check.getText().equals("Enter Solution"))
            {
                output.setText("");
                output.setEditable(true);
                check.setText("Check");
                
            }
            else{
                if(output.getText().equals("")){
                JOptionPane.showMessageDialog(null,"Enter your solution first!");
                }
                else{
                    check.setText("Enter Solution");
                    output.setEditable(false);
                    solution = output.getText().replaceAll("(?m)^[ \t]*\r?\n", "").split("\n");
                    output.append("\n============ Marking Solution ============\n");
                    mark();
                }
            }
            
            
        }
    }
    
    public void setTerm(LambdaTerm term, Definitions definitions, boolean collapse) {
        this.term = term;
        this.definitions = definitions;
     
      }
    
    /**
     * checks the string entered in the input text box if it is a valid lambda expression.
     * 
     * @return the parsed string with parenthesis added or error indication
     */
    public String parse(){
        try{
            setTerm(new Variable("Lambda Calculator"), new Definitions(new Definition[] {}), true);
            term = new Variable("Lambda Calculator");
            Definitions definitions = LambdaTermParser.parse("Init = "+input.getText().replace("\u03BB", "\\") +";");
            setTerm(definitions.get("Init"), definitions, true);
            return definitions.get("Init").toString(definitions);
                 
        }
        catch(Exception e){
            //output.setText(e.toString());
            return input.getText()+" : invalid expression";
        }
    }
    /**
     * checks the string if it is a valid lambda expression.
     * 
     * @return the parsed string with parenthesis added or error indication
     */
    public String parse(String expr){
        try{
            setTerm(new Variable("Lambda Calculator"), new Definitions(new Definition[] {}), true);
            term = new Variable("Lambda Calculator");
            Definitions definitions = LambdaTermParser.parse("Init = "+expr.replace("\u03BB", "\\") +";");
            setTerm(definitions.get("Init"), definitions, true);
            return definitions.get("Init").toString(definitions);
                 
        }
        catch(Exception e){
            //output.setText(e.toString());
            return input.getText()+" : invalid expression";
        }
    }
    /**
     * marks the user's solution
     */
    public void mark(){
        int i = 0;
        
        try{
            String result;
            for( i = 0; i<solution.length; i++){
                
                //first item is the problem
                if(i==0){
                    result = parse(solution[i]);
                    output.append(result+"\n");
                    if(result.contains("invalid")){
                        break;
                    }
                    
                }
                else{
                    Definitions definitions2 = LambdaTermParser.parse("Init = "+solution[i].replace("\u03BB", "\\")+";");
                    result = definitions2.get("Init").toString(definitions2);
                    Position position = term.visit(new HeadRedex(), null);
                    if(position != null){
                        setTerm(term.visit(new Rewrite(), position.copy()), definitions, false);
                        if(result.equals(term.toString(definitions))){
                            output.append(result+" : correct");
                        }
                        else{
                            output.append(result+" : incorrect");
                            break;
                        }
                        
                        output.append("\n");
                    }
                }
                
                
            }
            
        }
        catch(Exception e){
            output.append(solution[i] + " invalid expression");
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
        //frame.setJMenuBar(demo.createMenuBar());
        frame.setContentPane(demo.createContentPane());
 
        //Display the window.
        frame.setSize(450, 260);
        frame.setVisible(true);
    }
 
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception unused) {
            ; // Ignore exception because we can't do anything.  Will use default.
        }
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
