package com.ensd.routes;

import com.ensd.handlers.RequestHandler;
import com.ensd.http.HttpRequest;
import com.ensd.http.HttpResponse;
import org.json.JSONObject;

public class User implements RequestHandler {
    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        JSONObject obj = new JSONObject();
        obj.put("testing", "1");

        response.sendJson(200, obj);
    }
}
