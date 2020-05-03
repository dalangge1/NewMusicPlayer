package com.example.newmusicplayer.utils.HttpUtils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.example.newmusicplayer.Callback;
import com.example.newmusicplayer.utils.ThreadPool;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {

    private HttpUtil(){}
    private static HttpUtil httpUtil;
    public static HttpUtil getInstance(){
        if (httpUtil == null) {
            synchronized (HttpUtil.class){
                if (httpUtil == null)
                    httpUtil = new HttpUtil();
            }
        }
        return httpUtil;
    }

    public void httpGet(final String urlString, final Callback callback, final Context context, final String... requestVariableList){
        Runnable task = new Runnable(){
            @Override
            public void run() {
                try{
                    StringBuilder stringBuilder = new StringBuilder(urlString);
                    if(requestVariableList.length%2!=0){
                        Log.d("http_get","参数数量不对，应为一标签一值(偶数)");
                        return;
                    }
                    if(requestVariableList.length>0){
                        stringBuilder.append("?");
                        for (int i = 0; i < requestVariableList.length; i += 2){
                            stringBuilder.append(requestVariableList[i]).append("=").append(requestVariableList[i+1]);
                            if(i != requestVariableList.length - 2){
                                stringBuilder.append("&");
                            }
                        }
                    }
                    Log.d("http_get","已创建url："+stringBuilder.toString());
                    URL url = new URL(stringBuilder.toString());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(4000);
                    connection.setConnectTimeout(4000);
                    InputStream in=connection.getInputStream();
                    BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                    StringBuilder response=new StringBuilder();
                    String line;
                    int responseCode = connection.getResponseCode();
                    if(responseCode == HttpURLConnection.HTTP_OK){
                        while((line=reader.readLine())!=null){
                            response.append(line);
                        }
                        callback.callback(response.toString());
                        Log.d("http_get","已执行callback，result:"+response);

                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("http_get","错误！");
                }
            }
        };
        ThreadPool.getInstance().execute(task);
    }

    public void httpPost(final String urlString, final Callback callback, final Context context, final String... requestVariableList){
        Runnable task = new Runnable(){
            @Override
            public void run() {
                try{
                    URL url = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");

                    StringBuilder stringBuilder = new StringBuilder();
                    if(requestVariableList.length%2!=0){
                        Log.d("http_post","参数数量不对，应为一标签一值(偶数)");
                        return;
                    }
                    if(requestVariableList.length>0){
                        for (int i = 0; i < requestVariableList.length; i += 2){
                            stringBuilder.append(requestVariableList[i]).append("=").append(requestVariableList[i+1]);
                            if(i != requestVariableList.length - 2){
                                stringBuilder.append("&");
                            }
                        }
                        Log.d("http_post","已创建参数列表："+stringBuilder.toString());
                        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                        out.writeBytes(stringBuilder.toString());
                    }


                    connection.setReadTimeout(4000);
                    connection.setConnectTimeout(4000);
                    InputStream in=connection.getInputStream();
                    BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                    StringBuilder response=new StringBuilder();
                    String line;
                    int responseCode = connection.getResponseCode();
                    if(responseCode == HttpURLConnection.HTTP_OK){
                        while((line=reader.readLine())!=null){
                            response.append(line);
                        }
                        callback.callback(response.toString());
                        Log.d("http_post","已执行callback，result:"+response);

                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("http_post","错误！");
                }
            }
        };
        ThreadPool.getInstance().execute(task);
    }


}
