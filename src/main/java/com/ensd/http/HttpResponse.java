package com.ensd.http;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class HttpResponse {
    private final OutputStream outputStream;
    private final InputStream inputStream;
    private final Socket socket;

    public HttpResponse(OutputStream outputStream, InputStream inputStream, Socket socket) {
        this.outputStream = outputStream;
        this.inputStream = inputStream;
        this.socket = socket;
    }

    public String getRawRequest() {
        HttpParser httpParser = new HttpParser(socket);
        return null;
    }

    public void send(String res) {
        // 13, 10
        String CRLF = "\r\n";

        String response = "HTTP/1.1 200 OK" + CRLF + // Status line
                "Content-Length: " + res.getBytes(StandardCharsets.US_ASCII).length + CRLF + // Content length
                "Content-Type: application/json" + CRLF + // Content type for JSON
                CRLF + // End of headers
                res + CRLF; // Body
        try {
            outputStream.write(response.getBytes(StandardCharsets.US_ASCII));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendJson(JSONObject json) {
        // 13, 10
        String CRLF = "\r\n";

        String response = "HTTP/1.1 200 OK" + CRLF + // Status line
                "Content-Length: " + json.toString().getBytes(StandardCharsets.US_ASCII).length + CRLF + // Content length
                "Content-Type: application/json" + CRLF + // Content type for JSON
                CRLF + // End of headers
                json + CRLF; // Body
        try {
            outputStream.write(response.getBytes(StandardCharsets.US_ASCII));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
