package org.messaging.model;

import org.messaging.event.GameEventManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
/**
 * Player represents a participant in the messaging game. Each Player can receive messages and reply with a new message containing a counter.
 */
public class Player implements ActionListener {

    private final String name;
    private final GameEventManager manager;
    private final boolean initiator;
    private final int maxMessages;
    private Integer sentMessages = 0;

    public Integer getReceivedMessages() {
        return receivedMessages;
    }

    public Integer getSentMessages() {
        return sentMessages;
    }

    private Integer receivedMessages = 0;

    private static final Random random = new Random();

    public Player(String name, GameEventManager manager, int maxMessages, boolean initiator) {
        this.name = name;
        this.manager = manager;
        this.maxMessages = maxMessages;
        this.initiator = initiator;
        manager.register(this);
    }

    /**
     * Starts the game for the initiator.
     */
    public void start() {
        if (initiator) {
            sendMessage("Hello");
        }
    }

    private Integer incrementAndGetSentMessages() {
        synchronized (sentMessages) {
            sentMessages = sentMessages + 1;
            return sentMessages;
        }
    }

    private Integer incrementAndGetReceivedMessages() {
        synchronized (receivedMessages) {
            receivedMessages = receivedMessages + 1;
            return receivedMessages;
        }
    }

    /**
     * Sends a message to all other players.
     */
    private synchronized void sendMessage(String message) {
        if (incrementAndGetSentMessages() > maxMessages) {
            System.out.println(name + " finished sending messages.");
            return;
        }
        message = message + " [" + sentMessages + "]";
        System.out.println(name + "[Thread-" + Thread.currentThread().getId() + "] sending: " + message);
        manager.fireEvent(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, message));
    }

    @Override
    public synchronized void actionPerformed(ActionEvent e) {
        // Ignore own messages
        if (e.getSource() == this) {
            return;
        }

        String received = e.getActionCommand();
        System.out.println(name + "[Thread-" + Thread.currentThread().getId() + "] received: " + received);

        // Stop condition
        if (incrementAndGetReceivedMessages() >= maxMessages) {
            System.out.println(name + " received all messages. Game finished.");
            System.exit(0);
        }

        // Random delay to simulate async processing
        try {
            Thread.sleep(random.nextInt(100) + 50);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        // Reply to the message
        sendMessage(received);
    }
}
