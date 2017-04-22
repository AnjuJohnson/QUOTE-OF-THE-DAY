package com.cutesys.bibleapp.Helperclasses;

import android.content.Context;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HttpOperations {

    public final String SERVER_URL = "http://cutedemo.in/QuotesApp/index.php/";

    public Context ctx;

    public HttpOperations(Context ctx) {
        this.ctx = ctx;
    }

    enum APIS {

        API_THOUGHT("QuoteApi/quotelisttoday", "POST"),
        API_NOTIFICATIONLIST("NotificationApi/notificationlist", "POST"),
        API_REVIEW_LIST("QuoteApi/reviewlist", "POST"),
        API_ADDREVIEW("QuoteApi/reviewadd", "POST"),
        API_ADD_USER("QuoteApi/useradd", "POST");

        private final String api_name;
        private final String method;

        private APIS(final String text, String method) {
            this.api_name = text;
            this.method = method;
        }

        @Override
        public String toString() {
            return api_name;
        }
    }

    // Thought
    public StringBuilder doNewThought(final String date) {
        HashMap<String, String> params = new HashMap<String, String>();

        return sendRequest(params, APIS.API_THOUGHT, "?start="+0+"&limit="+
                500+"&day="+date);
    }

    // notification list
    public StringBuilder doNotifications() {
        HashMap<String, String> params = new HashMap<String, String>();
        return sendRequest(params, APIS.API_NOTIFICATIONLIST, "?start="+0+"&limit="+
                500);
    }

    // add user
    public StringBuilder doAddUser(final String mUser) {
        HashMap<String, String> params = new HashMap<String, String>();
        return sendRequest(params, APIS.API_ADD_USER, "?&user="+mUser);
    }

    // Review list
    public StringBuilder doReview_List(final String userid) {
        HashMap<String, String> params = new HashMap<String, String>();
        return sendRequest(params, APIS.API_REVIEW_LIST, "?start="+0+"&limit="+
                500+"&user_id="+userid);
    }

    // add review
    public StringBuilder doAddReview(final String quotes_id,final String review,
                                     final String user) {
        HashMap<String, String> params = new HashMap<String, String>();
        return sendRequest(params, APIS.API_ADDREVIEW, "?quotes_id="+quotes_id+"&review="+
                review+"&user_id="+user);
    }

    public StringBuilder sendRequest(HashMap<String, String> params, APIS api, String URL) {

        final HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 20000);
        HttpClient httpclient = new DefaultHttpClient(httpParams);

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            for (String key : params.keySet()) {
                nameValuePairs.add(new BasicNameValuePair(key, params.get(key)));
            }
            if (api.method.equals("POST")) {

                try {

                    HttpPost httppost = new HttpPost(SERVER_URL + api.toString()+URL
                            .replace(" ", "%20"));
                    httppost.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);
                    // httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    Log.d("111111111",SERVER_URL+api.toString()+URL);
                    HttpResponse response = httpclient.execute(httppost);
                    return readStream(response.getEntity().getContent());
                } catch (HttpHostConnectException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public StringBuilder readStream(InputStream in) throws Exception {
        StringBuilder stringBuilder = null;

        if (in == null) {
            return null;
        }

        BufferedReader inReader = new BufferedReader(new InputStreamReader(in));
        stringBuilder = new StringBuilder();
        String line = null;

        while ((line = inReader.readLine()) != null) {
            stringBuilder.append(line + "\n");
        }
        in.close();
        return stringBuilder;
    }
}