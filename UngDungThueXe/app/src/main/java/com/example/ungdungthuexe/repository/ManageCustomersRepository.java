package com.example.ungdungthuexe.repository;

import com.example.ungdungthuexe.api.ApiService;
import com.example.ungdungthuexe.api.RetrofitClient;
import com.example.ungdungthuexe.model.BaseResponse;
import com.example.ungdungthuexe.model.ManageCustomerResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageCustomersRepository {
    private ApiService apiService;

    public ManageCustomersRepository() {
        this.apiService = RetrofitClient.getInstance().getApiService();
    }

    public void getAllCustomers(DataCallback<List<ManageCustomerResponse>> callback) {
        Call<BaseResponse<List<ManageCustomerResponse>>> call = apiService.getAllCustomers();
        call.enqueue(new Callback<BaseResponse<List<ManageCustomerResponse>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<ManageCustomerResponse>>> call, Response<BaseResponse<List<ManageCustomerResponse>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(response.body().getData());
                } else {
                    callback.onError("Lỗi khi lấy danh sách khách hàng");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<ManageCustomerResponse>>> call, Throwable t) {
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    public void searchCustomers(String keyword, DataCallback<List<ManageCustomerResponse>> callback) {
        Call<BaseResponse<List<ManageCustomerResponse>>> call = apiService.searchCustomers(keyword);
        call.enqueue(new Callback<BaseResponse<List<ManageCustomerResponse>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<ManageCustomerResponse>>> call, Response<BaseResponse<List<ManageCustomerResponse>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(response.body().getData());
                } else {
                    callback.onError("Lỗi khi tìm kiếm khách hàng");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<ManageCustomerResponse>>> call, Throwable t) {
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    public interface DataCallback<T> {
        void onSuccess(T data);
        void onError(String error);
    }
}
