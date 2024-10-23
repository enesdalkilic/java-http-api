package com.ensd.core;

import com.ensd.Router;
import com.ensd.handlers.RequestHandler;
import com.ensd.handlers.ResponseHandler;
import com.ensd.http.HttpParser;
import com.ensd.http.HttpRequest;

import com.ensd.http.HttpResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import java.util.Map;

public class HttpConnectionWorkerThread extends Thread {
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpConnectionWorkerThread.class);
    private final Socket socket;
    private volatile boolean running = true; // Shutdown flag
    private Router router = null;

    public HttpConnectionWorkerThread(Socket socket, Router router) {
        this.socket = socket;
        this.router = router;


        //   this.workerId = ++threadCounter;
    }

    @Override
    public void run() {
        OutputStream outputStream = null;
        try {
            outputStream = socket.getOutputStream();

            HttpParser httpParser = new HttpParser(socket);
            HttpRequest request = httpParser.parseRequest();

            LOGGER.info("Received request from {} {}", socket.getInetAddress(), Thread.currentThread().getId());

            String path = request.getPath();

            HttpResponse response = new HttpResponse(outputStream, socket.getInputStream(), socket);

            router.newRequest(path, request, response);


        } catch (IOException e) {
            System.err.println("Error handling client request: " + e.getMessage());
        } finally {
            try {
                socket.close(); // Ensure the socket is closed after handling the request
            } catch (IOException e) {
                System.err.println("Error closing socket: " + e.getMessage());
            }
        }
    }

    // Method to shut down the thread
    public void shutdown() {
        running = false; // Set running flag to false
        this.interrupt(); // Interrupt the thread if it's sleeping
    }
}
