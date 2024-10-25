package com.ensd.http;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpResponse.class);
    private final OutputStream outputStream;

    private final Map<String, String> headers = new HashMap<>();


    public HttpResponse(OutputStream outputStream, InputStream inputStream, Socket socket) {
        this.outputStream = outputStream;
    }


    public void send(int statusCode, String res) {
        sendResponse(outputStream, statusCode, headers, res);
    }

    public void sendJson(int statusCode, JSONObject res) {
        headers.put("Content-Type", " application/json");
        sendResponse(outputStream, statusCode, headers, res.toString());
    }

    public void addHeader(String a, String b) {
        headers.put(a, b);
    }

    public void writeHeaders() throws IOException {
        int statusCode = 200;
        String statusText = "OK";
        // Status line (e.g., HTTP/1.1 200 OK)
        outputStream.write(("HTTP/1.1 " + statusCode + " " + statusText + "\r\n").getBytes());

        // Headers
        for (Map.Entry<String, String> header : headers.entrySet()) {
            outputStream.write((header.getKey() + ": " + header.getValue() + "\r\n").getBytes());
        }

        // End headers with a blank line
        outputStream.write("\r\n".getBytes());
    }

    private void sendResponse(OutputStream outputStream, int statusCode, Map<String, String> headers, String body) {
        try {
            StringBuilder response = new StringBuilder();
            response.append("HTTP/1.1 ").append(statusCode).append(" OK\r\n");
            for (String header : headers.keySet()) {
                response.append(header).append(": ").append(headers.get(header)).append("\r\n");
            }
            response.append("Content-Length: ").append(body.length()).append("\r\n");
            response.append("\r\n").append(body);

            outputStream.write(response.toString().getBytes());
            outputStream.flush();
        } catch (Exception e) {
            LOGGER.error("");
        }
    }

    /**
     * @param expiresIn It will be added to current time and will set the new date to cookies Expires.
     */
    public void setCookie(String name, String value, long... expiresIn) {
        StringBuilder cookieValue = new StringBuilder();
        cookieValue.append(name)
                .append("=")
                .append(value)
                .append(";")
                .append("Secure;")
                .append("HttpOnly;")
                .append("SameSite;");

        long currentDate = new Date().getTime();

        Date expireDate = new Date(currentDate + expiresIn[0]);
        if (expiresIn[0] > 0) {
            cookieValue.append("Expires=").append(expireDate).append(";");
        }


        headers.put("Set-Cookie", String.valueOf(cookieValue));
    }

    private void handlePreflightResponse(OutputStream output) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Origin", "*");  // Allow all origins
        headers.put("Access-Control-Allow-Methods", "GET, POST, OPTIONS");  // Allowed methods
        headers.put("Access-Control-Allow-Headers", "Content-Type");  // Allowed headers

        sendResponse(output, 204, headers, "ok");  // 204 No Content response for OPTIONS
    }

    private void handleResponseWithCORSHeaders(OutputStream output) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Origin", "*");  // Adjust the origin based on your requirements

        // Your response content
        String responseBody = "ok";

        sendResponse(output, 200, headers, responseBody);
    }
}
