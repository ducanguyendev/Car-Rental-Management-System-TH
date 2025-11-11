package com.example.ungdungthuexe.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ungdungthuexe.model.LoaiXe;
import com.example.ungdungthuexe.model.Xe;
import com.example.ungdungthuexe.repository.CustomerHomeRepository;

import java.util.List;

public class CustomerHomeViewModel extends AndroidViewModel {
    private CustomerHomeRepository repository;
    private MutableLiveData<List<Xe>> xeListLiveData;
    private MutableLiveData<List<LoaiXe>> loaiXeListLiveData;
    private MutableLiveData<Boolean> isLoadingLiveData;
    private MutableLiveData<String> errorLiveData;

    public CustomerHomeViewModel(@NonNull Application application) {
        super(application);
        this.repository = new CustomerHomeRepository();
        this.xeListLiveData = new MutableLiveData<>();
        this.loaiXeListLiveData = new MutableLiveData<>();
        this.isLoadingLiveData = new MutableLiveData<>();
        this.errorLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Xe>> getXeList() {
        return xeListLiveData;
    }

    public LiveData<List<LoaiXe>> getLoaiXeList() {
        return loaiXeListLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoadingLiveData;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }

    public void loadData() {
        isLoadingLiveData.setValue(true);

        // Load available xe
        repository.getAvailableXe(new CustomerHomeRepository.DataCallback<List<Xe>>() {
            @Override
            public void onSuccess(List<Xe> data) {
                xeListLiveData.setValue(data);
                checkLoadingComplete();
            }

            @Override
            public void onError(String error) {
                isLoadingLiveData.setValue(false);
                errorLiveData.setValue(error);
            }
        });

        // Load loai xe
        repository.getLoaiXe(new CustomerHomeRepository.DataCallback<List<LoaiXe>>() {
            @Override
            public void onSuccess(List<LoaiXe> data) {
                loaiXeListLiveData.setValue(data);
                checkLoadingComplete();
            }

            @Override
            public void onError(String error) {
                isLoadingLiveData.setValue(false);
                errorLiveData.setValue(error);
            }
        });
    }

    private void checkLoadingComplete() {
        if (xeListLiveData.getValue() != null && loaiXeListLiveData.getValue() != null) {
            isLoadingLiveData.setValue(false);
        }
    }
}

