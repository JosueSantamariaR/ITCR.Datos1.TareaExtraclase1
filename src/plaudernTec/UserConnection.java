package plaudernTec;

import java.util.Scanner;

public class UserConnection implements Runnable {
    private SocketServer server;
    private User user;

    public UserConnection(SocketServer server, User user) {
        this.server = server;
        this.user = user;
        this.server.showUsersList();


    }

    public void run() {
        String messageN;

        Scanner entrance = new Scanner(this.user.getInputStream());
        while (entrance.hasNextLine()) {
            messageN = entrance.nextLine();

            if (messageN.charAt(0) == '@') {
                if (messageN.contains(" ")) {
                    System.out.println("private msn : " + messageN);
                    int firstSpace = messageN.indexOf(" ");
                    String userPrivate = messageN.substring(1, firstSpace);
                    server.privateMessages(
                            messageN.substring(
                                    firstSpace + 1
                            ), user, userPrivate
                    );
                }
            } else {
                server.CompruveMss(messageN, user);
            }
        }
        server.userX(user);
        this.server.showUsersList();
        entrance.close();
    }

}
