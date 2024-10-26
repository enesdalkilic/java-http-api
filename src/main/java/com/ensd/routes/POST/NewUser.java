package com.ensd.routes.POST;

import com.ensd.database.Collection;
import com.ensd.database.Database;
import com.ensd.handlers.RequestHandler;
import com.ensd.http.HttpRequest;
import com.ensd.http.HttpResponse;
import org.json.JSONObject;

public class NewUser implements RequestHandler {

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        Collection collection = new Collection(Database.getCollection("movies"));
        JSONObject json = new JSONObject();
        json.put("name", "asd");
        collection.insertOne(json);
        response.sendJson(200, json);
    }
}
