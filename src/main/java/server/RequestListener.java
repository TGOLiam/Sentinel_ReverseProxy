package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RequestListener {
    private int PORT;
    private ServerSocket serverSocket;

    public RequestListener(final int port) {
        this.PORT = port;

        try {
            serverSocket = new ServerSocket(this.PORT);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public int getPORT() {
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
                        () -> handle(socket)
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Request parseHTTP(Socket socket) {
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            // server.Request line
            String requestLine = in.readLine();
            String[] parts = requestLine.split(" ");
            String method = parts[0];
            String path = parts[1];
            String version = parts[2];

            // Headers
            Map<String, String> headers = new HashMap<>();
            String line;
            while (!(line = in.readLine()).isEmpty()) {
                String[] header = line.split(": ", 2);
                headers.put(header[0], header[1]);
            }

            // Body
            String body = null;
            if (headers.containsKey("Content-Length")) {
                int contentLength = Integer.parseInt(headers.get("Content-Length"));
                char[] bodyChars = new char[contentLength];
                in.read(bodyChars);
                body = new String(bodyChars);
            }

            return new Request(method, path, version, headers, body);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void writeResponse(Socket socket, Response response) {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String HTTPHeader = "HTTP/1.1 %d %s\r\n";
            // Status line
            out.printf(HTTPHeader, response.statusCode(), response.statusMessage());

            // Headers
            response.headers().forEach((k, v) -> out.printf("%s: %s\r\n", k, v));

            // Header terminator
            out.print("\r\n");

            // Body
            if (response.body() != null) {
                out.print(response.body());
            }

            out.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void handle(Socket socket) {
        try (socket)
        {
            // ---- Rate limiter ----
            // Add client to hash map
            // Check if within limits
            // if limit exceeded, response with BAD

            // Then parse
            Request request = parseHTTP(socket);
            System.out.println(request);

            // ---- Router with Load Balancing ----
            // Which one of servers has the least load?
            // Forward request to that server
            // Receive server response

            // Output back to client
            // writeResponse()
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
