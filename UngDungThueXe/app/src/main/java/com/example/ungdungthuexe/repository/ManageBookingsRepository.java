package com.example.ungdungthuexe.repository;

import com.example.ungdungthuexe.api.ApiService;
import com.example.ungdungthuexe.api.RetrofitClient;
import com.example.ungdungthuexe.model.BaseResponse;
import com.example.ungdungthuexe.model.BookingDetailsAccountant;
import com.example.ungdungthuexe.model.UpdateBookingStatusRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageBookingsRepository {
    private ApiService apiService;

    public ManageBookingsRepository() {
        this.apiService = RetrofitClient.getInstance().getApiService();
    }

    public void getAllBookings(DataCallback<List<BookingDetailsAccountant>> callback) {
        Call<BaseResponse<List<BookingDetailsAccountant>>> call = apiService.getAllBookings();
        call.enqueue(new Callback<BaseResponse<List<BookingDetailsAccountant>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<BookingDetailsAccountant>>> call, Response<BaseResponse<List<BookingDetailsAccountant>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(response.body().getData());
                } else {
                    callback.onError("Lỗi khi lấy danh sách phiếu đặt xe");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<BookingDetailsAccountant>>> call, Throwable t) {
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    public void updateBookingStatus(int bookingId, String status, ActionCallback callback) {
        UpdateBookingStatusRequest request = new UpdateBookingStatusRequest(status);
        Call<Void> call = apiService.updateBookingStatus(bookingId, request);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError("Lỗi khi cập nhật trạng thái");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    public void deleteBooking(int bookingId, ActionCallback callback) {
        Call<Void> call = apiService.deleteBooking(bookingId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError("Lỗi khi xóa phiếu đặt xe");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
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

