package com.example.attendancedemo.model;

import java.util.List;

public class RequestSchool <S>{
    private List<school> schoolList;

    public List<school> getSchoolList() {
        return schoolList;
    }

    public void setSchoolList(List<school> schoolList) {
        this.schoolList = schoolList;
    }
}
