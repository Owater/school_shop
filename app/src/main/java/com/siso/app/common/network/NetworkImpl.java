package com.siso.app.common.network;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.siso.app.entity.DataJson;

import org.apache.http.Header;

/**
 * Created by Owater on 2015/6/1.
 */
public class NetworkImpl {

    Context appContext;
    private NetworkCallback networkCallback;

    public enum Request {
        Get, Post, Put, Delete
    }

    public NetworkImpl(Context ctx,NetworkCallback networkCallback) {
        this.appContext = ctx;
        this.networkCallback = networkCallback;
    }

    public void loadData(String url, RequestParams params, final String tag, Request type){
        AsyncHttpClient client = MyAsyncHttpClient.createClient(appContext);

        AsyncHttpResponseHandler asyncHttpResponseHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("tag", "responseBody==" + new String(responseBody));
                Gson gson = new Gson();
                DataJson dataJson = gson.fromJson(new String(responseBody), DataJson.class);
                Object data = dataJson.getData();
                networkCallback.parseJson(dataJson.getCode(), dataJson.getMsg(), tag, data);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("tag","onFailure responseBody=="+new String(responseBody));
            }
        };

        switch (type) {
            case Get:
                client.get(url, asyncHttpResponseHandler);
                break;

            case Post:
                client.post(url, params, asyncHttpResponseHandler);
                break;

            case Put:
                client.put(url, params, asyncHttpResponseHandler);
                break;

            case Delete:
                client.delete(url, asyncHttpResponseHandler);
                break;
        }
    }

}
