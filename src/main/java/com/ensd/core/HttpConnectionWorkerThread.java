package com.ensd.core;

import com.ensd.http.HttpParser;
import com.ensd.http.HttpRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class HttpConnectionWorkerThread extends Thread {
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpConnectionWorkerThread.class);
    private final Socket socket;
    private volatile boolean running = true; // Shutdown flag

    public HttpConnectionWorkerThread(Socket socket) {
        this.socket = socket;
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

            String jsonResponse = "{\"method\": \"" + request.getMethod() + "\"," +
                    "\"version\": \"" + request.getHttpVersion() + "\"," +
                    "\"path\": \"" + request.getPath() + "\"," +
                    "}";
            String CRLF = "\r\n"; // 13, 10
            String response = "HTTP/1.1 200 OK" + CRLF + // Status line
                    "Content-Length: " + jsonResponse.getBytes(StandardCharsets.US_ASCII).length + CRLF + // Content length
                    "Content-Type: application/json" + CRLF + // Content type for JSON
                    CRLF + // End of headers
                    jsonResponse + CRLF; // Body

            // Send response to the client
            outputStream.write(response.getBytes(StandardCharsets.US_ASCII));
            outputStream.flush(); // Ensure response is sent
//            shutdown();
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
