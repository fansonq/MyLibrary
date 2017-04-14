package com.example.fansonlib.eventBean;

/**
 * Created by fanson on 2016/8/25.
 */
public class EventNetWork {

    public static int AVAILABLE = 1;
    public static int UNAVAILABLE = -1;

    private int result;

    public EventNetWork(int result) {
        this.result = result;
    }

    public int getResult() {
        return result;
    }
}
