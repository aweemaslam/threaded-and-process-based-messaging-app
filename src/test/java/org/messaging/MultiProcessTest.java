package org.messaging;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MultiProcessTest {

    private static final int PORT = 6000;

    @Test
    public void testServerClientCommunication() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Future<Integer> serverFuture = executor.submit(() -> {
            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                Socket socket = serverSocket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                String line;
                int received = 0;
                while ((line = in.readLine()) != null) {
                    received++;
                    out.println(line + " [reply]");
                    if (received >= 3) break; // stop condition
                }
                socket.close();
                return received;
            }
        });

        Future<Integer> clientFuture = executor.submit(() -> {
            Thread.sleep(100); // wait for server
            Socket socket = new Socket("localhost", PORT);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            int sent = 0;
            String[] messages = {"Hello1", "Hello2", "Hello3"};
            for (String msg : messages) {
                sent++;
                out.println(msg);
                in.readLine(); // wait for reply
            }
            socket.close();
            return sent;
        });

        int serverReceived = serverFuture.get(5, TimeUnit.SECONDS);
        int clientSent = clientFuture.get(5, TimeUnit.SECONDS);

        assertEquals(3, serverReceived, "Server should receive 3 messages");
        assertEquals(3, clientSent, "Client should send 3 messages");

        executor.shutdown();
    }
}
