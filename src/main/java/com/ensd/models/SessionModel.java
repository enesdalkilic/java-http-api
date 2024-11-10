package com.ensd.models;

import com.ensd.database.FieldType;
import com.ensd.database.Model;

public class SessionModel extends Model {
    public SessionModel() {
        set_collection("sessions");
        registerField("_sid", FieldType.STRING);
        registerField("user_id", FieldType.STRING);
    }
}
