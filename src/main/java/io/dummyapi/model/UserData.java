package io.dummyapi.model;

import java.util.List;

public class UserData {
    private List<User> data;
    private  int total;
    private int page;
    private int limit;

    public UserData(List<User> data, int total, int page, int limit) {
        this.data = data;
        this.total = total;
        this.page = page;
        this.limit = limit;
    }

    public List<User> getData() {
        return data;
    }

    public void setData(List<User> data) {
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
