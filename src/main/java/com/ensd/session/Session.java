package com.ensd.session;

import com.mongodb.util.JSON;
import org.json.JSONObject;

import java.util.Date;

public class Session {

    private final String sessionID;
    private String sessionToken;
    private final String userID;
    private final boolean isValid;
    private final Date expireDate;
    private final long expireInMs;

    // Constructor to initialize the session
    public Session(String sessionID, String userID, Boolean isValid, Date expireDate, Long expireInMs) {
        this.sessionID = sessionID;
        this.userID = userID;
        this.isValid = isValid;
        this.expireDate = expireDate;
        this.expireInMs = expireInMs;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    // Getter methods
    public String getSessionID() {
        return sessionID;
    }

    public String getUserID() {
        return userID;
    }

    public Boolean getIsValid() {
        return isValid;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public long getExpireInMs() {
        return expireInMs;
    }

    // Converts the session data to a JSONObject
    public JSONObject getSessionAsJSONObject() {
        JSONObject session = new JSONObject();
        session.put("session_id", sessionID);
        session.put("user_id", userID);
        session.put("expire_date", expireDate);
        return session;
    }

}
