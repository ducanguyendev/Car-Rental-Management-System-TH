package com.example.ungdungthuexe.repository;

import com.example.ungdungthuexe.api.ApiService;
import com.example.ungdungthuexe.api.RetrofitClient;
import com.example.ungdungthuexe.model.BaseResponse;
import com.example.ungdungthuexe.model.CreateHoaDonRequest;
import com.example.ungdungthuexe.model.HopDongSpinnerItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateInvoiceRepository {
    private ApiService apiService;

    public CreateInvoiceRepository() {
        this.apiService = RetrofitClient.getInstance().getApiService();
    }

    public void getContractsForSpinner(DataCallback<List<HopDongSpinnerItem>> callback) {
        Call<BaseResponse<List<HopDongSpinnerItem>>> call = apiService.getContractsForSpinner();
        call.enqueue(new Callback<BaseResponse<List<HopDongSpinnerItem>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<HopDongSpinnerItem>>> call, Response<BaseResponse<List<HopDongSpinnerItem>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(response.body().getData());
                } else {
                    callback.onError("Lỗi khi lấy danh sách hợp đồng");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<HopDongSpinnerItem>>> call, Throwable t) {
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    public void createHoaDon(CreateHoaDonRequest request, ActionCallback callback) {
        Call<Object> call = apiService.createHoaDon(request);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError("Lỗi khi tạo hóa đơn");
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
