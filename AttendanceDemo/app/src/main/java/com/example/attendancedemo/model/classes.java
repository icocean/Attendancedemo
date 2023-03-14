package com.example.attendancedemo.model;

import java.util.List;

public class classes<C> {

    private List<ClasListBean> clasList;

    public List<ClasListBean> getClasList() {
        return clasList;
    }

    public void setClasList(List<ClasListBean> clasList) {
        this.clasList = clasList;
    }

    public static class ClasListBean {
        /**
         * clasId : 14
         * clasName : 安卓73班
         */

        private int clasId;
        private String clasName;

        public int getClasId() {
            return clasId;
        }

        public void setClasId(int clasId) {
            this.clasId = clasId;
        }

        public String getClasName() {
            return clasName;
        }

        public void setClasName(String clasName) {
            this.clasName = clasName;
        }
    }
}
