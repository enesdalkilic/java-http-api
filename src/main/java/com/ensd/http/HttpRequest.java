package com.ensd.http;

import java.util.Map;

public class HttpRequest {
    private String method;
    private String requestTarget;
    private String path;
    private String httpVersion;
    private Map<String, String> headers;
    private String name;

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


}
