package tech.raulballeza.p_intermedio;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class CustomJsonRequest extends Request {
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String,String> headers = new HashMap<>();

        /*change content-type to "application/x-www-form-urlencoded" from
         "application/json"
         */

        headers.put("Content-Type","application/x-www-form-urlencoded");
        return super.getHeaders();
    }

    Map<String, Object> params;
    private Response.Listener listener;

    public CustomJsonRequest(int requestMethod, String url, Map<String, Object> params,
                             Response.Listener responseListener, Response.ErrorListener errorListener) {

        super(requestMethod, url, errorListener);
        this.params = params;
        this.listener = responseListener;
    }

    @Override
    protected void deliverResponse(Object response) {
        listener.onResponse(response);

    }

    @Override
    public Map<String, Object> getParams() throws AuthFailureError {
        return params;
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

}