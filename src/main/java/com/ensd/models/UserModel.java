package com.ensd.models;

import com.ensd.database.FieldType;
import com.ensd.database.Model;

public class UserModel extends Model {

    //  TODO: get directly body and turn into document with validation in once

    public UserModel() {
        set_collection("users");
        registerField("user_id", FieldType.STRING);
        registerField("username", FieldType.STRING);
        registerField("password", FieldType.STRING);
        registerField("age", FieldType.INTEGER);
    }
}
