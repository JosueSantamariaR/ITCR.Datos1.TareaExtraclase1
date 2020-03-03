package plaudernTec;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;


class SocketClient {
    /**
     * Variables using encapsulating
     */
    private String idServer;
    private int port;


    public SocketClient(String idServer, int port) {
        this.idServer = idServer;
        this.port = port;

    }

    public static void main(String[] args) throws IOException {
        new SocketClient("127.0.0.1", 12345).run();
    }

    public void run() throws IOException {
        /**
         * Connection the client with the server
         */
        Socket client = new Socket(idServer, port);
        System.out.println("Client connected");

        PrintStream output = new PrintStream(client.getOutputStream());
        /**
         * Peticion the user identifier
         */
        Scanner scanner = new Scanner(System.in);
        System.out.println("Write the IDUser: ");
        String idUser = scanner.nextLine();
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
        while (scanner.hasNextLine()) {
            output.println(scanner.nextLine());
        }

        /**
         * Closing the system
         */
        output.close();
        scanner.close();
        client.close();
    }


}
