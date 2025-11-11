package com.example.ungdungthuexe.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ungdungthuexe.model.MyBooking;
import com.example.ungdungthuexe.repository.MyBookingsRepository;

import java.util.List;

public class MyBookingsViewModel extends AndroidViewModel {
    private MyBookingsRepository repository;
    private MutableLiveData<List<MyBooking>> bookingsLiveData;
    private MutableLiveData<Boolean> isLoadingLiveData;
    private MutableLiveData<String> errorLiveData;

    public MyBookingsViewModel(@NonNull Application application) {
        super(application);
        this.repository = new MyBookingsRepository();
        this.bookingsLiveData = new MutableLiveData<>();
        this.isLoadingLiveData = new MutableLiveData<>();
        this.errorLiveData = new MutableLiveData<>();
    }

    public LiveData<List<MyBooking>> getBookings() {
        return bookingsLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoadingLiveData;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }

    public void loadBookings(String username) {
        isLoadingLiveData.setValue(true);

        repository.getMyBookings(username, new MyBookingsRepository.DataCallback<List<MyBooking>>() {
            @Override
            public void onSuccess(List<MyBooking> data) {
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
}

