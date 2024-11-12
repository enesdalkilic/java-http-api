package com.ensd.routes.GET;

import com.ensd.handlers.RequestHandler;
import com.ensd.http.HttpRequest;
import com.ensd.http.HttpResponse;
import com.ensd.models.UserModel;
import org.json.JSONObject;

public class GetUser implements RequestHandler {
    @Override
    public String getMethod() {
        return "GET";
    }

    @Override
    public Boolean protectedPath() {
        return true;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        UserModel usermodel = new UserModel();
        JSONObject USER = usermodel.findOne("user_id", request.getSession().getUserID());
        USER.remove("_id");
        USER.remove("password");
        response.sendJson(200, USER);
    }
}
