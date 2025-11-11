package com.example.ungdungthuexe.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ungdungthuexe.model.MyContract;
import com.example.ungdungthuexe.repository.MyContractsRepository;

import java.util.List;

public class MyContractsViewModel extends AndroidViewModel {
    private MyContractsRepository repository;
    private MutableLiveData<List<MyContract>> contractsLiveData;
    private MutableLiveData<Boolean> isLoadingLiveData;
    private MutableLiveData<String> errorLiveData;

    public MyContractsViewModel(@NonNull Application application) {
        super(application);
        this.repository = new MyContractsRepository();
        this.contractsLiveData = new MutableLiveData<>();
        this.isLoadingLiveData = new MutableLiveData<>();
        this.errorLiveData = new MutableLiveData<>();
    }

    public LiveData<List<MyContract>> getContracts() {
        return contractsLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoadingLiveData;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }

    public void loadContracts(String username) {
        isLoadingLiveData.setValue(true);

        repository.getMyContracts(username, new MyContractsRepository.DataCallback<List<MyContract>>() {
            @Override
            public void onSuccess(List<MyContract> data) {
                isLoadingLiveData.setValue(false);
                contractsLiveData.setValue(data);
            }

            @Override
            public void onError(String error) {
                isLoadingLiveData.setValue(false);
                errorLiveData.setValue(error);
            }
        });
    }
}

