package plaudernTec;

import java.util.Scanner;

class UserConnection implements Runnable{
    private SocketServer server;
    private User user;

    public UserConnection(SocketServer server, User user) {
        this.server = server;
        this.user = user;

    }
    public void run() {
        String messageN;

        Scanner scanner = new Scanner(this.user.getInputStream());
        while (scanner.hasNextLine()) {
            messageN = scanner.nextLine();

            if (messageN.charAt(0) == '@') {
                if (messageN.contains(" ")) {
                    System.out.println("private msg : " + messageN);
                    int firstSpace = messageN.indexOf(" ");
                    String userPrivate = messageN.substring(1, firstSpace);
                    server.privateMessages(
                            messageN.substring(
                                    firstSpace + 1, messageN.length()
                            ), user, userPrivate
                    );
                }
            } else {
                server.groupMsn(messageN, user);
            }
        }
        user.userX(user);
        this.server.showUsersList();
        scanner.close();
    }

}
