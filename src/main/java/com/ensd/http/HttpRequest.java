package com.ensd.http;

import com.ensd.session.Session;
import com.ensd.session.SessionManager;
import com.ensd.utils.JWTUtil;
import org.json.JSONObject;

import java.util.Map;

public class HttpRequest {
    private String method;
    private String path;
    private String httpVersion;
    private Map<String, String> headers;
    private String originalHttpRequest;
    private String body;

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    private Session session = null;

    public JSONObject getBody() {
        return new JSONObject(body);
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getOriginalHttpRequest() {
        return originalHttpRequest;
    }

    public void setOriginalHttpRequest(String originalHttpRequest) {
        this.originalHttpRequest = originalHttpRequest;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public boolean verifyAndSetSession() {
        String token = getHeader("Authorization");
        String sid = null;

        try {
            JSONObject decoded = JWTUtil.decode(token);
            assert decoded != null;
            if (decoded.get("session_id") != null) {
                sid = (String) decoded.get("session_id");
            }
        } catch (Exception e) {
            return false;
        }

        Session session = SessionManager.getSession(sid);

        if (session == null) {
            return false;
        } else {
            setSession(session);
        }

        return session.getIsValid();
    }

}
