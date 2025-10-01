package org.messaging;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.messaging.event.GameEventManager;
import org.messaging.model.Player;

import java.awt.event.ActionEvent;

import static org.junit.jupiter.api.Assertions.*;

public class SingleProcessTest {

    private GameEventManager manager;
    private Player player1;
    private Player player2;
    private final int MAX_MESSAGES = 5; // smaller number for tests

    @BeforeEach
    public void setup() {
        manager = new GameEventManager();
        player1 = new Player("Player1", manager, MAX_MESSAGES, true);
        player2 = new Player("Player2", manager, MAX_MESSAGES, false);
    }

    @Test
    public void testPlayerRegistration() {
        assertEquals(2, manager.getListenersCount(), "Two players should be registered");
    }

    @Test
    public void testSendMessageIncrementsCounter() {
        player1.start();
        assertTrue(player1.getSentMessages() > 0);
    }

    @Test
    public void testReceivedMessagesCount() throws InterruptedException {
        player1.start();
        Thread.sleep(200); // wait for messages to be processed
        assertTrue(player2.getReceivedMessages() > 0);
        assertTrue(player1.getReceivedMessages() > 0);
    }

    @Test
    public void testMaxMessagesLimit() {
        player1.start();
        assertTrue(player1.getSentMessages() <= MAX_MESSAGES);
        assertTrue(player2.getSentMessages() <= MAX_MESSAGES);
    }

    @Test
    public void testMessageContent() {
        String msg = "TestMessage";
        ActionEvent event = new ActionEvent(player1, ActionEvent.ACTION_PERFORMED, msg);
        player2.actionPerformed(event);
        assertTrue(player2.getSentMessages() > 0);
    }
}
