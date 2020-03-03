package plaudernTec;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SocketServer {

    /**
     * Variables using encapsulating
     */
    private int portConnection;
    private List<User> clients;
    private ServerSocket server;

    public static void main(String[] args) throws IOException {
        new SocketServer(12345).run();
    }

    public SocketServer(int port) {
        this.portConnection = portConnection;
        this.clients = new List<User>();
    }

    public void run() throws IOException {
        server = new ServerSocket(portConnection) {
            protected void finalize() throws IOException {
                this.close();
            }
        };
        System.out.println("Port"+ portConnection +"is running");

        while (true){
            /**
             * Accept new clients
             */
            Socket client = server.accept();

            /**
             * Get a ID user identifier
             */
            String idUser =(new Scanner(client.getInputStream() )).nextLine();
            idUser = idUser.replace(",","");
            idUser = idUser.replace(" ", "_");
            System.out.println("New Client: \"" + idUser + "\"\n\t Host:"+
            client.getInetAddress().getHostAddress());

            /**
             * Creation of new user
             * call User() class
             */

        }

    }


}
