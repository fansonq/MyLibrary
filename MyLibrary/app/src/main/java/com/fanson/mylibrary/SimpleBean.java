package com.fanson.mylibrary;

import com.example.fansonlib.bean.BaseBean;

/**
 * Created by：fanson
 * Created on：2017/9/12 18:05
 * Describe：
 */

public class SimpleBean extends BaseBean{

    private int code;

    private String message;

    private Data data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
