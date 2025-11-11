package com.example.ungdungthuexe.repository;

import com.example.ungdungthuexe.api.ApiService;
import com.example.ungdungthuexe.api.RetrofitClient;
import com.example.ungdungthuexe.model.BaseResponse;
import com.example.ungdungthuexe.model.MyBooking;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyBookingsRepository {
    private ApiService apiService;

    public MyBookingsRepository() {
        this.apiService = RetrofitClient.getInstance().getApiService();
    }

    public void getMyBookings(String username, DataCallback<List<MyBooking>> callback) {
        Call<BaseResponse<List<MyBooking>>> call = apiService.getMyBookings(username);
        call.enqueue(new Callback<BaseResponse<List<MyBooking>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<MyBooking>>> call, Response<BaseResponse<List<MyBooking>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(response.body().getData());
                } else {
                    callback.onError("Lỗi khi lấy danh sách phiếu đặt xe");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<MyBooking>>> call, Throwable t) {
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    public interface DataCallback<T> {
        void onSuccess(T data);
        void onError(String error);
    }
}

