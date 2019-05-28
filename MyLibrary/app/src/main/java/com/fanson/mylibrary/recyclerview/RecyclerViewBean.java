package com.fanson.mylibrary.recyclerview;

import java.util.List;

/**
 * @author Created by：Fanson
 * Created Time: 2019/5/25 9:42
 * Describe：测试RecyclerView的实体类
 */
public class RecyclerViewBean {


    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * name : Google
         */

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
