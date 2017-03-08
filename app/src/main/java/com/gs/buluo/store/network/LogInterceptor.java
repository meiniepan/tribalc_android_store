package com.gs.buluo.store.network;

import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by hjn on 2017/3/2.
 */
public class LogInterceptor implements okhttp3.Interceptor {
    private String TAG ="TribeNetwork";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long start =System.currentTimeMillis();
        Log.e(TAG, "request url:" + request.url() +" request body is "+(request.body()!=null? request.body().toString() :null));
        Response response = chain.proceed(request);
        long end =System.currentTimeMillis();
        String bodyString ="";
        if (response!=null&&response.body()!=null){
            bodyString= new String(response.body().bytes());
        }
        Log.e(TAG, "response from url:" + response.request().url() + " ----------in  " + (end - start) +
                "ms \n response code is  " + response.code() + "  and response body is " + bodyString);

        return response.newBuilder().body(ResponseBody.create(MediaType.parse("UTF-8"),bodyString)).build();
    }
}
