package exception;

public class BadResponseFromPeer extends Exception{
    public BadResponseFromPeer(int status) {
        super("Bad response from peer : error " + status);
    }
}
