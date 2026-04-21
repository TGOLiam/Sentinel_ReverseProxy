import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RequestListener {
    private int PORT;
    private ServerSocket serverSocket;

    public RequestListener(final int port)
    {
        this.PORT = port;

        try {
            serverSocket = new ServerSocket(this.PORT);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public int getPORT()
    {
        return PORT;
    }

    public void start(){
        System.out.println("Server running at port: " + PORT);

        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        while(true)
        {
            try{
                Socket socket = serverSocket.accept();
                executor.submit(
                        () -> handle_test(socket)
                );
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void parseHTTP()
    {

    }

    private void handle_test(Socket socket)
    {
        try (socket){
            String clientIP = socket.getInetAddress().getHostAddress();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            String msg = in.readLine();
            System.out.printf("[%s] %s\n", clientIP, msg);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void handle(Socket socket)
    {
        try (socket)
        {

        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
