package com.ensd.routes.POST;

import com.ensd.database.ModelTypeException;
import com.ensd.database.ModelValidateException;
import com.ensd.models.UserModel;

import com.ensd.handlers.RequestHandler;

import com.ensd.http.HttpRequest;
import com.ensd.http.HttpResponse;


import com.ensd.session.Session;
import com.ensd.session.SessionManager;

import com.ensd.utils.SHA256;
import com.ensd.utils.SnowflakeID;
import org.json.JSONObject;

public class NewUser implements RequestHandler {

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        JSONObject _body = request.getBody();
        UserModel usrModel = new UserModel();

        String user_id = SnowflakeID.generateID();
        String pass = SHA256.generate(_body.get("password").toString());


        try {
            usrModel.put("username", _body.get("username"));
            usrModel.put("password", pass);
            usrModel.put("user_id", user_id);
        } catch (ModelTypeException e) {
            response.sendStatus(400);
        }

        try {
            usrModel.validate();
        } catch (ModelValidateException e) {
            response.sendStatus(400);
            return;
        } finally {
            usrModel.save();
            Session session = SessionManager.putSession(user_id, response);
            response.setCookie("_sid", session.getSessionToken(), session.getExpireInMs());
        }

        response.sendStatus(200);
    }
}
