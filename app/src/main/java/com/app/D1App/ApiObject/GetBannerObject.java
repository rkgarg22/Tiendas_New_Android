package com.app.D1App.ApiObject;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kartikeya on 19/11/2018.
 */

public class GetBannerObject
{
    @SerializedName("imgUrl")
    private String imgUrl;
    @SerializedName("url")
    private String url;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
