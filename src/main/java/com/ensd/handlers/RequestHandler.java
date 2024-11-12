package com.ensd.handlers;

import com.ensd.http.HttpRequest;
import com.ensd.http.HttpResponse;
import org.json.JSONObject;

public interface RequestHandler {

    String getMethod();

    Boolean protectedPath();

    void handle(HttpRequest request, HttpResponse response);
}