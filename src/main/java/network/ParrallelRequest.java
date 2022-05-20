package network;

import java.util.concurrent.FutureTask;

public class ParrallelRequest extends FutureTask<String> {

    public ParrallelRequest(Runnable runnable, String result) {
        super(runnable, result);
    }
}
