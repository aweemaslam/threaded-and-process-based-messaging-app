package org.messaging;

import org.messaging.event.GameEventManager;
import org.messaging.model.Player;

public class Main {

    private static final int PLAYER_COUNT = 2;
    private static final int MAX_MESSAGES = 10;

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("multi")) {
            runMultiProcess();
        } else {
            runSingleProcess();
        }
    }

    private static void runSingleProcess() {

        GameEventManager manager = new GameEventManager();
        // create N players
        Player initPlayer = null;
        for (int i = 1; i <= PLAYER_COUNT; i++) {
            if (i == 1) {
                initPlayer = new Player("Player" + i, manager, MAX_MESSAGES, true);
            } else {
                new Player("Player" + i, manager, MAX_MESSAGES, false);
            }
        }
        // start game
        initPlayer.start();
    }

    private static void runMultiProcess() {
        try {
            ProcessBuilder p1 = new ProcessBuilder(
                "java", "-cp", "target/classes", "org.messaging.processor.MultiPlayerProcess", "1", "true"
            );
            ProcessBuilder p2 = new ProcessBuilder(
                "java", "-cp", "target/classes", "org.messaging.processor.MultiPlayerProcess", "2", "false"
            );

            // Inherit IO to see output from processes
            p1.inheritIO();
            p2.inheritIO();

            // Start server first
            Process server = p1.start();

            // Wait a bit to ensure server socket is ready
            Thread.sleep(500);

            Process client = p2.start();

            // Optionally wait for both processes to finish
            server.waitFor();
            client.waitFor();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


