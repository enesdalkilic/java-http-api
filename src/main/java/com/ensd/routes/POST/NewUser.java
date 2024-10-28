package com.ensd.routes.POST;

import com.ensd.database.Collection;
import com.ensd.database.Database;
import com.ensd.handlers.RequestHandler;
import com.ensd.helpers.DbSchemaHelper;
import com.ensd.http.HttpRequest;
import com.ensd.http.HttpResponse;
import com.ensd.schemaEnums.User;

import org.bson.Document;

public class NewUser implements RequestHandler {

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        Collection collection = new Collection(Database.getCollection("movies"));
        Document _doc = new Document();
        _doc.append("email", "enes");
        _doc.append("username", "enes");
        _doc.append("phoneNumber", "enes");
        _doc.append("password", "enes");

        boolean isValidated = DbSchemaHelper.validateSchema(User.values(), _doc.keySet(), false);

        if (!isValidated) {
            response.sendStatus(400);
            return;
        }

        response.sendStatus(200);
    }
}
