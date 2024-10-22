package com.ensd.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class HttpParser {
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpParser.class);

    private Socket socket = null;


    public HttpParser(Socket socket) {
        this.socket = socket;
    }

    public HttpRequest parseRequest() {
        InputStream inputStream = null;
        HttpRequest httpRequest = new HttpRequest();

        try {
            inputStream = socket.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.US_ASCII));
            StringBuilder request = new StringBuilder();
            String line;

            //Reading the request
            String requestLine = reader.readLine();
            String[] requestLineParts = requestLine.split("\\s+");

            //Setting
            httpRequest.setMethod(requestLineParts[0]);
            httpRequest.setPath(requestLineParts[1]);
            httpRequest.setHttpVersion(requestLineParts[2]);

            throw new IOException("message");
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return httpRequest;
    }
}
