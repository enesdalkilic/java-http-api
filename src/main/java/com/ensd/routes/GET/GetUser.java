package com.ensd.routes.GET;

import com.ensd.handlers.RequestHandler;
import com.ensd.http.HttpRequest;
import com.ensd.http.HttpResponse;

public class GetUser implements RequestHandler {
    @Override
    public void handle(HttpRequest request, HttpResponse response) {

        response.send(200, "OK");
    }
}
