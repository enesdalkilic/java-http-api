package com.ensd.core;

import com.ensd.Router;

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
                Router router = new Router(socket.getOutputStream());

                // Create and start a new worker thread for each connection

                HttpConnectionWorkerThread workerThread = new HttpConnectionWorkerThread(socket, router);
                workerThread.start(); // This should create a new thread
                activeThreads.add(workerThread);
            }
        } catch (IOException e) {
            LOGGER.error("Problem with setting socket", e);
        } finally {
            shutdownWorkers();
            serverSocket.close();
        }
    }

    private void shutdownWorkers() {

        for (HttpConnectionWorkerThread worker : activeThreads) {
            worker.shutdown(); // Call shutdown on each active worker
        }
    }
}
