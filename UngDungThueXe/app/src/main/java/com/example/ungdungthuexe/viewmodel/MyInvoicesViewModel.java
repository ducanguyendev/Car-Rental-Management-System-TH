package com.example.ungdungthuexe.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ungdungthuexe.model.MyInvoice;
import com.example.ungdungthuexe.repository.MyInvoicesRepository;

import java.util.List;

public class MyInvoicesViewModel extends AndroidViewModel {
    private MyInvoicesRepository repository;
    private MutableLiveData<List<MyInvoice>> invoicesLiveData;
    private MutableLiveData<Boolean> isLoadingLiveData;
    private MutableLiveData<String> errorLiveData;

    public MyInvoicesViewModel(@NonNull Application application) {
        super(application);
        this.repository = new MyInvoicesRepository();
        this.invoicesLiveData = new MutableLiveData<>();
        this.isLoadingLiveData = new MutableLiveData<>();
        this.errorLiveData = new MutableLiveData<>();
    }

    public LiveData<List<MyInvoice>> getInvoices() {
        return invoicesLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoadingLiveData;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }

    public void loadInvoices(int customerId) {
        isLoadingLiveData.setValue(true);

        repository.getMyInvoices(customerId, new MyInvoicesRepository.DataCallback<List<MyInvoice>>() {
            @Override
            public void onSuccess(List<MyInvoice> data) {
                isLoadingLiveData.setValue(false);
                invoicesLiveData.setValue(data);
            }

            @Override
            public void onError(String error) {
                isLoadingLiveData.setValue(false);
                errorLiveData.setValue(error);
            }
        });
    }
}

