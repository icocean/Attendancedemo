package com.example.attendancedemo.http;

import android.app.Application;
import android.util.Log;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseApplication extends Application {
    public  String TAG=BaseApplication.class.getSimpleName();
    public  static SchoolClassRequest iSchoolRequest;
    @Override
    public void onCreate() {
        super.onCreate();

            //1.初始化Retrofit对象       http://192.168.31.120:8080/SignIn/findAllSchool
            Retrofit retrofit=new Retrofit.Builder()
                    .baseUrl("http://192.168.31.120:8080/SignIn/")
                    //添加GSON解析器插件
                    .addConverterFactory(GsonConverterFactory.create())
                    //rxjava
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();

            //初始化请求接口
            iSchoolRequest=retrofit.create(SchoolClassRequest.class);
        }
}
