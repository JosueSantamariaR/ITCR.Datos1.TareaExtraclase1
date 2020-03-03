package plaudernTec;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Check if some field is empty
 * This is a sub class of WindowClient
 */
public class TextViewer implements DocumentListener {
    JTextField jTextField1;
    JTextField jTextField2;
    JTextField jTextField3;
    JButton jbutton;

    public TextViewer(JTextField jtf1, JTextField jtf2, JTextField jtf3, JButton jbutton){
        this.jTextField1 = jtf1;
        this.jTextField2 = jtf2;
        this.jTextField3 = jtf3;
        this.jbutton = jbutton;
    }

    public void changedUpdate(DocumentEvent e) {}

    public void removeUpdate(DocumentEvent e) {
        if(jTextField1.getText().trim().equals("") ||
                jTextField2.getText().trim().equals("") ||
                jTextField3.getText().trim().equals("")
        ){
            jbutton.setEnabled(false);
        }else{
            jbutton.setEnabled(true);
        }
    }
    public void insertUpdate(DocumentEvent e) {
        if(jTextField1.getText().trim().equals("") ||
                jTextField2.getText().trim().equals("") ||
                jTextField3.getText().trim().equals("")
        ){
            jbutton.setEnabled(false);
        }else{
            jbutton.setEnabled(true);
        }
    }

}