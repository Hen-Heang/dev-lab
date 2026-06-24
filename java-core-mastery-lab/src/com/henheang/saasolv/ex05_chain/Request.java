package com.henheang.saasolv.ex05_chain;

import java.util.HashMap;
import java.util.Map;

/** A tiny stand-in for HttpServletRequest. */
public class Request {

    private final String path;
    private boolean loggedIn;
    private final Map<String, String> params = new HashMap<>();

    public Request(String path, boolean loggedIn) {
        this.path = path;
        this.loggedIn = loggedIn;
    }

    public String getPath() { return path; }
    public boolean isLoggedIn() { return loggedIn; }

    public void setParam(String k, String v) { params.put(k, v); }
    public String getParam(String k) { return params.get(k); }
    public Map<String, String> getParams() { return params; }
}
