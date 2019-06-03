package com.example.fansonlib.bean;

/**
 * @author Created by：fansonq
 *         Created Time: 2019/5/31 16:59
 *         Describe：MVVM网络请求时，页面状态的Bean
 */

public class LoadStateBean extends BaseBean {

    /**
     * 页面状态值
     */
    private String state;

    /**
     * 网络返回的code
     */
    private int code;

    /**
     * 网络返回的提示
     */
    private String content;

    /**
     * 构造LoadStateBean
     *
     * @param state 页面状态值
     */
    public LoadStateBean(String state) {
        this.state = state;
    }

    /**
     * 构造LoadStateBean
     *
     * @param state   页面状态值
     * @param content 网络返回的提示
     */
    public LoadStateBean(String state, String content) {
        this.state = state;
        this.content = content;
    }

    /**
     * 构造LoadStateBean
     *
     * @param code    网络返回的code
     * @param content 网络返回的提示
     */
    public LoadStateBean(int code, String content) {
        this.code = code;
        this.content = content;
    }

    /**
     * 构造LoadStateBean
     *
     * @param state 页面状态值
     * @param code  网络返回的code
     */
    public LoadStateBean(String state, int code) {
        this.code = code;
        this.state = state;
    }

    /**
     * 构造LoadStateBean
     *
     * @param state   页面状态值
     * @param code    网络返回的code
     * @param content 网络返回的提示
     */
    public LoadStateBean(String state, int code, String content) {
        this.code = code;
        this.state = state;
        this.content = content;
    }


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
