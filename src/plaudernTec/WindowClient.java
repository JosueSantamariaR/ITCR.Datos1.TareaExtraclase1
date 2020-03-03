package plaudernTec;

import javax.swing.*;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;


public class WindowClient extends Thread {

    final JTextPane chaText = new JTextPane();
    final JTextField inpuText = new JTextField();
    final JTextPane usersList = new JTextPane();
    BufferedReader input;
    PrintWriter output;
    Socket server;
    private int portConnection;
    private String previusMsn = "";
    private String serverName;
    private String idUser;
    private Thread read;

    public WindowClient() {
        this.serverName = "LocalHost";
        this.portConnection = 12345;
        this.idUser = "ID_User";
        /**
         * Type of the font in the window
         */
        String fontfamily = "Times New Roman, Georgia-serif";
        Font font = new Font(fontfamily, Font.PLAIN, 16);

        final JFrame jFrame = new JFrame("PlaudernTec");
        jFrame.getContentPane().setLayout(null);
        jFrame.setSize(500, 460);
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /**
         * Area of chat text details
         */
        chaText.setBounds(25, 25, 490, 320);
        chaText.setFont(font);
        chaText.setMargin(new Insets(6, 6, 6, 6));
        chaText.setEditable(false);
        JScrollPane jtextFilDiscuSP = new JScrollPane(chaText);
        jtextFilDiscuSP.setBounds(25, 25, 290, 320);
        /**
         * Type of text allowed in chat area
         */
        chaText.setContentType("text/html");
        chaText.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);

        /**
         * Users list module sizes
         */
        usersList.setBounds(520, 25, 156, 320);
        usersList.setEditable(true);
        usersList.setFont(font);
        usersList.setMargin(new Insets(6, 6, 6, 6));
        usersList.setEditable(false);
        JScrollPane jsplistuser = new JScrollPane(usersList);
        jsplistuser.setBounds(320, 25, 150, 320);
        /**
         * Type of text allowed in users list
         */
        usersList.setContentType("text/html");
        usersList.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);

        /**
         * Details of the field to write the chat
         */
        inpuText.setBounds(0, 350, 100, 50);
        inpuText.setFont(font);
        inpuText.setMargin(new Insets(6, 6, 6, 6));
        final JScrollPane jtextInputChatSP = new JScrollPane(inpuText);
        jtextInputChatSP.setBounds(25, 350, 290, 50);

        /**
         * Button to send a messages
         */
        final JButton jbSend = new JButton("Enviar");
        jbSend.setFont(font);
        jbSend.setBounds(320, 350, 150, 50);

        /**
         * Button to return where u can choose another indentifier
         */
        final JButton jbDesconect = new JButton("Desconectarse");
        jbDesconect.setFont(font);
        jbDesconect.setBounds(320, 10, 150, 15);

        inpuText.addKeyListener(new KeyAdapter() {

            /**
             * Method to send messages just pressing enter
             * @param e
             */
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }

                /**
                 * For get your last message write
                 */
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    String currentMessage = inpuText.getText().trim();
                    inpuText.setText(previusMsn);
                    previusMsn = currentMessage;
                }

                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    String currentMessage = inpuText.getText().trim();
                    inpuText.setText(previusMsn);
                    previusMsn = currentMessage;
                }
            }
        });

        /**
         * Click in the send button for send
         */
        jbSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                sendMessage();
            }
        });

        /**
         * Previus view to the connection
         * Can modifier identifier, port, server ip
         */
        final JTextField jtfName = new JTextField(this.idUser);
        final JTextField jtfport = new JTextField(Integer.toString(this.portConnection));
        final JTextField jtfAddr = new JTextField(this.serverName);
        final JButton jButtonConnect = new JButton("Conectarse");

        /**
         * Each space should has a parameters
         */
        jtfName.getDocument().addDocumentListener(new TextViewer(jtfName, jtfport, jtfAddr, jButtonConnect));
        jtfport.getDocument().addDocumentListener(new TextViewer(jtfName, jtfport, jtfAddr, jButtonConnect));
        jtfAddr.getDocument().addDocumentListener(new TextViewer(jtfName, jtfport, jtfAddr, jButtonConnect));

        /**
         * Position of the modules and button
         */
        jButtonConnect.setFont(font);
        jtfAddr.setBounds(25, 380, 80, 40);//Server name
        jtfName.setBounds(110, 380, 80, 40);//Identifier
        jtfport.setBounds(195, 380, 80, 40);//port
        jButtonConnect.setBounds(320, 380, 150, 40);

        /**
         * The format color of the background in the first view
         */
        chaText.setBackground(Color.LIGHT_GRAY);
        usersList.setBackground(Color.LIGHT_GRAY);

        /**
         * Call things of the window
         */
        jFrame.add(jButtonConnect);
        jFrame.add(jtextFilDiscuSP);
        jFrame.add(jsplistuser);
        jFrame.add(jtfName);
        jFrame.add(jtfport);
        jFrame.add(jtfAddr);
        jFrame.setVisible(true);


        /**
         * Executing
         */
        jButtonConnect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    idUser = jtfName.getText();
                    String port = jtfport.getText();
                    serverName = jtfAddr.getText();
                    portConnection = Integer.parseInt(port);
                    server = new Socket(serverName, portConnection);
                    input = new BufferedReader(new InputStreamReader(server.getInputStream()));
                    output = new PrintWriter(server.getOutputStream(), true);

                    //Send identifier to the server
                    output.println(idUser);

                    // create new Read Thread
                    read = new Read();
                    read.start();
                    jFrame.remove(jtfName);
                    jFrame.remove(jtfport);
                    jFrame.remove(jtfAddr);
                    jFrame.remove(jButtonConnect);
                    jFrame.add(jbSend);
                    jFrame.add(jtextInputChatSP);
                    jFrame.add(jbDesconect);
                    jFrame.revalidate();
                    jFrame.repaint();
                    chaText.setBackground(Color.lightGray);
                    usersList.setBackground(Color.lightGray);
                } catch (Exception ex) {
                    addToTextArea(chaText, "<span>Error with server connection</span>");
                    JOptionPane.showMessageDialog(jFrame, ex.getMessage());
                }
            }

        });

        /**
         * Desconect press button
         */
        jbDesconect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                jFrame.add(jtfName);
                jFrame.add(jtfport);
                jFrame.add(jtfAddr);
                jFrame.add(jButtonConnect);
                jFrame.remove(jbSend);
                jFrame.remove(jtextInputChatSP);
                jFrame.remove(jbDesconect);
                jFrame.revalidate();
                jFrame.repaint();
                read.interrupt();
                usersList.setText(null);
                chaText.setBackground(Color.LIGHT_GRAY);
                usersList.setBackground(Color.LIGHT_GRAY);
                addToTextArea(chaText, "<span>Connection closed.</span>");
                output.close();
            }
        });

    }

    /**
     * Call of the window
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        WindowClient client = new WindowClient();
    }

    /**
     * Send messages
     */
    public void sendMessage() {
        try {
            String message = inpuText.getText().trim();
            if (message.equals("")) {
                return;
            }
            this.previusMsn = message;
            output.println(message);
            inpuText.requestFocus();
            inpuText.setText(null);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            System.exit(0);
        }
    }

    /**
     * Apennd text in the pane of the window
     * Convert to html format
     *
     * @param tp
     * @param msg
     */
    private void addToTextArea(JTextPane tp, String msg) {
        HTMLDocument doc = (HTMLDocument) tp.getDocument();
        HTMLEditorKit editorKit = (HTMLEditorKit) tp.getEditorKit();
        try {
            editorKit.insertHTML(doc, doc.getLength(), msg, 0, 0, null);
            tp.setCaretPosition(doc.getLength());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Reading and check the messages
     */
    class Read extends Thread {
        public void run() {
            String message;
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    message = input.readLine();
                    if (message != null) {
                        if (message.charAt(0) == '[') {
                            message = message.substring(1, message.length() - 1);
                            ArrayList<String> ListUser = new ArrayList<String>(
                                    Arrays.asList(message.split(", "))
                            );
                            usersList.setText(null);
                            for (String user : ListUser) {
                                addToTextArea(usersList, "@" + user);
                            }
                        } else {
                            addToTextArea(chaText, message);
                        }
                    }
                } catch (IOException ex) {
                    System.err.println("Error, with the incoming message");
                }
            }
        }
    }
}
