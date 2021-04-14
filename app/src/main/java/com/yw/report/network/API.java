package com.yw.report.network;

import android.util.Log;

import com.yw.report.activity.Data;

import java.io.File;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class API {
   public static OkHttpClient client = new OkHttpClient();

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static Call uploadfile(String url,String path) {
        File file = new File(path);
        Log.d("APILOG","文件路径"+file.getPath());
        if (!file.exists()) {
            Log.d("APILOG","文件不存在");
            return null;
        } else {
            RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                            //这里的name是后台接受的键    第二个参数是后台接收时文件的名字   第三个为文件
            RequestBody requestBody =
                    new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("fileName", file.getName(), fileBody).build();

            Request requestPostFile = new Request.Builder()
                    .url(url)
                    .addHeader("token", Data.loginUser.getToken())
                    .addHeader("Content-Type","multipart/form-data")
                    .post(requestBody)
                    .build();
            return client.newCall(requestPostFile);
        }
    }
    public static Call get(String url){
        Request request = new Request.Builder()
                .addHeader("Content-Type","application/json;charset=UTF-8;")
                .get()
                .url(url)
                .build();
       return client.newCall(request);

    }
    public static Call  post(String url,String json){
        RequestBody body = RequestBody.create(JSON, json);
        Request requestPost = new Request.Builder()
                .url(url)
                .post(body)
                .build();
       return client.newCall(requestPost);

    }

    public static Call  postWithToken(String url,String json){
        RequestBody body = RequestBody.create(JSON, json);
        Request requestPost = new Request.Builder()
                .url(url)
                .addHeader("token", Data.loginUser.getToken())
                .post(body)
                .build();
        return client.newCall(requestPost);

    }
}
