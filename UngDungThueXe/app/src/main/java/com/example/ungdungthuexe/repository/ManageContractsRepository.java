package com.example.ungdungthuexe.repository;

import com.example.ungdungthuexe.api.ApiService;
import com.example.ungdungthuexe.api.RetrofitClient;
import com.example.ungdungthuexe.model.BaseResponse;
import com.example.ungdungthuexe.model.ManageContractResponse;
import com.example.ungdungthuexe.model.UpdateContractStatusRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageContractsRepository {
    private ApiService apiService;

    public ManageContractsRepository() {
        this.apiService = RetrofitClient.getInstance().getApiService();
    }

    public void getAllContracts(DataCallback<List<ManageContractResponse>> callback) {
        Call<BaseResponse<List<ManageContractResponse>>> call = apiService.getAllContracts();
        call.enqueue(new Callback<BaseResponse<List<ManageContractResponse>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<ManageContractResponse>>> call, Response<BaseResponse<List<ManageContractResponse>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(response.body().getData());
                } else {
                    callback.onError("Lỗi khi lấy danh sách hợp đồng");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<ManageContractResponse>>> call, Throwable t) {
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    public void updateContractStatus(int contractId, String status, ActionCallback callback) {
        UpdateContractStatusRequest request = new UpdateContractStatusRequest(status);
        Call<Object> call = apiService.updateContractStatus(contractId, request);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError("Lỗi khi cập nhật trạng thái");
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    public interface DataCallback<T> {
        void onSuccess(T data);
        void onError(String error);
    }

    public interface ActionCallback {
        void onSuccess();
        void onError(String error);
    }
}

