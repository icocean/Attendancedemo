package com.example.attendancedemo;

import androidx.annotation.LongDef;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.attendancedemo.http.BaseApplication;
import com.example.attendancedemo.model.RequestIfAut;
import com.example.attendancedemo.model.RequestQiandao;
import com.example.attendancedemo.model.imeis;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import rx.Observer;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private AMapLocationClient MapsInitializer;
    int QiandaoId,ifAutId;
    double weidu,jingdu;
    String shenId;
    double qiandaoweidu=34.235197;
    double qiandaojingdu=108.89318;
    Calendar calendar = Calendar.getInstance();
    //获取系统的日期
    //年
    int year = calendar.get(Calendar.YEAR);
    //月
    int month = calendar.get(Calendar.MONTH)+1;
    //日
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    //获取系统时间
    //小时
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    //分钟
    int minute = calendar.get(Calendar.MINUTE);
    //秒
    int second = calendar.get(Calendar.SECOND);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //定位告知
        MapsInitializer.updatePrivacyShow(this,true,true);
        MapsInitializer.updatePrivacyAgree(this,true);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String []{android.Manifest.permission.ACCESS_COARSE_LOCATION},1);
        }
       startLocation();
        initView();

    }




    //声明mlocationClient对象
    private AMapLocationClient aMapLocationClient;
    private AMapLocationListener aMapLocationListener=new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //定位成功回调信息，设置相关消息
                    String citylog=aMapLocation.getCity();
                    aMapLocation.getLatitude();//获取纬度
                    weidu=aMapLocation.getLatitude();
                    aMapLocation.getLongitude();//获取经度
                    jingdu=aMapLocation.getLongitude();

                    aMapLocation.getAccuracy();//获取精度信息
                    aMapLocation.getStreet();
                    Log.d(TAG, "城市定位: " + citylog.substring(0,2)+aMapLocation.getStreet());

                    /*Log.d(TAG, "城市经度: " +aMapLocation.getLongitude() );
                    //108.893134
                    Log.d(TAG, "城市纬度: " +aMapLocation.getLatitude() );
                    //34.235148
                    Log.d(TAG, "城市精度: " +aMapLocation.getAccuracy() );
                    Log.d(TAG, "距离: "+getdistance
                            (108.893134,34.235148,108.907953 ,34.245294));*/
                    //load(citylog.substring(0,2));34.245294,108.907953
                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }
    };
        @Override
        protected void onDestroy() {
            stopLocation();
            super.onDestroy();
        }


    private void initView() {
         TextView search_tv=findViewById(R.id.search_tv);
         ImageView qiandao_tv=findViewById(R.id.qiandao_tv);
        if (getSharedPreferences("SP", Context.MODE_PRIVATE)!=null){
            //  取值默认为shengId 没有返回 0
            shenId=getSharedPreferences("SP", Context.MODE_PRIVATE).getString("shengId","1");
        }else {
            shenId="1";
        }

        Log.d(TAG, "获取当前日期时间"+hour+":"+minute+":"+second);



        qiandao_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ifAut(shenId);
            }
        });
        search_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, ListActivity.class);
                startActivity(intent);
            }
        });

    }
    //签到方法  0 尚未实名认证 ，无法签到    1 签到成功    -1 学员不存在   -2 签到失败
    private void Qiandao(String imei){
                BaseApplication.iSchoolRequest.reqiandao(imei)
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Observer<RequestQiandao>() {
                            @Override
                            public void onCompleted() {
                                Log.d(TAG, "签到请求成功");
                            }
                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, e.toString());
                                Log.d(TAG, "签到请求失败");
                            }
                            @Override
                            public void onNext(RequestQiandao requestQiandao) {
                                String QiandaoId=requestQiandao.getRes();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                switch (QiandaoId){
                                    case "1":
                                        Toast.makeText(MainActivity.this, "签到成功", Toast.LENGTH_SHORT).show();
                                        //Log.d(TAG, "签到成功---------");
                                        break;
                                    case "0":
                                        Toast.makeText(MainActivity.this, "尚未实名认证", Toast.LENGTH_SHORT).show();
                                        //Log.d(TAG, " 尚未实名认证 ---------");
                                        break;
                                    case "-1":
                                        Toast.makeText(MainActivity.this, "学员不存在", Toast.LENGTH_SHORT).show();
                                        //Log.d(TAG, "学员不存在---------");
                                        break;
                                    case "-2":
                                        Toast.makeText(MainActivity.this, "签到失败", Toast.LENGTH_SHORT).show();
                                        //Log.d(TAG, "签到失败---------");
                                        break;
                                }
                                Log.d(TAG, "签到请求数据"+QiandaoId);
                            }
                        });
            }
        });
    }
    //第一次进行判断是否认证 认证直接签到 未认证去认证
    private void ifAut(String imei){
        BaseApplication.iSchoolRequest.requestIfAut(imei)
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<RequestIfAut>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "请求判断认证跳转成功");
                    }
                    @Override
                    public void onError(Throwable e) {

                        Log.d(TAG, "请求判断认证跳转失败"+e.toString());
                    }
                    @Override
                    public void onNext(RequestIfAut requestIfAut) {
                        ifAutId=requestIfAut.getRes();
                        if (ifAutId==0){
                            Intent intent=new Intent(MainActivity.this,RealNameActivity.class);
                            startActivity(intent);
                        }else {
                            Log.d(TAG, "-------签到身份证号码"+shenId);
                            //加入定位逻辑

                            Log.d(TAG, "经度信息:"+jingdu+"纬度"+weidu);
                            double sst2=getDistance(qiandaojingdu,qiandaoweidu,jingdu,weidu);
                            Log.d(TAG, "距离"+sst2);//33.868775 109.907074
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (sst2>300){
                                        Toast.makeText(MainActivity.this, "请到签到地点进行签到", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Qiandao(shenId);
                                    }

                                }
                            });

                        }
                        Log.d(TAG, "请求判断认证跳转数据"+ifAutId);
                    }
                });
    }


            //开始定位
            public void startLocation(){
        //        设置客户端
                try {
                    aMapLocationClient=new AMapLocationClient(getApplicationContext());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                aMapLocationClient.setLocationListener(aMapLocationListener);
                //设置高德定位客户端的选项
                AMapLocationClientOption Option=new AMapLocationClientOption();
                //设置高精度定位
                Option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                //设置获取地址信息
                Option.setNeedAddress(true);

                //Option.setOnceLocation(true);
        //        设定定位周期为10s
                Option.setInterval(1000*1000);
                //设置可以使用模拟定位
                //aMapLocationClient.setLocationOption(Option);
                //先停止在开始
                aMapLocationClient.stopLocation();
                aMapLocationClient.startLocation();
            }
            //停止定位
            public void stopLocation(){
                aMapLocationClient.stopLocation();
                aMapLocationClient.onDestroy();
            }
            /*
                计算距离方法
                签到地点 longitude1经度 lng1纬度
                定位地点 longitude2 经度 lng2纬度
            * */
    /*public double getdistance(double longitude1, double lng1,
                                     double longitude2, double lng2){
                    double Lat1 =longitude1*Math.PI / 180.0;
                    double Lat2 =longitude2*Math.PI / 180.0;
                    double a = Lat1 - Lat2;
                    double b =lng1*Math.PI / 180.0 - lng2*Math.PI / 180.0;
                    double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                            + Math.cos(Lat1) * Math.cos(Lat2)
                            * Math.pow(Math.sin(b / 2), 2)));
                    s = s * 6378.137;//地球赤道半径
                    //有小数的情况;注意这里的10000d中的“d”
                    s = Math.round(s * 10000d) / 10000d;
                    s=s*1000;//单位:米
                    return s;
            }*/

    //    地球半径
    private static final double EARTH_RADIUS = 6378.137;
    //第一种方法，返回多少米
    /**
     * 计算两点之间距离
     *
     * @return 返回米
     */
    public Double getDistance(double longitude1, double latitude1,
                              double longitude2, double latitude2) {
        double lat1 = (Math.PI / 180) * latitude1;
        double lat2 = (Math.PI / 180) * latitude2;

        double lon1 = (Math.PI / 180) * longitude1;
        double lon2 = (Math.PI / 180) * longitude2;

        //两点间距离 km，如果想要米的话，结果*1000
        double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) * EARTH_RADIUS;
        if (d < 1)
            return (Double) (d * 1000);
        else
            return Double.valueOf(String.format("%.2f", d * 1000));
    }





}
