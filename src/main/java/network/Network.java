package network;

import filter.GsonProvider;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicInteger;

public class Network {

    private String ipAdress;
    private ArrayList<String> peersIPAdressesList;
    private static Network instance;
    private ResteasyClient client;
    private static String baseURI = "http://{ipAddress}:8080/api";
    private static AtomicInteger nextPeer = new AtomicInteger(0);

    private Network() {
        this.ipAdress = getIpAddress();
        this.peersIPAdressesList = getPeersIpAdresses();
        this.client = new ResteasyClientBuilder().build();
        this.client.register(GsonProvider.class);
    }

    public int getNumberOfPeers() {
        return peersIPAdressesList.size();
    }

    private int goToNextPeer() {
        if ((nextPeer.get() + 1) < getNumberOfPeers()) {
            return nextPeer.getAndIncrement();
        } else {
            return nextPeer.getAndSet(0);
        }
    }

    private ArrayList<String> getPeersIpAdresses() {
        ArrayList<String> peers = new ArrayList<>();
        // file containing ip adresses of machines
        File file = new File("./src/main/java/machine_information.txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String peerIpAddress;
            // reading ip address
            while ((peerIpAddress = br.readLine()) != null) {
                System.out.println("Adding peer with IP address = " + peerIpAddress);
                // add every peers
                if (!peerIpAddress.equals(getIpAdress())) {
                    peers.add(peerIpAddress);
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return peers;
    }

    public static String getIpAddress() {
        String ip;
        try {
            /* on parcours les interfaces réseaux pour obtenir l'adresse ip */
            Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface netint : Collections.list(nets)){
                Enumeration<?> e2 = netint.getInetAddresses();
                while (e2.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) e2.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.getHostAddress().contains(":")) {
                        ip = inetAddress.getHostAddress();
            System.out.println("Your current IP address : " + ip);
            return ip;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Network getInstance() {
        if (instance == null) {
            instance = new Network();
        }
        return instance;
    }

    private ResteasyClient getClient() {
        return client;
    }

    public String getIpAdress() {
        return ipAdress;
    }

    public Response sendPostRequest(String path, Object input, String mediaType, String responseMessage) {
        ArrayList<String> peers = peersIPAdressesList;

        ResteasyWebTarget target = getClient().target(UriBuilder.fromPath(baseURI));
        try {
            for (String ipAddress : peers) {
                System.out.println("• Sending post request to " + ipAddress);

                Response response = target
                        .path(path)
                        .resolveTemplate("ipAddress", ipAddress)
                        .queryParam("fromClient", false)
                        .request()
                        .post(Entity.entity(input, mediaType));

                System.out.println("--> Status code :" + response.getStatus());
                response.close();
            }
        } catch (Exception e) {
            return Response.ok(e.getMessage()).build();
        }
        return Response.ok(responseMessage).build();
    }

    public Response sendDataToPeer(ArrayList<String> buffer, String tableName, String path, String mediaType) {

        ResteasyWebTarget target = getClient().target(UriBuilder.fromPath(baseURI));
        try {
            String ipAddress = peersIPAdressesList.get(nextPeer.get());
            goToNextPeer();
            System.out.println("• Sending data request to " + ipAddress);

            Response response = target
                    .path(path)
                    .resolveTemplate("ipAddress", ipAddress)
                    .queryParam("tableName", tableName)
                    .request()
                    .post(Entity.entity(buffer, mediaType));
            System.out.println("--> Status code :" + response.getStatus());
            response.close();
        } catch (Exception e) {
            return Response.ok(e.getMessage()).build();
        }
        return Response.ok("Data successfully inserted into " + tableName + " to a peer").build();
    }


    public String getIpAdressFromIndex(int i) {
        return peersIPAdressesList.get(i);
    }

    public Object sendSelectToPeer(String ipAddress,String json, String tableName, String selectType, String path, String mediaType) {
        goToNextPeer();
        ResteasyWebTarget target = getClient().target(UriBuilder.fromPath(baseURI));
        Response response;
            try {
                System.out.println("• Sending select request to " + ipAddress);
                response = target
                        .path(path)
                        .resolveTemplate("ipAddress", ipAddress)
                        .queryParam("tableName", tableName)
                        .queryParam("selectType", selectType)
                        .queryParam("fromClient", false)
                        .request()
                        .post(Entity.entity(json, mediaType));
        } catch (Exception e) {
            return Response.ok(e.getMessage()).build();
        }
        return response;
    }
}
