package com.fanson.mylibrary.bean;

import com.example.fansonlib.bean.BaseBean;
import com.fanson.mylibrary.SimpleBean;

/**
 * @author Created by：Fanson
 * Created Time: 2019/6/28 10:13
 * Describe：
 */
public class TestVmBean extends BaseBean {

    private int code;

    private String message;

    private SimpleBean.Data data;

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

    public SimpleBean.Data getData() {
        return data;
    }

    public void setData(SimpleBean.Data data) {
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
