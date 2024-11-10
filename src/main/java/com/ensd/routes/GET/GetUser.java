package com.ensd.routes.GET;

import com.ensd.database.Collection;
import com.ensd.database.Database;
import com.ensd.handlers.RequestHandler;
import com.ensd.http.HttpRequest;
import com.ensd.http.HttpResponse;
import org.json.JSONObject;

public class GetUser implements RequestHandler {
    @Override
    public String getMethod() {
        return "GET";
    }
    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        JSONObject obj = new JSONObject();
        obj.put("testing", "1");
        Collection collection = new Collection("users");
        JSONObject doc = collection.findOne("name", "sherlock");
        response.sendJson(200, doc);
    }
}
