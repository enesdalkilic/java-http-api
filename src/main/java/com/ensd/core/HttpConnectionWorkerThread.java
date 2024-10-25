package com.ensd.core;

import com.ensd.Router;
import com.ensd.http.HttpParser;
import com.ensd.http.HttpRequest;
import com.ensd.http.HttpResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class HttpConnectionWorkerThread extends Thread {
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpConnectionWorkerThread.class);
    private final Socket socket;
    private volatile boolean running = true; // Shutdown flag
    private Router router = null;
    private static final int SP = 0x20; // 32
    private static final int CR = 0x0D; // 13
    private static final int LF = 0x0A; // 10

    public HttpConnectionWorkerThread(Socket socket, Router router) {
        this.socket = socket;
        this.router = router;
    }

    @Override
    public void run() {
        OutputStream outputStream = null;
        try {
            LOGGER.info("Received request from {} {}", socket.getInetAddress(), Thread.currentThread().getId());
            outputStream = socket.getOutputStream();

            HttpParser httpParser = new HttpParser();

            HttpRequest request = httpParser.parseRequest(socket.getInputStream());
            if (request != null) {
                String path = request.getPath();
                HttpResponse response = new HttpResponse(outputStream, socket.getInputStream(), socket);
                router.newRequest(path, request, response);
            } else {
                socket.close();
            }


        } catch (IOException e) {
            LOGGER.error("Error handling client request: {}", e.getMessage());
        } finally {
            try {
                socket.close(); // Ensure the socket is closed after handling the request
            } catch (IOException e) {
                LOGGER.error("Error closing socket: {}", e.getMessage());
            }
        }
    }


    // Method to shut down the thread
    public void shutdown() {
        running = false; // Set running flag to false
        this.interrupt(); // Interrupt the thread if it's sleeping
    }
}
