package org.michenux.drodrolib.network.volley;

import android.location.Location;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.michenux.drodrolib.network.json.LocationDeserializer;
import org.michenux.drodrolib.network.json.TimestampDeserializer;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Map;

public class GsonRequest<T> extends Request<T> {
	
	private final Gson gson ;
    private final Class<T> clazz;
    private final Map<String, String> headers;
    private final Listener<T> listener;
    
    public GsonRequest(int method, String url, Class<T> clazz, Map<String, String> headers,
            Listener<T> listener, ErrorListener errorListener) {
        super(method, url, errorListener);
        
        GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Timestamp.class, new TimestampDeserializer());
        gsonBuilder.registerTypeAdapter(Location.class, new LocationDeserializer());
		this.gson = gsonBuilder.create();
        this.clazz = clazz;
        this.headers = headers;
        this.listener = listener;
    }
 
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }
 
    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }
 
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(
                    gson.fromJson(json, clazz), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException | JsonSyntaxException  e) {
            return Response.error(new ParseError(e));
        }
    }
}
