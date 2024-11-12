package com.ensd.routes.POST;

import com.ensd.handlers.RequestHandler;
import com.ensd.http.HttpRequest;
import com.ensd.http.HttpResponse;
import com.ensd.models.UserModel;
import com.ensd.session.Session;
import com.ensd.session.SessionManager;
import com.ensd.utils.SHA256;
import org.json.JSONObject;


public class Login implements RequestHandler {

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public Boolean protectedPath() {
        return false;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        JSONObject _body = request.getBody();
        UserModel userDB = new UserModel();
        JSONObject USER = userDB.findOne("username", (String) _body.get("username"));
        String providedPass = (String) _body.get("password");
        Boolean comparePasswords = SHA256.compare(providedPass, (String) USER.get("password"));
        Session currentSession = SessionManager.putSession((String) USER.get("user_id"), response);
        if (comparePasswords) {
            response.send(200, "{\"token\":\"" + currentSession.getSessionToken() + "\"}");
            return;
        }
        response.sendStatus(403);

    }
}
