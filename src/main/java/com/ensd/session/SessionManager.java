package com.ensd.session;

import com.ensd.HttpServer;
import com.ensd.database.ModelTypeException;
import com.ensd.http.HttpResponse;
import com.ensd.models.SessionModel;
import com.ensd.utils.JWTUtil;
import com.ensd.utils.SnowflakeID;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SessionManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionManager.class);

    private static final Map<String, Session> _sessions = new HashMap<>();
    private static final long defaultExpireInMs = 1000L * 60 * 60 * 24 * 7 * 52;

    public static void fetchSessions() {
        LOGGER.info("All Session Fetching from the database...");
        SessionModel sessionModel = new SessionModel();
        List<JSONObject> DBsessions = sessionModel.findAll();
        for (JSONObject _session : DBsessions) {
            Session session = new Session((String) _session.get("_sid"), (String) _session.get("user_id"), true, new Date(Long.parseLong((String) _session.get("expire_date"))), Long.parseLong((String) _session.get("expire_in_ms")));
            _sessions.put(session.getSessionID(), session);
        }
        LOGGER.info("Fetch mission is DONE. '{}' session(s)", _sessions.size());
    }

    public static Session putSession(String userID, HttpResponse response) {
        Session session = generateSession(userID);

        SessionModel sessionModel = new SessionModel();

        try {
            sessionModel.put("_sid", session.getSessionID());
            sessionModel.put("user_id", userID);
            sessionModel.put("expire_date", userID);
            sessionModel.put("expire_in_ms", userID);
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
            //It should never fail
            throw new RuntimeException(e);
        }
    }
}
