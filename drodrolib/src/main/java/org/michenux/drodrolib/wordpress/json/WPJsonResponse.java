package org.michenux.drodrolib.wordpress.json;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WPJsonResponse {

    public static final String STATUS_OK = "ok";

    private String status ;
    private int count ;
    @SerializedName("count_total")
    private int countTotal;
    private int pages;
    private List<WPJsonPost> posts;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCountTotal() {
        return countTotal;
    }

    public void setCountTotal(int countTotal) {
        this.countTotal = countTotal;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<WPJsonPost> getPosts() {
        return posts;
    }

    public void setPosts(List<WPJsonPost> posts) {
        this.posts = posts;
    }
}
