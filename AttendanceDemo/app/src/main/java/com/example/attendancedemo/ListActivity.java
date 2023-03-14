package com.example.attendancedemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.attendancedemo.Adapter.qiandaoListAddCityAdapter;
import com.example.attendancedemo.http.BaseApplication;
import com.example.attendancedemo.model.RequestqiandaoList;
import com.example.attendancedemo.model.classes;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.schedulers.Schedulers;

public class ListActivity extends AppCompatActivity {
    private static final String TAG = "ListActivity";
    List<RequestqiandaoList.SignListBean> qiandaoList;
    RecyclerView list_recycler;
    String shenId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        initview();

    }
    private void initview() {
        list_recycler=findViewById(R.id.list_recycler);
        ImageView list_item_back=findViewById(R.id.list_item_back);
        LinearLayoutManager llm= new LinearLayoutManager(this);
        list_recycler.setLayoutManager(llm);
        if (getSharedPreferences("SP", Context.MODE_PRIVATE)!=null){
            //  取值默认为shengId 没有返回 0
            shenId=getSharedPreferences("SP", Context.MODE_PRIVATE).getString("shengId","1");
        }else {
            shenId="1";
        }
        getIdlist("all",shenId);

        list_item_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    private void getIdlist(String opt,String imei) {

                BaseApplication.iSchoolRequest.requestqiandaoLists(opt,imei)
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Observer<RequestqiandaoList<RequestqiandaoList.SignListBean>>() {
                            @Override
                            public void onCompleted() {
                                Log.d(TAG, "签到记录请求成功");
                            }
                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "签到记录请求失败"+e.toString());
                            }
                            @Override
                            public void onNext(RequestqiandaoList<RequestqiandaoList.SignListBean> List) {
                                List<RequestqiandaoList.SignListBean> List1=List.getSignList();
                        /*for (int i=0;i<List1.size();i++){
                            Log.d(TAG, "签到记录 "+List1.get(i).getStuName()
                                    +List1.get(i).getSignTime()
                                    +"迟到"+List1.get(i).getIsLate());
                            RequestqiandaoList.SignListBean sb=new RequestqiandaoList.SignListBean();
                            sb.setIsLate(List1.get(i).getIsLate());
                            sb.setSignTime(List1.get(i).getSignTime());
                            sb.setStuName(List1.get(i).getStuName());
                            //qiandaoList.add(sb);
                            //Log.d(TAG, "tags: "+qiandaoList.get(2).getSignTime());
                        }*/
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        qiandaoListAddCityAdapter iad =
                                                new qiandaoListAddCityAdapter(ListActivity.this);
                                        iad.setQiandaoList(List1);
                                        list_recycler.setAdapter(iad);
                                    }
                                });

                            }
                        });

        }

}