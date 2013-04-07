package com.nullpandaexception.locality;

import org.json.json.JSONObject;

public interface APIHandler {
    public String getResponse();
    public JSONObject getJSONResponse();
    public void call(String call);
}
