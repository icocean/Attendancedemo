package com.example.attendancedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;

import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attendancedemo.http.BaseApplication;
import com.example.attendancedemo.model.RequestId;
import com.example.attendancedemo.model.RequestSchool;
import com.example.attendancedemo.model.classes;
import com.example.attendancedemo.model.school;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import rx.Observer;
import rx.schedulers.Schedulers;

public class RealNameActivity extends AppCompatActivity {
    private static final String TAG = "RealNameActivity";

     Spinner spSchool,spClass;
     ArrayAdapter<String> adapterSchool,adapterClass;
     List<String> schoolInfo=new ArrayList<>();
    List<String> classInfo=new ArrayList<>();
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_name);
        classInfo.add("选择班级");
        initview();
    }


    private void getschool(){
        BaseApplication.iSchoolRequest.requestSchoolList()
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<RequestSchool<school>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "学校请求成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "学校请求失败");
                    }

                    @Override
                    public void onNext(RequestSchool<school> schoolRequestSchool) {
                        List<school> schools=schoolRequestSchool.getSchoolList();
                        for (int i = 0; i < schools.size(); i++){
                            int schid=schools.get(i).getSchId();
                            String schoolName=schools.get(i).getSchName();
                            Log.d(TAG, "校区名称: "+schid+schoolName);
                            schoolInfo.add(schoolName);
                        }
                    }
                });

    }
    private void getClassList(int schId){
        BaseApplication.iSchoolRequest.requestClassList2(schId)
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<classes<classes.ClasListBean>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "班级请求成功");
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "班级请求失败");
                    }
                    @Override
                    public void onNext(classes<classes.ClasListBean> clasListBeanclasses) {
                        List<classes.ClasListBean> clasList=clasListBeanclasses.getClasList();
                        for (int i = 0; i < clasList.size(); i++){
                            clasList.get(i).getClasId();
                            String clasName=clasList.get(i).getClasName();
                            Log.d(TAG, "班级数据: "+clasList.get(i).getClasName());
                            classInfo.add(clasName);
                        }
                    }
                });
    }

    private int getIDList(String cardNum,String imei){
        BaseApplication.iSchoolRequest.requestIdList(cardNum,imei)
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<RequestId>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "ID认证请求成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "ID认证请求失败");
                    }

                    @Override
                    public void onNext(RequestId requestId) {
                         id=requestId.getRes();
                        Log.d(TAG, "ID认证请求数据"+id);
                    }
                });
        return id;
    }

    private void initview() {
        spSchool=findViewById(R.id.real_tv_xiaoqu);
        spClass=findViewById(R.id.real_tv_class);
        EditText real_tv_Id=findViewById(R.id.real_tv_Id);
        EditText real_tv_phone=findViewById(R.id.real_tv_phone);
        TextView real_tv_Id_text=findViewById(R.id.real_tv_Id_text);
        TextView real_tv_phone_text=findViewById(R.id.real_tv_phone_text);
        Button button=findViewById(R.id.huoqu);
        schoolInfo.add("选择校区");

        getschool();
        //将数据填充到适配器中
        adapterSchool = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, schoolInfo);
        spSchool.setAdapter(adapterSchool);
        spSchool.setSelection(0,true);
        adapterClass = new ArrayAdapter<String>(RealNameActivity.this,
                android.R.layout.simple_spinner_dropdown_item, classInfo);
        spClass.setAdapter(adapterClass);
        spClass.setSelection(0,true);
            //第一个下拉框监听事件
        spSchool.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String school = adapterView.getItemAtPosition(i).toString();
                /*Toast.makeText(RealNameActivity.this, "选择的校区是：" + school,
                        Toast.LENGTH_LONG).show();*/
                getClassList(i);
                adapterClass = new ArrayAdapter<String>(RealNameActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, classInfo);
                spClass.setAdapter(adapterClass);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //动态实现的下拉框，数据在程序中获得，实际项目可能来自数据库等
        this.spClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String spclass = adapterView.getItemAtPosition(i).toString();
                /*Toast.makeText(RealNameActivity.this, "选择的班级是：" + spclass,
                        Toast.LENGTH_LONG).show();*/
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //身份证号码输入实时监听
        real_tv_Id.addTextChangedListener(new TextWatcher() {
            //改变之前会执行的方法
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            //身份证号码输入实时监听
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String tel = real_tv_Id.getText().toString();
                /*String s = real_tv_Id.getText().toString().trim();
                if(TextUtils.isEmpty(s)){
                    Toast.makeText(getApplicationContext(),
                            "身份证号码未输入",Toast.LENGTH_LONG).show();
                }*/
                Pattern p = Pattern.compile("^\\d{17}([0-9]|X|x)$");


                Matcher m = p.matcher(tel);
                if (m.matches()) {
                    //提示
                    //Toast.makeText(getApplicationContext(), "[实时]正确", Toast.LENGTH_LONG).show();
                    //修改提示信息TextView
                    real_tv_Id_text.setText("正确");
                    real_tv_Id_text.setTextColor(Color.parseColor("#00EE00"));
                } else {
                    //提示
                    //Toast.makeText(getApplicationContext(), "输入有误"+charSequence.toString(), Toast.LENGTH_LONG).show();
                    //修改提示信息TextView
                    real_tv_Id_text.setText("输入有误");
                    real_tv_Id_text.setTextColor(Color.parseColor("#EE2C2C"));
                }
            }
            //改变之后会执行的方法
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //手机号码输入实时监听
        real_tv_phone.addTextChangedListener(new TextWatcher() {
            //改变之前会执行的方法
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            //实时监听
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String tel = real_tv_phone.getText().toString();

                Pattern p = Pattern.compile("^1[3|4|5|6|7|8|9][0-9]\\d{8}$");
                //("^((13[0-9])|(15[^4])|(18[0-9])|(17[0-8])|(147,145))\\d{8}$");//正则
                Matcher m = p.matcher(tel);
                if (m.matches()) {
                    //提示
                    //Toast.makeText(getApplicationContext(), "[实时]正确", Toast.LENGTH_LONG).show();
                    //修改提示信息TextView
                    real_tv_phone_text.setText("正确");
                    real_tv_phone_text.setTextColor(Color.parseColor("#00EE00"));

                } else {
                    //提示
                    //Toast.makeText(getApplicationContext(), "输入有误"+charSequence.toString(), Toast.LENGTH_LONG).show();
                    //修改提示信息TextView
                    real_tv_phone_text.setText("输入有误");
                    real_tv_phone_text.setTextColor(Color.parseColor("#EE2C2C"));
                }
            }
            //改变之后会执行的方法
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String shengId=real_tv_Id.getText().toString();
                getIDList(shengId,shengId);
                //1 认证成功    0 认证失败   -1 身份证号码不存在
                 switch (id){
                            case 1:
                                //获取SharedPreferences对象
                                Context ctx = RealNameActivity.this;
                                SharedPreferences sp = ctx.getSharedPreferences("SP", MODE_PRIVATE);
                                //存入数据
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("shengId", shengId);
                                editor.apply();
                                Toast.makeText(RealNameActivity.this, "认证成功", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(RealNameActivity.this,MainActivity.class);
                                startActivity(intent);

                            case 0:
                                Toast.makeText(RealNameActivity.this, "请重新认证", Toast.LENGTH_SHORT).show();
                                break;
                            case -1:
                                Toast.makeText(RealNameActivity.this, "身份证号码不存在,请重新认证", Toast.LENGTH_SHORT).show();
                                break;
                        }



            }
        });

        //手机号输入框失去焦点
        /*real_tv_phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    // 此处为得到焦点时的处理内容
                } else {
                    // 此处为失去焦点时的处理内容
                    String tel = real_tv_phone.getText().toString();
                    Pattern p = Pattern.compile("^1[3|4|5|6|7|8|9][0-9]\\d{8}$");
                            //("^((13[0-9])|(15[^4])|(18[0-9])|(17[0-8])|(147,145))\\d{8}$");//正则
                    Matcher m = p.matcher(tel);
                    //("^1[3|4|5|6|7|8|9][0-9]\\d{8}$")
                    if (m.matches()) {
                        //提示
                        Toast.makeText(getApplicationContext(), "[失焦]您输入了正确的手机号", Toast.LENGTH_LONG).show();
                        //修改提示信息TextView
                        real_tv_phone_text.setText("[失焦]正确");
                        real_tv_phone_text.setTextColor(Color.parseColor("#00EE00"));
                    } else {
                        //提示
                        Toast.makeText(getApplicationContext(), "[失焦]正则校验手机号不正确："+tel, Toast.LENGTH_LONG).show();
                        //修改提示信息TextView
                        real_tv_phone_text.setText("[失焦]手机号错误");
                        real_tv_phone_text.setTextColor(Color.parseColor("#EE2C2C"));
                    }
                }
            }
        });*/

    }
    /*public static int PrintArray(List<String> array,String value){
        for (int i=0;i<array.size();i++){
            if (array.get(i)==value){
                return i;
            }
        }
        return -1;
    }*/


}