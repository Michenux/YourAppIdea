package org.michenux.drodrolib.wordpress.json;

import com.google.gson.annotations.SerializedName;

public class WPThumbnailImages {

    private WPThumbnailImage full;
    private WPThumbnailImage thumbnail;
    private WPThumbnailImage medium;
    private WPThumbnailImage large;

    @SerializedName("foundation-featured-image")
    private WPThumbnailImage foundationFeaturedImage;

    public WPThumbnailImage getFull() {
        return full;
    }

    public void setFull(WPThumbnailImage full) {
        this.full = full;
    }

    public WPThumbnailImage getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(WPThumbnailImage thumbnail) {
        this.thumbnail = thumbnail;
    }

    public WPThumbnailImage getMedium() {
        return medium;
    }

    public void setMedium(WPThumbnailImage medium) {
        this.medium = medium;
    }

    public WPThumbnailImage getLarge() {
        return large;
    }

    public void setLarge(WPThumbnailImage large) {
        this.large = large;
    }

    public WPThumbnailImage getFoundationFeaturedImage() {
        return foundationFeaturedImage;
    }

    public void setFoundationFeaturedImage(WPThumbnailImage foundationFeaturedImage) {
        this.foundationFeaturedImage = foundationFeaturedImage;
    }


}
