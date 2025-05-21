package com.tourismclient.cultoura.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.Nullable;

import com.tourismclient.cultoura.BuildConfig;
import com.tourismclient.cultoura.confidential.AuthTokenFetcher;
import com.tourismclient.cultoura.network.ApiUrl;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class VolleyRequest {

    private static final boolean DEBUG_APK = BuildConfig.DEBUG;
    private VolleyRequestListener listener;
    private ListPopUp listPopUp;
    private final static String TAG = "VolleyRequest.Java";


    public interface VolleyRequestListener {
        public void onDataLoaded(JSONObject jsonObject) throws JSONException;

        public void onError(VolleyError error);
    }

    public VolleyRequest() {
        listPopUp = new ListPopUp();
        this.listener = null;
    }

    // Assign the listener implementing events interface that will receive the events
    public void setVolleyRequestListener(VolleyRequestListener listener) {
        this.listener = listener;
    }

    public void makeGETRequest(String url, final Map<String, String> parameters, final Context context, boolean userToken) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        debugSuccess(url, response.toString(), (JSONObject) null);
                        if (listener != null) {
                            try {
                                listener.onDataLoaded(response);
                            } catch (JSONException ignored) {
                                ;
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        debugError(url, error.toString(), null);
                        listener.onError(error);
                        // listPopUp.showPopup(context.getResources().getString(R.string.some_error_occured)+"\n--"+error.getLocalizedMessage()+"--", context.getResources().getString(R.string.ok_action), context);
//                        Timber.e("onErrorResponse(GetRequest): %s", error.getLocalizedMessage());

                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getHeader(context,userToken);
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObjectRequest);
    }





    public void makePushNotification(String url, JSONObject params, final Context context) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("message==", response.toString());
            }
        }, error -> {
            Log.e("error==", error.toString());
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/json");
                header.put("Authorization", "key=AAAAVK45wg0:APA91bHe-Z0gXtVRwoDHsYtgzf_M59p7aqW2dO-rDyiD86uyvMc_KkFzt8CGiwBH0IJiUyvnr_4MwjVulvHca-wn89tRHRqqbVViBCAmeb4uhN6ZObLCuF-mG7AlAE9VsbVyZeuIXAJe");
                return header;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    public void makePOSTRequestWithoutHeader(String url, JSONObject params, final Context context,boolean userToken) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        debugSuccess(url, response.toString(), params);
                        if (listener != null) {
                            try {
                                listener.onDataLoaded(response);
                            } catch (JSONException e) {
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (params != null) {
                    debugError(url, error.toString(), params.toString());
                } else {
                    debugError(url, error.toString(), null);
                }
                if (error.networkResponse != null && error.networkResponse.statusCode == 401) {
                    Utils.getInstance().logout(context);
                    Utils.triggerRebirth(context);
                }
                if (listener != null)
                    listener.onError(error);

                // listPopUp.showPopup(context.getResources().getString(R.string.some_error_occured)+"Error : "+error.getLocalizedMessage(), context.getResources().getString(R.string.ok_action), context);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getHeader(context,userToken);
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObjectRequest);
    }
    public void makePOSTRequest(String url, JSONObject params, final Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        debugSuccess(url, response.toString(), params);
                        if (listener != null) {
                            try {
                                listener.onDataLoaded(response);
                            } catch (JSONException e) {
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (params != null) {
                    debugError(url, error.toString(), params.toString());
                } else {
                    debugError(url, error.toString(), null);
                }
                if (error.networkResponse != null && error.networkResponse.statusCode == 401) {
                    Utils.getInstance().logout(context);
                    Utils.triggerRebirth(context);
                }
                if (listener != null)
                    listener.onError(error);

                // listPopUp.showPopup(context.getResources().getString(R.string.some_error_occured)+"Error : "+error.getLocalizedMessage(), context.getResources().getString(R.string.ok_action), context);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getHeader(context);
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObjectRequest);
    }
    public void makePOSTRequest(String url, JSONObject params, final Context context, boolean userToken) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        debugSuccess(url, response.toString(), params);
                        if (listener != null) {
                            try {
                                listener.onDataLoaded(response);
                            } catch (JSONException e) {
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (params != null) {
                    debugError(url, error.toString(), params.toString());
                } else {
                    debugError(url, error.toString(), null);
                }
                if (error.networkResponse != null && error.networkResponse.statusCode == 401) {
                    Utils.getInstance().logout(context);
                    Utils.triggerRebirth(context);
                }
                if (listener != null)
                    listener.onError(error);

                // listPopUp.showPopup(context.getResources().getString(R.string.some_error_occured)+"Error : "+error.getLocalizedMessage(), context.getResources().getString(R.string.ok_action), context);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getHeader(context,userToken);
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObjectRequest);
    }


    public void makePOSTRequest(String url, HashMap<String, String> params, final Context context, boolean userToken) {
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                debugSuccess(url, response, params);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (listener != null) {
                        try {
                            listener.onDataLoaded(jsonObject);
                        } catch (JSONException e) {
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (params != null) {
                    debugError(url, error.toString(), params.toString());
                } else {
                    debugError(url, error.toString(), null);
                }
                if (error.networkResponse != null && error.networkResponse.statusCode == 401) {
                    Utils.getInstance().logout(context);
                    Utils.triggerRebirth(context);
                }
                if (listener != null)
                    listener.onError(error);

                // listPopUp.showPopup(context.getResources().getString(R.string.some_error_occured)+"Error : "+error.getLocalizedMessage(), context.getResources().getString(R.string.ok_action), context);
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getHeader(context,userToken);

            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);
    }





    public void makeMultipartRequest(String url, final Map<String, String> parameters, final Map<String, Bitmap> dataParams, final Context context) {

        RequestQueue queue = Volley.newRequestQueue(context);

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    JSONObject obj = new JSONObject(new String(response.data));
                    debugSuccess(url, "" + obj, parameters);
                    if (listener != null)
                        listener.onDataLoaded(obj);
                } catch (JSONException ignored) {
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        debugError(url, error.toString(), null);
                        if (listener != null) {
                            listener.onError(error);
                        } else {
                            //listPopUp.showPopup(context.getResources().getString(R.string.some_error_occured)+"\nError : "+error.getLocalizedMessage(), context.getResources().getString(R.string.ok_action), context);
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = SharedPreferences.getVariablesInPreferences(Constants.TOKEN, context);
                if (token.equals(""))
                    return super.getHeaders();
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Cache-Control", "no-cache");
                headers.put("Pragma", "no-cache");
                headers.put("Authorization", "Token " + token);
                headers.put("User-Agent" , "3WjE7G6jvbjYtMJDs7wHQUxJAg1kHqlvd2M680War84cwrULNC");
                return headers;

            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return parameters;
            }

            @Override
            protected Map<String, DataPart> getByteData() {

                Map<String, VolleyMultipartRequest.DataPart> params = new HashMap<>();
                Iterator it = dataParams.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    long imagename = System.currentTimeMillis();
                    String key = pair.getKey().toString();
                    params.put(key, new VolleyMultipartRequest.DataPart(imagename + ".png", getFileDataFromDrawable(dataParams.get(key))));
                    it.remove();
                }

                return params;
            }
        };

        queue.add(volleyMultipartRequest);
    }

    public void pushNotificationWithoutBody(String sendNotificationTo, String message, Context context, VolleyRequestListener mVolleyRequestListener,boolean userToken) {

        JSONObject notificationData = new JSONObject();
        try {
            notificationData.put("notify_to", sendNotificationTo);
            notificationData.put("notification_msg", message);
            notificationData.put("notification_type", "others");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ApiUrl.SEND_NOTIFICATION, notificationData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (mVolleyRequestListener != null) {
                            try {
                                mVolleyRequestListener.onDataLoaded(response);
                            } catch (JSONException e) {
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (mVolleyRequestListener != null)
                    mVolleyRequestListener.onError(error);

                // listPopUp.showPopup(context.getResources().getString(R.string.some_error_occured)+"Error : "+error.getLocalizedMessage(), context.getResources().getString(R.string.ok_action), context);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                return getHeader(context,userToken);

            }
        };

//        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObjectRequest);

    }


    public void getRequestWithOutHeader(String url, final Map<String, String> parameters, final Context context, boolean userToken) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        debugSuccess(url, response.toString(), (Map<String, String>) null);
                        if (listener != null) {
                            try {
                                listener.onDataLoaded(response);
                            } catch (JSONException e) {

                            }
                        }
                    }
                }, (Response.ErrorListener) error -> {
                    debugError(url, error.toString(), null);

                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getHeader(context,userToken);

            }
        };

        queue.add(jsonObjectRequest);
    }

    private Map<String, String> getHeader(Context context, boolean userToken) {
        String token = userToken ? SharedPreferences.getVariablesInPreferences(Constants.USER_TOKEN, context) : SharedPreferences.getVariablesInPreferences(Constants.TOKEN, context);
        Map<String, String> headers = new HashMap<>();
        if (!token.isEmpty()){
            headers.put("Authorization", "Bearer " + token);
        }
        return headers;
    }
    private Map<String, String> getHeader(Context context) {
        String token = new AuthTokenFetcher().getToken();
        Map<String, String> headers = new HashMap<>();
        if (!token.isEmpty()){
            headers.put("Authorization", "Basic " + token);
        }
        return headers;
    }




    private void debugSuccess(String url, String response, Map<String, String> body) {
        if (DEBUG_APK) {
            if (body != null) {
                Log.e("hello body <----->", body.toString());
            } else {
                Log.e("hello body <----->", "empty body");
            }
            Log.e("hello url <----->", url);

            Log.e("hello response", response);
        }
    }

    private void debugSuccess(String url, String response, JSONObject body) {
            if (body != null) {
                Log.e("hello body <----->", body.toString());
            } else {
                Log.e("hello body <----->", "empty body");
            }
            Log.e("hello url <----->", url);
            Log.e("hello response", response);


    }

    private void debugError(String url, String error, String body) {
            if (body != null) {
                Log.e("hello onError body", " <----->" + body);
            } else {
                Log.e("hello onError body", " <-----> empty body");
            }
            Log.e("hello onError <----->", url + "<----->" + error);
    }


    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

}
