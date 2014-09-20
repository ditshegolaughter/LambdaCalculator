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
    public static String Reduction = "";
    public static String Alpha = "";
    JTextArea output;
    JScrollPane scrollPane;
    
    private JButton startButton = new JButton("Start");
    private JButton headStep = new JButton("Next Step");
    private JButton markerButton = new JButton("Marker");
    private JButton check = new JButton("Check");
    private JTextField input = new JTextField("(\\x.\\y.y (x x y))(\\x.\\y.y (x x y)) a");
    private LambdaTerm term = new Variable("Lambda Calculator");
    private LambdaTerm term2 = new Variable("Marker parsing");
    private Definitions definitions = new Definitions(new Definition[0]);
    
    private String[] solution;
 
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
        buttonPanel.add(startButton);
        headStep.setVisible(false);
        //buttonPanel.add(spacer());
        buttonPanel.add(headStep);
        check.setVisible(false);
        buttonPanel.add(check);
        buttonPanel.add(markerButton);
        
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
 
        //Create input textbox
        //JTextField input = new JTextField();
        //input.setSize(2, 10);
        contentPane.add(topPanel,BorderLayout.NORTH);
        
        startButton.addActionListener(this);
        headStep.addActionListener(this);
        check.addActionListener(this);
        markerButton.addActionListener(this);
        input.addActionListener(this);
        //Create a scrolled text area.
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
        if(actionEvent.getSource() == startButton ||actionEvent.getSource() == input){
            output.setText(parse()+"\n");
            headStep.setVisible(true);
        }
        if(actionEvent.getSource() == headStep){
            int count = 1;
            for(int i = 0; i < count; i++) {
                Position position = term.visit(new HeadRedex(), null);
                if(position != null) {
                    setTerm(term.visit(new Rewrite(), position.copy()), definitions, false);
                    output.append(" ==> ");
                    output.append(term.toString(definitions));
                    output.append(Reduction);
                    output.append("\n");
                    if(!Alpha.equals("")){
                        output.append(" ==> ");
                        output.append(Alpha);
                        output.append("\n");
                        Alpha = "";
                    }
                }
            }
        }
        if(actionEvent.getSource() == markerButton){
            
            if(markerButton.getText() == "Marker"){
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
        if(actionEvent.getSource() == check){
            //String[] solution = output.getText();
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
                    solution = output.getText().split("\n");
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
    public void mark(){
        int i = 0;
        
        try{
            //setTerm(new Variable("Lambda Calculator"), new Definitions(new Definition[] {}), true);
            //term = new Variable("Lambda Calculator");
            String result;
            for( i = 0; i<solution.length; i++){
                if(i==0){
                    result = parse(solution[i]);
                    output.append(result+"\n");
                    if(result.contains("invalid")){
                        break;
                    }
                    
                }
                else{
                    System.out.println(solution[i]);
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
                        }
                        
                        output.append("\n");
                    }
                }
                
                
            }
            
            
            
            

            //setTerm(definitions.get("Init"), definitions, true);
            
        }
        catch(Exception e){
            //output.setText(e.toString());
            output.append(solution[i] + " invalid expression");
            System.out.println(e.toString());
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
