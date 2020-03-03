package plaudernTec;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Messages implements Runnable{
    private InputStream server;

    public Messages(InputStream server){
        this.server = server;

    }


    public void run() {
        Scanner scanner = new Scanner(server);
        String temporal = "";
        while(scanner.hasNextLine()){
            temporal= scanner.nextLine();
            if(temporal.charAt(0) == '['){
                temporal= temporal.substring(1,temporal.length()-1);
                System.out.println("\nUsers List"+
                new ArrayList<String>(Arrays.asList(temporal.split(","))) + "\n");
            }else{
                try{
                    System.out.println("\n" + getTagValue(temporal));
                } catch (Exception ignore){}
            }

        }
        scanner.close();
    }
    public static String getTagValue(String xml){
        return  xml.split(">")[2].split("<")[0] + xml.split("<span>")[1].split("</span>")[0];
    }

}
