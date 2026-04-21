package sentinel;

import server.RequestListener;

public class Main {
    public static void main(String[] args) {
        // Init ServerHealthChecker thread
        RequestListener requestListener = new RequestListener(8090);
        requestListener.start();
    }
}