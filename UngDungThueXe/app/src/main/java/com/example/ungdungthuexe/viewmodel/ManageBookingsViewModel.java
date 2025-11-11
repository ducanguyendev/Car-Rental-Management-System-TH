package com.example.ungdungthuexe.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ungdungthuexe.model.BookingDetailsAccountant;
import com.example.ungdungthuexe.repository.ManageBookingsRepository;

import java.util.List;

public class ManageBookingsViewModel extends AndroidViewModel {
    private ManageBookingsRepository repository;
    private MutableLiveData<List<BookingDetailsAccountant>> bookingsLiveData;
    private MutableLiveData<Boolean> isLoadingLiveData;
    private MutableLiveData<String> errorLiveData;
    private MutableLiveData<String> successMessageLiveData;

    public ManageBookingsViewModel(@NonNull Application application) {
        super(application);
        this.repository = new ManageBookingsRepository();
        this.bookingsLiveData = new MutableLiveData<>();
        this.isLoadingLiveData = new MutableLiveData<>();
        this.errorLiveData = new MutableLiveData<>();
        this.successMessageLiveData = new MutableLiveData<>();
    }

    public LiveData<List<BookingDetailsAccountant>> getBookings() {
        return bookingsLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoadingLiveData;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }

    public LiveData<String> getSuccessMessage() {
        return successMessageLiveData;
    }

    public void loadAllBookings() {
        isLoadingLiveData.setValue(true);

        repository.getAllBookings(new ManageBookingsRepository.DataCallback<List<BookingDetailsAccountant>>() {
            @Override
            public void onSuccess(List<BookingDetailsAccountant> data) {
                isLoadingLiveData.setValue(false);
                bookingsLiveData.setValue(data);
            }

            @Override
            public void onError(String error) {
                isLoadingLiveData.setValue(false);
                errorLiveData.setValue(error);
            }
        });
    }

    public void approveBooking(int maDatXe) {
        isLoadingLiveData.setValue(true);

        repository.updateBookingStatus(maDatXe, "Đã xác nhận", new ManageBookingsRepository.ActionCallback() {
            @Override
            public void onSuccess() {
                successMessageLiveData.setValue("Duyệt phiếu đặt xe thành công");
                loadAllBookings(); // Auto-refresh list
            }

            @Override
            public void onError(String error) {
                isLoadingLiveData.setValue(false);
                errorLiveData.setValue(error);
            }
        });
    }

    public void rejectBooking(int maDatXe) {
        isLoadingLiveData.setValue(true);

        repository.updateBookingStatus(maDatXe, "Đã từ chối", new ManageBookingsRepository.ActionCallback() {
            @Override
            public void onSuccess() {
                successMessageLiveData.setValue("Từ chối phiếu đặt xe thành công");
                loadAllBookings(); // Auto-refresh list
            }

            @Override
            public void onError(String error) {
                isLoadingLiveData.setValue(false);
                errorLiveData.setValue(error);
            }
        });
    }

    public void deleteBooking(int maDatXe) {
        isLoadingLiveData.setValue(true);

        repository.deleteBooking(maDatXe, new ManageBookingsRepository.ActionCallback() {
            @Override
            public void onSuccess() {
                successMessageLiveData.setValue("Xóa phiếu đặt xe thành công");
                loadAllBookings(); // Auto-refresh list
            }

            @Override
            public void onError(String error) {
                isLoadingLiveData.setValue(false);
                errorLiveData.setValue(error);
            }
        });
    }
}

