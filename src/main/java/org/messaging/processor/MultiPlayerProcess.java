package org.messaging.processor;

import java.io.*;
import java.net.*;

/**
 * MultiPlayerProcess runs as a standalone process for multi-process version.
 */
public class MultiPlayerProcess {

    private static final int PORT = 5000;

    public static void main(String[] args) throws Exception {
        int id = Integer.parseInt(args[0]);        // Player ID
        boolean initiator = Boolean.parseBoolean(args[1]);

        if (id == 1) {
            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                System.out.println("Player1 (server) waiting...");
                Socket socket = serverSocket.accept();
                play(socket, initiator, "Player1");
            }
        } else {
            Thread.sleep(500); // Give server time
            Socket socket = new Socket("localhost", PORT);
            play(socket, initiator, "Player2");
        }
    }

    private static void play(Socket socket, boolean initiator, String self) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        int sent = 0, received = 0, max = 10;

        if (initiator) {
            sent++;
            String msg = "Hello [" + sent + "]";
            System.out.println(self + " [PID: " + ProcessHandle.current().pid() + "] sending: " + msg);
            out.println(msg);
        }

        String line;
        while ((line = in.readLine()) != null) {
            received++;
            System.out.println(self + " [PID: " + ProcessHandle.current().pid() + "] received: " + line);

            if (received >= max) {
                System.out.println(self + " [PID: " + ProcessHandle.current().pid() + "] received all messages. Game finished.");
                break;
            }

            sent++;
            String reply = line + " [" + sent + "]";
            System.out.println(self + " [PID: " + ProcessHandle.current().pid() + "] sending: " + reply);
            out.println(reply);
        }

        socket.close();
    }
}
