package com.ensd.routes.POST;

import com.ensd.handlers.RequestHandler;
import com.ensd.http.HttpRequest;
import com.ensd.http.HttpResponse;
import org.json.JSONObject;

public class NewUser implements RequestHandler {
    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        JSONObject obj = new JSONObject();

        obj.put("Method", request.getMethod());
        obj.put("HttpVersion", request.getHttpVersion());
        obj.put("Body", request.getBody());
        obj.put("headers", request.getHeaders());
        obj.put("original request", request.getOriginalHttpRequest());
        response.setCookie("test", "123",1123);
        response.sendJson(200, obj);
    }
}
