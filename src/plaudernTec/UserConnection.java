package plaudernTec;

import java.util.Scanner;

class UserConnection implements Runnable{
    private SocketServer server;
    private User user;

    public UserConnection(SocketServer server, User user) {
        this.server = server;
        this.user = user;

    }
    public void run(){
        String messageN;

        Scanner sc = new Scanner(this.user.getInputStream());
    }

}
