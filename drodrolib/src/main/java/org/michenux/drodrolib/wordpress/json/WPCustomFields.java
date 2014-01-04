package org.michenux.drodrolib.wordpress.json;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WPCustomFields {

    @SerializedName("android_desc")
    List<String> description ;

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }
}
