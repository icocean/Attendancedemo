package com.example.attendancedemo.model;

public class RequestQiandao {


    /**
     * res : 0 尚未实名认证 ，无法签到    1 签到成功    -1 学员不存在   -2 签到失败
     */

    private String res;

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }
}
