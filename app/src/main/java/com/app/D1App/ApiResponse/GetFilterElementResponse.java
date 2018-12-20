package com.app.D1App.ApiResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kartikeya on 18/11/2018.
 */

public class GetFilterElementResponse {
    @SerializedName("success")
    private Integer success;
    @SerializedName("result")
    private List<String> result = null;
    @SerializedName("error")
    private String error;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public List<String> getResult() {
        return result;
    }

    public void setResult(List<String> result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
