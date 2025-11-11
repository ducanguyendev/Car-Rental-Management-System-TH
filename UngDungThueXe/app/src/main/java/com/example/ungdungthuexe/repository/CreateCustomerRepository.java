package com.example.ungdungthuexe.repository;

import com.example.ungdungthuexe.api.ApiService;
import com.example.ungdungthuexe.api.RetrofitClient;
import com.example.ungdungthuexe.model.CreateCustomerRequest;
import com.example.ungdungthuexe.model.CreateCustomerResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateCustomerRepository {
    private ApiService apiService;

    public CreateCustomerRepository() {
        this.apiService = RetrofitClient.getInstance().getApiService();
    }

    public void createCustomer(CreateCustomerRequest request, DataCallback<CreateCustomerResponse> callback) {
        Call<CreateCustomerResponse> call = apiService.createCustomer(request);
        call.enqueue(new Callback<CreateCustomerResponse>() {
            @Override
            public void onResponse(Call<CreateCustomerResponse> call, Response<CreateCustomerResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(response.body());
                } else {
                    String errorMsg = "Lỗi khi tạo khách hàng";
                    if (response.body() != null && response.body().getMessage() != null) {
                        errorMsg = response.body().getMessage();
                    }
                    callback.onError(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<CreateCustomerResponse> call, Throwable t) {
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    public interface DataCallback<T> {
        void onSuccess(T data);
        void onError(String error);
    }
}
