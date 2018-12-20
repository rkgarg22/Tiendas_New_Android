package com.app.D1App.ApiResponse;

import com.app.D1App.ApiObject.GetBannerObject;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kartikeya on 19/11/2018.
 */

public class GetBannerResponse
{
    @SerializedName("success")
    private Integer success;
    @SerializedName("result")
    private List<GetBannerObject> result = null;
    @SerializedName("error")
    private String error;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public List<GetBannerObject> getResult() {
        return result;
    }

    public void setResult(List<GetBannerObject> result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
