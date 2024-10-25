package com.ensd.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;

import java.util.HashMap;
import java.util.Map;

public class HttpParser {
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpParser.class);

    public HttpParser() {

    }

    public HttpRequest parseRequest(InputStream inputStream) {

        HttpRequest httpRequest = new HttpRequest();

        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.US_ASCII));

            //Reading the request

            String requestLine = reader.readLine();

            // Check if the request line is null
            if (requestLine == null) {
                return null;
            }

            String[] requestLineParts = new String[0];
            requestLineParts = requestLine.split("\\s+");


            //Setting
            httpRequest.setMethod(requestLineParts[0]);
            httpRequest.setPath(requestLineParts[1]);
            httpRequest.setHttpVersion(requestLineParts[2]);

            Map<String, String> headers = new HashMap<>();

            String headerLine;
            while (!(headerLine = reader.readLine()).isEmpty()) {
                String[] headerParts = headerLine.split(":\\s*", 2);
                if (headerParts.length == 2) {
                    headers.put(headerParts[0], headerParts[1]);
                }
            }

            if ("POST".equalsIgnoreCase(httpRequest.getMethod()) || "PUT".equalsIgnoreCase(httpRequest.getMethod())) {
                int _byte;
                String contentLengthHeader = headers.get("Content-Length");
                if (contentLengthHeader != null) {
                    int contentLength = Integer.parseInt(contentLengthHeader);
                    char[] bodyChars = new char[contentLength];
                    _byte = reader.read(bodyChars, 0, contentLength);
                    String body = new String(bodyChars);
                    httpRequest.setBody(body);
                }
            }

            httpRequest.setHeaders(headers);


        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return httpRequest;
    }

    private void parseRequestLine(InputStreamReader reader, HttpRequest request) {

    }


}

