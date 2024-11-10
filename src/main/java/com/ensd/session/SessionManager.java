package com.ensd.session;

import com.ensd.database.ModelTypeException;
import com.ensd.http.HttpResponse;
import com.ensd.models.SessionModel;
import com.ensd.utils.JWTUtil;
import com.ensd.utils.SnowflakeID;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SessionManager {
    private static final Map<String, Session> _sessions = new HashMap<>();
    private static final long defaultExpireInMs = 1000L * 60 * 60 * 24 * 7 * 52;

    public static Session putSession(String userID, HttpResponse response) {
        Session session = generateSession(userID);

        SessionModel sessionModel = new SessionModel();

        try {
            sessionModel.put("_sid", session.getSessionID());
            sessionModel.put("user_id", userID);
        } catch (ModelTypeException e) {
            response.sendStatus(500);
        }
        _sessions.put(session.getSessionID(), session);
        sessionModel.save();
        return session;
    }

    public static Session getSession(String _sid) {
        return _sessions.get(_sid);
    }

    private static Session generateSession(String userID) {
        String sessionID = SnowflakeID.generateID();
        Date expireDate = new Date(new Date().getTime() + defaultExpireInMs);
        Session session = new Session(sessionID, userID, true, expireDate, defaultExpireInMs);
        String sid = generateSessionToken(session);
        session.setSessionToken(sid);

        return session;
    }

    private static String generateSessionToken(Session _session) {
        try {
            return JWTUtil.createJWT(_session.getSessionAsJSONObject().toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
