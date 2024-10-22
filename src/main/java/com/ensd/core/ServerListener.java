package com.ensd.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerListener {
    private static ServerSocket serverSocket = null;
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerListener.class);
    private static final List<HttpConnectionWorkerThread> activeThreads = new ArrayList<>();
    public ServerListener(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void run() throws IOException {
        try {
            while (serverSocket.isBound() && !serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();


                // Create and start a new worker thread for each connection
                HttpConnectionWorkerThread workerThread = new HttpConnectionWorkerThread(socket);
                workerThread.start(); // This should create a new thread
                activeThreads.add(workerThread);
//                LOGGER.info(" * Connection accepted {}", socket.getInetAddress());
            }
        } catch (IOException e) {
            LOGGER.error("Problem with setting socket", e);
        } finally {
            shutdownWorkers();
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    private void shutdownWorkers() {
        System.out.println("Workers shut down");
        for (HttpConnectionWorkerThread worker : activeThreads) {
            worker.shutdown(); // Call shutdown on each active worker
        }
    }
}
