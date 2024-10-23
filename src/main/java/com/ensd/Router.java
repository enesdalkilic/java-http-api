package com.ensd;

import com.ensd.handlers.RequestHandler;

//Route Handlers
import com.ensd.http.HttpRequest;
import com.ensd.http.HttpResponse;
import com.ensd.routes.User;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Router {
    private final Map<String, RequestHandler> routes = new HashMap<>();
    private final OutputStream outputStream;

    public Router(OutputStream outputStream) {
        this.outputStream = outputStream;

        //**
        // ROUTES
        // *//

        putRoute("/user", new User());
    }

    public void newRequest(String path, HttpRequest request, HttpResponse response) {
        RequestHandler HANDLER = routes.get(path);

        if (HANDLER != null) {
            HANDLER.handle(request, response);
        } else {
            notFound();
        }
    }

    public void putRoute(String path, RequestHandler handler) {
        routes.put(path, handler);
    }

    private void notFound() {
        // Debug: System.out.println("Route not found");
        JSONObject notFoundJson = new JSONObject();
        notFoundJson.put("status", 404);
        notFoundJson.put("message", "Not Found");
        // notFoundJson.put("path", request.getPath());
        String CRLF = "\r\n";

        String notFoundRes = "HTTP/1.1 404 Not Found" + CRLF + // Status line
                "Content-Length: " + notFoundJson.toString().getBytes(StandardCharsets.US_ASCII).length + CRLF + // Content length
                "Content-Type: application/json" + CRLF + // Content type for JSON
                CRLF + // End of headers
                notFoundJson + CRLF; // Body
        try {
            outputStream.write(notFoundRes.getBytes(StandardCharsets.US_ASCII));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
