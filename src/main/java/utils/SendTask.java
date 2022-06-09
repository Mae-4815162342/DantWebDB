package utils;

import controller.Worker;
import exception.BadResponseFromPeer;
import network.Network;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

public class SendTask implements Callable<Void> {
    private String ipAddress;
    private String json;
    private String table;
    private String type;
    public SendTask(String ipAddress, String json, String table, String type) {
        this.ipAddress = ipAddress;
        this.json = json;
        this.table = table;
        this.type = type;
    }
    public Void call() throws Exception {
        Object res = Network.getInstance().sendSelectToPeer(ipAddress, json, table, type, "/get-machine", MediaType.APPLICATION_JSON);
        System.out.println("RES:"  + res);
        Worker.getInstance().addLinesToAnswer(res);
        return null;
    }
};