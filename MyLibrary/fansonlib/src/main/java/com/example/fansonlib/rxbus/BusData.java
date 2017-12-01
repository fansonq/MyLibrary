package com.example.fansonlib.rxbus;

public class BusData {

   private String code;

    private  Object object;

    public BusData(){}

    public BusData(String code, Object object) {
        this.code = code;
        this.object = object;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

}
