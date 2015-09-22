package com.siso.app.common.network;

/**
 * Created by chaochen on 14-10-6.
 */
public interface NetworkCallback {
    void parseJson(int code, String msg, String tag, Object data);

    void getNetwork(String uri, String tag);
}
