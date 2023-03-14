package com.example.attendancedemo.model;

import java.util.List;

public class RequestqiandaoList<L>{


    private List<SignListBean> signList;

    public List<SignListBean> getSignList() {
        return signList;
    }

    public void setSignList(List<SignListBean> signList) {
        this.signList = signList;
    }

    public static class SignListBean {
        /**
         * isLate : 1
         * signTime : 2023-03-06 12:09:07.0
         * stuName : 郭少
         */

        private int isLate;
        private String signTime;
        private String stuName;

        public int getIsLate() {
            return isLate;
        }

        public void setIsLate(int isLate) {
            this.isLate = isLate;
        }

        public String getSignTime() {
            return signTime;
        }

        public void setSignTime(String signTime) {
            this.signTime = signTime;
        }

        public String getStuName() {
            return stuName;
        }

        public void setStuName(String stuName) {
            this.stuName = stuName;
        }
    }
}
