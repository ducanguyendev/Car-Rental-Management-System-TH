package com.example.ungdungthuexe.repository;

import com.example.ungdungthuexe.api.ApiService;
import com.example.ungdungthuexe.api.RetrofitClient;
import com.example.ungdungthuexe.model.BaseResponse;
import com.example.ungdungthuexe.model.ManageInvoiceResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageInvoicesRepository {
    private ApiService apiService;

    public ManageInvoicesRepository() {
        this.apiService = RetrofitClient.getInstance().getApiService();
    }

    public void getAllInvoices(DataCallback<List<ManageInvoiceResponse>> callback) {
        Call<BaseResponse<List<ManageInvoiceResponse>>> call = apiService.getAllInvoices();
        call.enqueue(new Callback<BaseResponse<List<ManageInvoiceResponse>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<ManageInvoiceResponse>>> call, Response<BaseResponse<List<ManageInvoiceResponse>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(response.body().getData());
                } else {
                    callback.onError("Lỗi khi lấy danh sách hóa đơn");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<ManageInvoiceResponse>>> call, Throwable t) {
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    public interface DataCallback<T> {
        void onSuccess(T data);
        void onError(String error);
    }
}

