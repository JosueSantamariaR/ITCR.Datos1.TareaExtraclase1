package plaudernTec;

import java.io.InputStream;

public class Messages implements Runnable{
    private InputStream server;

    public Messages(InputStream server){
        this.server = server;

    }

    @Override
    public void run() {

    }
}
