package com.example.ungdungthuexe.repository;

import com.example.ungdungthuexe.api.ApiService;
import com.example.ungdungthuexe.api.RetrofitClient;
import com.example.ungdungthuexe.model.BaseResponse;
import com.example.ungdungthuexe.model.MyContract;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyContractsRepository {
    private ApiService apiService;

    public MyContractsRepository() {
        this.apiService = RetrofitClient.getInstance().getApiService();
    }

    public void getMyContracts(String username, DataCallback<List<MyContract>> callback) {
        Call<BaseResponse<List<MyContract>>> call = apiService.getMyContracts(username);
        call.enqueue(new Callback<BaseResponse<List<MyContract>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<MyContract>>> call, Response<BaseResponse<List<MyContract>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(response.body().getData());
                } else {
                    callback.onError("Lỗi khi lấy danh sách hợp đồng");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<MyContract>>> call, Throwable t) {
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    public interface DataCallback<T> {
        void onSuccess(T data);
        void onError(String error);
    }
}

