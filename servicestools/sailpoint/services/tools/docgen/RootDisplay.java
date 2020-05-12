package sailpoint.services.tools.docgen;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
 * Created by adam.creaney on 3/23/16.
 * Main driver of the DocumentGenerator project, will create the UI components
 *
 * TODO:
 * Add progress meter JProgressBar
 *
 */
public class RootDisplay extends JPanel implements ActionListener {

    private JLabel chooseSourceLabel;
    private JLabel chooseDestinationLabel;
    private JTextArea argSrcText;
    private JTextArea argDestText;
    private JButton executeButton;

    private JTextArea log;
    private JButton chooseSourceButton;
    private JButton chooseDestinationButton;
    private JFileChooser fcs;
    private JFileChooser fcd;

    private File src;
    private File dest;

    static private final String newLine = "\n";

    public RootDisplay() {
        super(new BorderLayout());

        log = new JTextArea(5, 20);
        log.setMargin(new Insets(5,5,5,5));
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);

        fcs = new JFileChooser();
        fcs.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        fcd = new JFileChooser();
        fcd.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        chooseSourceLabel = new JLabel("Choose config directory");
        chooseSourceButton = new JButton("...");
        chooseSourceButton.addActionListener(this);

        chooseDestinationLabel = new JLabel("Choose output directory");
        chooseDestinationButton = new JButton("...");
        chooseDestinationButton.addActionListener(this);

        argSrcText = new JTextArea(1, 30);
        argSrcText.setEditable(false);
        argSrcText.setLineWrap(true);

        argDestText = new JTextArea(1, 30);
        argDestText.setEditable(false);
        argDestText.setLineWrap(true);

        executeButton = new JButton("Execute");
        executeButton.addActionListener(this);

        JPanel btnPanel = new JPanel(new GridBagLayout());

        //btnPanel layout
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        btnPanel.add(chooseSourceLabel, c);
        c.gridx = 1;
        btnPanel.add(chooseSourceButton, c);
        c.gridx = 2;
        btnPanel.add(argSrcText, c);
        c.gridx = 0;
        c.gridy = 1;
        btnPanel.add(chooseDestinationLabel, c);
        c.gridx = 1;
        btnPanel.add(chooseDestinationButton, c);
        c.gridx = 2;
        btnPanel.add(argDestText, c);

        JPanel executePanel = new JPanel();
        executePanel.add(executeButton);

        setBorder(new EmptyBorder(10, 10, 10, 10));
        add(btnPanel, BorderLayout.PAGE_START);
        add(executePanel, BorderLayout.LINE_END);
        add(logScrollPane, BorderLayout.PAGE_END);

    }

    public void actionPerformed(ActionEvent ae){
        if(ae.getSource() == chooseSourceButton){
            int returnVal = fcs.showOpenDialog(RootDisplay.this);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                src = fcs.getSelectedFile();
                argSrcText.setText(src.getAbsolutePath());
                log.append("Source: " + src.getName() + newLine);
            }
            else{
                log.append("Open file source cancelled by user!" + newLine);
            }
            log.setCaretPosition(log.getDocument().getLength());
        }
        else if(ae.getSource() == chooseDestinationButton){
            int returnVal = fcd.showOpenDialog(RootDisplay.this);
            if(returnVal == JFileChooser.APPROVE_OPTION){
                dest = fcd.getSelectedFile();
                argDestText.setText(dest.getAbsolutePath());
                log.append("Destination: " + dest.getName() + newLine);
            }
            else{
                log.append("Open file destination cancelled by user!" + newLine);
            }
            log.setCaretPosition(log.getDocument().getLength());
        }
        else if(ae.getSource() == executeButton){
            executeButton.setEnabled(false);
            if(dest != null && src != null) {
                log.append("Executing..." + newLine);
                Documentizer d = new Documentizer(src, dest);
                d.execute();
                log.append("Complete.");
            }
            else{
                log.append("Please select a source AND a destination." + newLine);
            }
            executeButton.setEnabled(true);
        }

    }

    public static void main(String[] args) {

        JFrame frame = new JFrame("Services Documentation Generator");
        //frame.setContentPane(new RootDisplay().rd_main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new RootDisplay());
        frame.pack();
        frame.setVisible(true);
    }
}
