package com.app.D1App.ApiResponse;

import com.app.D1App.ApiObject.GetListObject;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kartikeya on 03/11/2018.
 */

public class GetListingResponse {
    @SerializedName("success")
    private Integer success;
    @SerializedName("result")
    private ArrayList<GetListObject> result = null;
    @SerializedName("error")
    private String error;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public ArrayList<GetListObject> getResult() {
        return result;
    }

    public void setResult(ArrayList<GetListObject> result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
