package com.ensd.handlers;

import com.ensd.http.HttpRequest;
import com.ensd.http.HttpResponse;

public interface RequestHandler {
    String getMethod();

    void handle(HttpRequest request, HttpResponse response);
}