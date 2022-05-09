package network;

import com.sun.mail.iap.Response;
import endpoints.TableEndpoint;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.ws.rs.core.UriBuilder;
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
                System.out.println("Adding peer with IP address = " + peerIpAddress);
                // add every peers
                if (!peerIpAddress.equals(instance.getIpAdress())) {
                    peers.add(peerIpAddress);
                }
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

    public String getIpAdress() {
        return ipAdress;
    }

    public ArrayList<String> getPeersIPAdressesList() {
        return peersIPAdressesList;
    }

/*    public static void sendPostRequest(String ipAdress, String endpoint, Class<?> service) {
        final String path = "http://" + ipAdress + ":8080/" + endpoint;

        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target(UriBuilder.fromPath(path));
        return target.proxy(service);
    }

    public static Response sendPostRequestToPeers(String request, Object parameter, String endpoint, Class<?> service){
        ArrayList<String> peers = Network.getInstance().getPeersIPAdressesList();

        for(String ipAddress : peers){
            String path = "http://" + ipAddress + ":8080/" + endpoint;
            Object proxy = getProxy(ipAddress, endpoint, service);
            System.out.println("Sending to " + ipAddress);

        }

    }*/
}
