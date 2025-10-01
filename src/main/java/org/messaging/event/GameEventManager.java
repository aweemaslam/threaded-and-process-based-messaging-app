package org.messaging.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * GameEventManager is responsible for registering players and broadcasting messages (events) to them.
 */
public class GameEventManager {

    private final List<ActionListener> listeners = new CopyOnWriteArrayList<>();
    // Thread pool to handle all message events
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public void register(ActionListener listener) {
        listeners.add(listener);
    }

    public void fireEvent(ActionEvent event) {
        // Send the event to all registered players asynchronously
        for (ActionListener listener : listeners) {
            executor.submit(() -> listener.actionPerformed(event));
        }
    }

    public int getListenersCount() {
        return listeners.size();
    }
}