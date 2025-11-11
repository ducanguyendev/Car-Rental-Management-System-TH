package com.example.ungdungthuexe.repository;

import com.example.ungdungthuexe.api.ApiService;
import com.example.ungdungthuexe.api.RetrofitClient;
import com.example.ungdungthuexe.model.BaseResponse;
import com.example.ungdungthuexe.model.MyInvoice;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyInvoicesRepository {
    private ApiService apiService;

    public MyInvoicesRepository() {
        this.apiService = RetrofitClient.getInstance().getApiService();
    }

    public void getMyInvoices(int customerId, DataCallback<List<MyInvoice>> callback) {
        Call<BaseResponse<List<MyInvoice>>> call = apiService.getMyInvoices(customerId);
        call.enqueue(new Callback<BaseResponse<List<MyInvoice>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<MyInvoice>>> call, Response<BaseResponse<List<MyInvoice>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(response.body().getData());
                } else {
                    callback.onError("Lỗi khi lấy danh sách hóa đơn");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<MyInvoice>>> call, Throwable t) {
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    public interface DataCallback<T> {
        void onSuccess(T data);
        void onError(String error);
    }
}

