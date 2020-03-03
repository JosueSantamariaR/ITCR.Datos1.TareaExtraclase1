package plaudernTec;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.html.*;
import java.util.ArrayList;
import java.util.Arrays;


public class WindowClient extends Thread{

    final JTextPane chatText = new JTextPane();
    final JTextPane usersList = new JTextPane();
    final JTextField fielNewMessage = new JTextField();
    private String oldMsg = "";
    private Thread read;
    private String serverName;
    private int PORT;
    private String name;
    BufferedReader input;
    PrintWriter output;
    Socket server;

    public WindowClient() {
        this.serverName = "LocalHost";
        this.PORT = 12345;
        this.name = "ID User";

        String fontfamily = "Arial, sans-serif";
        Font font = new Font(fontfamily, Font.PLAIN, 15);

        final JFrame jfr = new JFrame("PlaudernTec");
        jfr.getContentPane().setLayout(null);
        jfr.setSize(500, 500);
        jfr.setResizable(false);
        jfr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Module du fil de discussion
        chatText.setBounds(25, 25, 490, 320);
        chatText.setFont(font);
        chatText.setMargin(new Insets(6, 6, 6, 6));
        chatText.setEditable(false);
        JScrollPane jtextFilDiscuSP = new JScrollPane(chatText);
        jtextFilDiscuSP.setBounds(25, 25, 290, 320);

        chatText.setContentType("text/html");
        chatText.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);

        // Module de la liste des utilisateurs
        usersList.setBounds(520, 25, 156, 320);
        usersList.setEditable(true);
        usersList.setFont(font);
        usersList.setMargin(new Insets(6, 6, 6, 6));
        usersList.setEditable(false);
        JScrollPane jsplistuser = new JScrollPane(usersList);
        jsplistuser.setBounds(320, 25, 150, 320);

        usersList.setContentType("text/html");
        usersList.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);

        // Field message user input
        fielNewMessage.setBounds(0, 350, 100, 50);
        fielNewMessage.setFont(font);
        fielNewMessage.setMargin(new Insets(6, 6, 6, 6));
        final JScrollPane jtextInputChatSP = new JScrollPane(fielNewMessage);
        jtextInputChatSP.setBounds(25, 350, 290, 50);

        // button send
        final JButton jsbtn = new JButton("Enviar");
        jsbtn.setFont(font);
        jsbtn.setBounds(320, 350, 150, 50);

        // button Disconnect
        final JButton jsbtndeco = new JButton("Desconectarse");
        jsbtndeco.setFont(font);
        jsbtndeco.setBounds(320, 10, 150, 15);

        fielNewMessage.addKeyListener(new KeyAdapter() {
            // send message on Enter
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }

                // Get last message typed
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    String currentMessage = fielNewMessage.getText().trim();
                    fielNewMessage.setText(oldMsg);
                    oldMsg = currentMessage;
                }

                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    String currentMessage = fielNewMessage.getText().trim();
                    fielNewMessage.setText(oldMsg);
                    oldMsg = currentMessage;
                }
            }
        });

        // Click on send button
        jsbtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                sendMessage();
            }
        });

        // Connection view
        final JTextField jtfName = new JTextField(this.name);
        final JTextField jtfport = new JTextField(Integer.toString(this.PORT));
        final JTextField jtfAddr = new JTextField(this.serverName);
        final JButton jcbtn = new JButton("Conectarse");

        // check if those field are not empty
        jtfName.getDocument().addDocumentListener(new TextListener(jtfName, jtfport, jtfAddr, jcbtn));
        jtfport.getDocument().addDocumentListener(new TextListener(jtfName, jtfport, jtfAddr, jcbtn));
        jtfAddr.getDocument().addDocumentListener(new TextListener(jtfName, jtfport, jtfAddr, jcbtn));

        // position des Modules
        jcbtn.setFont(font);
        jtfAddr.setBounds(25, 380, 80, 40);
        jtfName.setBounds(110, 380, 80, 40);
        jtfport.setBounds(195, 380, 80, 40);
        jcbtn.setBounds(320, 380, 150, 40);

        // couleur par defaut des Modules fil de discussion et liste des utilisateurs
        chatText.setBackground(Color.LIGHT_GRAY);
        usersList.setBackground(Color.LIGHT_GRAY);

        // ajout des Ã©lÃ©ments
        jfr.add(jcbtn);
        jfr.add(jtextFilDiscuSP);
        jfr.add(jsplistuser);
        jfr.add(jtfName);
        jfr.add(jtfport);
        jfr.add(jtfAddr);
        jfr.setVisible(true);


        // info sur le Chat



        // On connect
        jcbtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    name = jtfName.getText();
                    String port = jtfport.getText();
                    serverName = jtfAddr.getText();
                    PORT = Integer.parseInt(port);
                    server = new Socket(serverName, PORT);
                    input = new BufferedReader(new InputStreamReader(server.getInputStream()));
                    output = new PrintWriter(server.getOutputStream(), true);

                    // send nickname to server
                    output.println(name);

                    // create new Read Thread
                    read = new Read();
                    read.start();
                    jfr.remove(jtfName);
                    jfr.remove(jtfport);
                    jfr.remove(jtfAddr);
                    jfr.remove(jcbtn);
                    jfr.add(jsbtn);
                    jfr.add(jtextInputChatSP);
                    jfr.add(jsbtndeco);
                    jfr.revalidate();
                    jfr.repaint();
                    chatText.setBackground(Color.WHITE);
                    usersList.setBackground(Color.WHITE);
                } catch (Exception ex) {
                    appendToPane(chatText, "<span>Could not connect to Server</span>");
                    JOptionPane.showMessageDialog(jfr, ex.getMessage());
                }
            }

        });

        // on deco
        jsbtndeco.addActionListener(new ActionListener()  {
            public void actionPerformed(ActionEvent ae) {
                jfr.add(jtfName);
                jfr.add(jtfport);
                jfr.add(jtfAddr);
                jfr.add(jcbtn);
                jfr.remove(jsbtn);
                jfr.remove(jtextInputChatSP);
                jfr.remove(jsbtndeco);
                jfr.revalidate();
                jfr.repaint();
                read.interrupt();
                usersList.setText(null);
                chatText.setBackground(Color.LIGHT_GRAY);
                usersList.setBackground(Color.LIGHT_GRAY);
                appendToPane(chatText, "<span>Connection closed.</span>");
                output.close();
            }
        });

    }

    // check if if all field are not empty
    public class TextListener implements DocumentListener{
        JTextField jtf1;
        JTextField jtf2;
        JTextField jtf3;
        JButton jcbtn;

        public TextListener(JTextField jtf1, JTextField jtf2, JTextField jtf3, JButton jcbtn){
            this.jtf1 = jtf1;
            this.jtf2 = jtf2;
            this.jtf3 = jtf3;
            this.jcbtn = jcbtn;
        }

        public void changedUpdate(DocumentEvent e) {}

        public void removeUpdate(DocumentEvent e) {
            if(jtf1.getText().trim().equals("") ||
                    jtf2.getText().trim().equals("") ||
                    jtf3.getText().trim().equals("")
            ){
                jcbtn.setEnabled(false);
            }else{
                jcbtn.setEnabled(true);
            }
        }
        public void insertUpdate(DocumentEvent e) {
            if(jtf1.getText().trim().equals("") ||
                    jtf2.getText().trim().equals("") ||
                    jtf3.getText().trim().equals("")
            ){
                jcbtn.setEnabled(false);
            }else{
                jcbtn.setEnabled(true);
            }
        }

    }

    // envoi des messages
    public void sendMessage() {
        try {
            String message = fielNewMessage.getText().trim();
            if (message.equals("")) {
                return;
            }
            this.oldMsg = message;
            output.println(message);
            fielNewMessage.requestFocus();
            fielNewMessage.setText(null);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            System.exit(0);
        }
    }

    public static void main(String[] args) throws Exception {
        WindowClient client = new WindowClient();
    }

    // read new incoming messages
    class Read extends Thread {
        public void run() {
            String message;
            while(!Thread.currentThread().isInterrupted()){
                try {
                    message = input.readLine();
                    if(message != null){
                        if (message.charAt(0) == '[') {
                            message = message.substring(1, message.length()-1);
                            ArrayList<String> ListUser = new ArrayList<String>(
                                    Arrays.asList(message.split(", "))
                            );
                            usersList.setText(null);
                            for (String user : ListUser) {
                                appendToPane(usersList, "@" + user);
                            }
                        }else{
                            appendToPane(chatText, message);
                        }
                    }
                }
                catch (IOException ex) {
                    System.err.println("Failed to parse incoming message");
                }
            }
        }
    }

    // send html to pane
    private void appendToPane(JTextPane tp, String msg){
        HTMLDocument doc = (HTMLDocument)tp.getDocument();
        HTMLEditorKit editorKit = (HTMLEditorKit)tp.getEditorKit();
        try {
            editorKit.insertHTML(doc, doc.getLength(), msg, 0, 0, null);
            tp.setCaretPosition(doc.getLength());
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
