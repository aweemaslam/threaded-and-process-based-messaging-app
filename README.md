# Player Messaging Game

This Java project demonstrates **inter-player messaging** with two distinct modes:

1. **Single-process mode**: All players run in the same JVM using threads and an asynchronous event manager.
2. **Multi-process mode**: Each player runs as a separate Java process (different PID) and communicates over TCP sockets.

---

## Project Structure

```
src/main/java/org/messaging
├── Main.java                 # Entry point for the game, supports single or multi-process
├── model
│   └── Player.java           # Represents a player and handles sending/receiving messages
├── event
│   └── GameEventManager.java # Manages asynchronous messaging between players
└── service
    └── MultiPlayerProcess.java # Standalone process for multi-process mode
```

---

## Features

* **Two players** communicate back and forth by sending messages.
* Each message includes a **counter** to track the number of sent and received messages.
* The program **stops gracefully** after the initiator sends and receives 10 messages.
* **Single-process mode**: Uses `ExecutorService` for asynchronous event handling within the same JVM.
* **Multi-process mode**: Players communicate via **TCP sockets**, each running in a separate JVM, with PID logging for clarity.
* Supports **thread-safe counters** and randomized message delays to simulate real asynchronous behavior.

---

## Requirements

* **Java 17+** (tested with Java 17)
* **Maven 3.8+**
* Unix-like OS recommended for running shell scripts

---

## Build the Project

Compile the project using Maven:

```bash
mvn clean compile
```

This will generate class files in `target/classes`.

---

## Running the Game

### Single-process Mode

All players run in the same JVM using threads.

```bash
java -cp target/classes org.messaging.Main
```

**Sample Output:**

```
Player1[Thread-1] sending: Hello [1]
Player2[Thread-2] received: Hello [1]
Player2[Thread-2] sending: Hello [1] [1]
Player1[Thread-1] received: Hello [1] [1]
...
```

---

### Multi-process Mode

Each player runs as a separate Java process and communicates via TCP sockets.

```bash
java -cp target/classes org.messaging.Main multi
```

* `Player1` acts as **server** and waits for a connection.
* `Player2` acts as **client** and connects to the server.
* Each process logs its **PID** to distinguish between them.

**Sample Output:**

```
Player1 (server) waiting...
Player1 [PID: 12345] sending: Hello [1]
Player2 [PID: 12346] received: Hello [1]
Player2 [PID: 12346] sending: Hello [1] [1]
...
```

---

## Shell Script

You can create a shell script `run.sh` to build and run the game:

```bash
#!/bin/bash

# Compile the project
mvn clean compile

# Run single-process mode
java -cp target/classes org.messaging.Main

# Uncomment below to run multi-process mode
# java -cp target/classes org.messaging.Main multi
```

Make it executable:

```bash
chmod +x runPlayerMessaging.sh
./runPlayerMessaging.sh
```

---

## Testing

Unit tests are provided for single-process mode using **JUnit 5**:

```bash
mvn test
```

> Note: Multi-process mode involves separate JVMs and sockets, which are better tested manually.

---

## Design Overview

* **Player.java**:
  Handles sending and receiving messages and maintains counters for messages sent and received.

* **GameEventManager.java**:
  Registers players and asynchronously broadcasts messages using a thread pool.

* **MultiPlayerProcess.java**:
  Standalone player process for multi-process mode using TCP sockets.

* **Main.java**:
  Entry point for single-process and multi-process modes. Chooses mode based on command-line arguments.
