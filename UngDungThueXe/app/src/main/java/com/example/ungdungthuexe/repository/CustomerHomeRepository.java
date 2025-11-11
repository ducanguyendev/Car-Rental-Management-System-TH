package com.example.ungdungthuexe.repository;

import com.example.ungdungthuexe.api.ApiService;
import com.example.ungdungthuexe.api.RetrofitClient;
import com.example.ungdungthuexe.model.BaseResponse;
import com.example.ungdungthuexe.model.LoaiXe;
import com.example.ungdungthuexe.model.Xe;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerHomeRepository {
    private ApiService apiService;

    public CustomerHomeRepository() {
        this.apiService = RetrofitClient.getInstance().getApiService();
    }

    public void getAvailableXe(DataCallback<List<Xe>> callback) {
        Call<BaseResponse<List<Xe>>> call = apiService.getAvailableXe();
        call.enqueue(new Callback<BaseResponse<List<Xe>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<Xe>>> call, Response<BaseResponse<List<Xe>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(response.body().getData());
                } else {
                    callback.onError("Lỗi khi lấy danh sách xe");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<Xe>>> call, Throwable t) {
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    public void getLoaiXe(DataCallback<List<LoaiXe>> callback) {
        Call<BaseResponse<List<LoaiXe>>> call = apiService.getLoaiXe();
        call.enqueue(new Callback<BaseResponse<List<LoaiXe>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<LoaiXe>>> call, Response<BaseResponse<List<LoaiXe>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(response.body().getData());
                } else {
                    callback.onError("Lỗi khi lấy danh sách loại xe");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<LoaiXe>>> call, Throwable t) {
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    public interface DataCallback<T> {
        void onSuccess(T data);
        void onError(String error);
    }
}

