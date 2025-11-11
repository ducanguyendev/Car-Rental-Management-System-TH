package com.example.ungdungthuexe.repository;

import com.example.ungdungthuexe.api.ApiService;
import com.example.ungdungthuexe.api.RetrofitClient;
import com.example.ungdungthuexe.model.BaseResponse;
import com.example.ungdungthuexe.model.GiaThue;
import com.example.ungdungthuexe.model.PhieuDatXeRequest;
import com.example.ungdungthuexe.model.PhieuDatXeResponse;
import com.example.ungdungthuexe.model.XeDetail;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class XeDetailRepository {
    private ApiService apiService;

    public XeDetailRepository() {
        this.apiService = RetrofitClient.getInstance().getApiService();
    }

    public void getXeDetail(int maXe, DataCallback<XeDetail> callback) {
        Call<BaseResponse<XeDetail>> call = apiService.getXeDetail(maXe);
        call.enqueue(new Callback<BaseResponse<XeDetail>>() {
            @Override
            public void onResponse(Call<BaseResponse<XeDetail>> call, Response<BaseResponse<XeDetail>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(response.body().getData());
                } else {
                    callback.onError("Lỗi khi lấy thông tin xe");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<XeDetail>> call, Throwable t) {
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    public void getGiaThue(int loaiXeId, DataCallback<GiaThue> callback) {
        Call<BaseResponse<GiaThue>> call = apiService.getGiaThue(loaiXeId);
        call.enqueue(new Callback<BaseResponse<GiaThue>>() {
            @Override
            public void onResponse(Call<BaseResponse<GiaThue>> call, Response<BaseResponse<GiaThue>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(response.body().getData());
                } else {
                    callback.onError("Lỗi khi lấy giá thuê");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<GiaThue>> call, Throwable t) {
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    public void createPhieuDatXe(PhieuDatXeRequest request, DataCallback<PhieuDatXeResponse> callback) {
        Call<PhieuDatXeResponse> call = apiService.createPhieuDatXe(request);
        call.enqueue(new Callback<PhieuDatXeResponse>() {
            @Override
            public void onResponse(Call<PhieuDatXeResponse> call, Response<PhieuDatXeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Lỗi khi đặt xe");
                }
            }

            @Override
            public void onFailure(Call<PhieuDatXeResponse> call, Throwable t) {
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    public interface DataCallback<T> {
        void onSuccess(T data);
        void onError(String error);
    }
}

