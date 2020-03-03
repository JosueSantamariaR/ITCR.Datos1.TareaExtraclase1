package plaudernTec;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SocketServer {

    static List<User> clients;
    /**
     * Variables using encapsulating
     */
    private int port;
    private ServerSocket server;

    public SocketServer(int port) {
        this.port = port;
        clients = new ArrayList<User>();
    }

    public static void main(String[] args) throws IOException {
        new SocketServer(12345).run();
    }

    public void run() throws IOException {
        server = new ServerSocket(port) {
            @Override
            protected void finalize() throws IOException {
                this.close();
            }
        };
        System.out.println("Port " + port + " is running");

        while (true) {
            /**
             * Accept new clients
             */
            Socket client = server.accept();

            /**
             * Get a ID user identifier
             */
            String idUser = (new Scanner(client.getInputStream())).nextLine();
            idUser = idUser.replace(",", "");
            idUser = idUser.replace(" ", "_");
            System.out.println("New Client: \"" + idUser + "\"\n\t Host:" +
                    client.getInetAddress().getHostAddress());

            /**
             * Creation of new client
             * call User() class
             */
            User newClient = new User(client, idUser);

            clients.add(newClient);

            newClient.getOutStream().println(newClient.getName());
            /**
             * Creation a new Thread for the messages
             */
            new Thread(new UserConnection(this, newClient)).start();


        }


    }

    public void CompruveMss(String msn, User userSender) {
        for (User client : clients) {
            client.getOutStream().println(userSender.getName() +
                    "<span>: " + msn + "</span>");
        }
    }

    public void userX(User user) {
        clients.remove(user);
    }

    /**
     * Show the user list to each user
     */
    public void showUsersList() {
        for (User client : clients) {
            client.getOutStream().println(clients);
        }
    }

    public void privateMessages(String msn, User userSender, String user) {
        boolean exist = false;
        for (User client : clients) {
            if (client.getName().equals(user) && client != userSender) {
                exist = true;
                userSender.getOutStream().println((userSender.getName() +
                        " --> " + client.getName() + ": " + msn));
                client.getOutStream().println(
                        "(<b>Mensaje Privado</b>)" + userSender.getName() +
                                "<span>: " + msn + "</span>");
            }
        }
        if (!exist) {
            userSender.getOutStream().println(userSender.getName() +
                    " --> (<b>no one!</b>): " + msn);
        }
    }

}
