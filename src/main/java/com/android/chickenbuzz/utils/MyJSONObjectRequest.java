package com.android.chickenbuzz.utils;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by krelluru on 8/26/2016.
 */

public class MyJSONObjectRequest extends JsonObjectRequest {

    public MyJSONObjectRequest(int method, String url, JSONObject jsonRequest,Response.Listener<JSONObject> listener,Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    /*public MyJSONObjectRequest(int method, String url, Map<String, String> params,Response.Listener<JSONObject> listener,Response.ErrorListener errorListener) {
        super(method, url, params, listener, errorListener);
    }*/
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json; charset=utf-8");
        return headers;
    }

    @Override
    public RetryPolicy getRetryPolicy() {
        // here you can write a custom retry policy
        return super.getRetryPolicy();
    }

}
