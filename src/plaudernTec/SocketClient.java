package plaudernTec;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.io.InputStream;
import plaudernTec.User;


public class SocketClient {
    /**
     * Variables using encapsulating
     */
    private String idServer;
    private int portConnection;



    public static void main(String[] args) throws UnknownHostException, IOException {
        new SocketClient("127.0.0.1", 40001).run();
    }

    public  SocketClient(String idServer, int portConnection){
        this.idServer = idServer;
         this.portConnection = portConnection;

    }

    public void run() throws UnknownHostException, IOException{
        /**
         * Connection the client with the server
         */
        Socket client = new Socket(idServer,portConnection);
        System.out.println("Client connected");

        PrintStream output = new PrintStream(client.getOutputStream());
        /**
         * Peticion the user identifier
         */
        Scanner scann = new Scanner(System.in);
        System.out.println("Write the IDUser: ");
        String idUser = scann.nextLine();
        /**
         * Sending the user identifier to server
         */
        output.println(idUser);

        /**
         * Creation a new Thread for the messages in the server
         */
        new Thread(new Messages(client.getInputStream())).start();

        /**
         * Loop to see if there are new messages
         */
        while(scann.hasNextLine()){
            output.println(scann.nextLine());
        }

        /**
         * Closing the system
         */
        output.close();
        scann.close();
        client.close();
    }


}
