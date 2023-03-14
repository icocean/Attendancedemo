package com.example.attendancedemo.http;

import com.example.attendancedemo.model.RequestId;
import com.example.attendancedemo.model.RequestIfAut;
import com.example.attendancedemo.model.RequestIfSId;
import com.example.attendancedemo.model.RequestQiandao;
import com.example.attendancedemo.model.RequestSchool;
import com.example.attendancedemo.model.RequestqiandaoList;
import com.example.attendancedemo.model.classes;
import com.example.attendancedemo.model.imeis;

import com.example.attendancedemo.model.school;



import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;


public interface SchoolClassRequest {
    //1 查找校区
    @GET("findAllSchool")
    Observable<RequestSchool<school>> requestSchoolList();
    //2 查找校区内存在班级
    @POST("findAllSchool?")
    @FormUrlEncoded
    Observable<classes<classes.ClasListBean>> requestClassList2(
            @Field("schId") int schoolId);
    //3.首页判断是否已经认证  bind?imei=123456
    @POST("bind?")
    @FormUrlEncoded
    Observable<RequestIfAut> requestIfAut(
            @Field("imei") String imei);
    //4、判断是否同一个设备（IMEI），认证之后签到之前调用
    @POST("getIMEI?")
    @FormUrlEncoded
    Observable<RequestIfSId> requestIfSId(
            @Field("imei") String imei);
    //5认证页面提交认证
    @POST("student?opt=1&")
    @FormUrlEncoded
    Observable<RequestId> requestIdList(
            @Field("cardNum") String cardNum,@Field("imei")String imei);
    //6、签到接口（距离限制）
    //    先判断是否实名认证，然后在判断是否在200米之内，满足条件后在可以签到，调用签到接口
   /* @POST("signIn?")
    @FormUrlEncoded
    Observable<RequestQiandao> requestiQiandao(
            @Field("imei") String imei);*/

    @GET("signIn?")
    Observable<RequestQiandao> reqiandao(@Query("imei") String imei);
   //7 签到记录查询
   @POST("signIn?")
   @FormUrlEncoded
   Observable<RequestqiandaoList<RequestqiandaoList.SignListBean>> requestqiandaoLists(
           @Field("opt") String opt,@Field("imei")String imei);

}