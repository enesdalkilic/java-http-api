package com.ensd;

// IMPORTING ROUTES

import com.ensd.routes.GET.GetUser;
import com.ensd.routes.POST.*;

//Request Handlers
import com.ensd.http.HttpRequest;
import com.ensd.http.HttpResponse;

import com.ensd.handlers.RequestHandler;

//Dependencies
import org.json.JSONObject;

//UTILS
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

//Exceptions
import java.io.IOException;
import java.io.OutputStream;


public class Router {
    private final Map<String, RequestHandler> routes = new HashMap<>();

    private final OutputStream outputStream;

    public Router(OutputStream outputStream) {
        this.outputStream = outputStream;

        //**
        // ROUTES
        // *//

        putRoute("/new-user", new NewUser());
        putRoute("/get-user", new GetUser());
    }

    public void newRequest(String path, HttpRequest request, HttpResponse response) {
        RequestHandler HANDLER = routes.get(path);

        String method = request.getMethod();
        HANDLER.getMethod();
        if (HANDLER.getMethod().equals(method)) {
            HANDLER.handle(request, response);
        }else if(!HANDLER.getMethod().equals(method)){
            response.send(405, "Method Not Allowed");
        } else {
            response.send(404, "Not Found");
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
