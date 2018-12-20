package com.app.D1App;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.app.D1App.API.ServiceGenerator;
import com.app.D1App.API.TiendasAppService;
import com.app.D1App.Adapter.NuevoAdapter;
import com.app.D1App.ApiResponse.GetBannerResponse;
import com.app.D1App.Utils.AppCommon;
import com.google.gson.JsonSyntaxException;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NuevoFragment extends Fragment {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    Call call;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nuevo, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        callingBannerAPI();
        return view;
    }

    void callingBannerAPI() {
        AppCommon.getInstance(getActivity()).setNonTouchableFlags(getActivity());
        if (AppCommon.getInstance(getActivity()).isConnectingToInternet(getActivity())) {
            progressBar.setVisibility(View.VISIBLE);
            TiendasAppService tiendasAppService = ServiceGenerator.createService(TiendasAppService.class);
            call = tiendasAppService.GetBanners();
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(getActivity()).clearNonTouchableFlags(getActivity());
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        if (response.body() != null) {
                            GetBannerResponse getBannerResponse = (GetBannerResponse) response.body();
                            assert getBannerResponse != null;
                            if (getBannerResponse.getSuccess() == 1) {
                                setBannerAdapter(getBannerResponse);
                            } else {
                                AppCommon.showDialog(getActivity(), getBannerResponse.getError());
                            }
                        }
                    } else {
                        AppCommon.showDialog(getActivity(), getString(R.string.serverError));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(getActivity()).clearNonTouchableFlags(getActivity());
                    progressBar.setVisibility(View.GONE);
                    if (t instanceof JsonSyntaxException) {
                        AppCommon.showDialog(getActivity(), "Something went wrong please try Again");
                    } else {
                        AppCommon.showDialog(getActivity(), getResources().getString(R.string.network_error));
                    }
                }
            });
        } else {
            AppCommon.getInstance(getActivity()).clearNonTouchableFlags(getActivity());
            AppCommon.showDialog(getActivity(), getResources().getString(R.string.network_error));
        }
    }

    private void setBannerAdapter(GetBannerResponse getBannerResponse) {
        recyclerView.setAdapter(new NuevoAdapter(getActivity(), getBannerResponse));
    }

}
