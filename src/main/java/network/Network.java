package network;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Network {

    private String ipAdress;
    private static Network instance;

    public Network(){
        this.ipAdress = getIpAddress();
    }

    public static String getIpAddress() {
        String ip;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
            System.out.println("Your current IP address : " + ip);
            return ip;

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Network getInstance() {
        if(instance == null) {
            instance = new Network();
        }
        return instance;
    }

}
