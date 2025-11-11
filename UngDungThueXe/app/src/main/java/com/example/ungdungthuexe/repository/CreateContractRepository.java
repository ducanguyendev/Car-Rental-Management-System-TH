package com.example.ungdungthuexe.repository;

import com.example.ungdungthuexe.api.ApiService;
import com.example.ungdungthuexe.api.RetrofitClient;
import com.example.ungdungthuexe.model.BaseResponse;
import com.example.ungdungthuexe.model.ChiTietHopDongs;
import com.example.ungdungthuexe.model.CreateHopDongRequest;
import com.example.ungdungthuexe.model.GiaThue;
import com.example.ungdungthuexe.model.KhachHangSpinner;
import com.example.ungdungthuexe.model.PhieuDatXeDuyet;
import com.example.ungdungthuexe.model.Xe;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateContractRepository {
    private ApiService apiService;

    public CreateContractRepository() {
        this.apiService = RetrofitClient.getInstance().getApiService();
    }

    public void getKhachHangs(DataCallback<List<KhachHangSpinner>> callback) {
        Call<BaseResponse<List<KhachHangSpinner>>> call = apiService.getKhachHangsForSpinner();
        call.enqueue(new Callback<BaseResponse<List<KhachHangSpinner>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<KhachHangSpinner>>> call, Response<BaseResponse<List<KhachHangSpinner>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(response.body().getData());
                } else {
                    callback.onError("Lỗi khi lấy danh sách khách hàng");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<KhachHangSpinner>>> call, Throwable t) {
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    public void getApprovedBookings(DataCallback<List<PhieuDatXeDuyet>> callback) {
        Call<BaseResponse<List<PhieuDatXeDuyet>>> call = apiService.getApprovedBookings("Đã duyệt");
        call.enqueue(new Callback<BaseResponse<List<PhieuDatXeDuyet>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<PhieuDatXeDuyet>>> call, Response<BaseResponse<List<PhieuDatXeDuyet>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(response.body().getData());
                } else {
                    callback.onError("Lỗi khi lấy danh sách phiếu đặt");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<PhieuDatXeDuyet>>> call, Throwable t) {
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
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

    public void createHopDong(CreateHopDongRequest request, ActionCallback callback) {
        Call<Object> call = apiService.createHopDong(request);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError("Lỗi khi tạo hợp đồng");
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

