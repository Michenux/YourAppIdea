package org.michenux.android.wordpress.json;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WPJsonPost {
    private int id ;
    private String post;
    private String slug;
    private String url;
    private String status;
    private String title;
    @SerializedName("title_plain")
    private String titlePlain;
    private String content;
    private String date;
    private String modified;
    @SerializedName("custom_fields")
    private WPCustomFields customFields;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitlePlain() {
        return titlePlain;
    }

    public void setTitlePlain(String titlePlain) {
        this.titlePlain = titlePlain;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public WPCustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(WPCustomFields customFields) {
        this.customFields = customFields;
    }
}
