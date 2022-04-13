package network;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Network {

    private String ipAdress;
    private ArrayList<String> peersIPAdressesList;
    private static Network instance;

    public Network(){
        this.ipAdress = getIpAddress();
        this.peersIPAdressesList = getPeersIpAdresses();
    }

    private ArrayList<String> getPeersIpAdresses() {
        ArrayList<String> peers = new ArrayList<>();

        // file containing ip adresses of machines
        File file = new File("./src/main/java/machine_information.txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String peerIpAddress;
            // reading ip address
            while ((peerIpAddress = br.readLine()) != null){
                System.out.println(peerIpAddress);
                peers.add(peerIpAddress);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return peers;
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
