package com.siso.app.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.siso.app.entity.GoodsCommentEntity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * 
 * description : 网络工具类
 *
 * @version 1.0
 * @author Owater
 * @createtime : 2015-3-7 下午4:35:26
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-3-7 下午4:35:26
 *
 */
public class HttpUtils {
	
	private final String TAG = "uploadFile";
	private final static int TIMEOUT_CONNECTION = 10 * 1000; // 超时时间
	private final static String CHARSET = "utf-8"; // 设置编码
	public final static String SUCCESS = "1";
	public final static String FAILURE = "0";

	/**
	* 判断网络是否连接
	* @param context
	* @return
	*/
	public static boolean isNetworkConnected(Context context){
		if(context!=null){
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if(mNetworkInfo!=null){
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}
	
	/**
	* 
	* @param url
	* @return string
	*/
	public static String request(List<NameValuePair> params,String url) {
		String result = "";
		try {
			HttpPost httpRequest = new HttpPost(url);
			if (params!=null) {
				HttpEntity httpEntity = new UrlEncodedFormEntity(params,HTTP.UTF_8);
				httpRequest.setEntity(httpEntity);
			}
			HttpClient httpClient = new DefaultHttpClient();
//			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(TIMEOUT_CONNECTION);
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(httpResponse.getEntity());
				Log.i("commit", "提交成功");
			} else {
				result = "0x110";//请求失败
				Log.e("commit", "提交失败");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		Log.i("result", "result==="+result.trim());
		return result;
	}
	/**
	 * 
	 * @author zhoufeng
	 * @createtime 2015-3-23 下午9:54:06
	 * @Decription 访问服务携带参数数据的对象
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public static NameValuePair getNameValuePair(String key,String value){
		return new BasicNameValuePair(key, value);
	}
	
	public static String requestGson(String url,Object object) {
        String result = "";
        try {
            HttpPost httpRequest = new HttpPost(url);
            if (object!=null) {
                Gson gson = new Gson();
                StringEntity stringEntity = new StringEntity(gson.toJson(object),HTTP.UTF_8);
                stringEntity.setContentType("application/json");
                httpRequest.setEntity(stringEntity);
            }
            HttpClient httpClient = new DefaultHttpClient();
//			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(TIMEOUT_CONNECTION);
            HttpResponse httpResponse = httpClient.execute(httpRequest);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(httpResponse.getEntity());
                Log.i("commit", "提交成功");
            } else {
                result = "0x110";//请求失败
                Log.i("commit", "提交失败");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//		Log.i("result", "result==="+result.trim());
        return result;
    }
}
