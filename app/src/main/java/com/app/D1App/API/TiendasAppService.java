package com.app.D1App.API;

import com.app.D1App.ApiResponse.GetBannerResponse;
import com.app.D1App.ApiResponse.GetFilterElementResponse;
import com.app.D1App.ApiResponse.GetListingResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by kartikeya on 03/11/2018.
 */

public interface TiendasAppService {

    @GET("listing/")
    Call<GetListingResponse> GetListing(@Query("latitude") double latitude, @Query("longitude") double longitude);

    @GET("filter/")
    Call<GetListingResponse> GetFilter(@Query("latitude") double latitude, @Query("longitude") double longitude,
                                       @Query("departmento") String departmento, @Query("municipio") String municipio,
                                       @Query("barrio") String barrio);

    @GET("filterElement/")
    Call<GetFilterElementResponse> GetFilterElement(@Query("parentName") String parentName, @Query("section") String section);

    @GET("banner/")
    Call<GetBannerResponse> GetBanners();

}
