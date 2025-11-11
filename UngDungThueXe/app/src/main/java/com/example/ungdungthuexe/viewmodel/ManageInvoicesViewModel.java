package com.example.ungdungthuexe.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ungdungthuexe.model.ManageInvoiceResponse;
import com.example.ungdungthuexe.repository.ManageInvoicesRepository;

import java.util.List;

public class ManageInvoicesViewModel extends AndroidViewModel {
    private ManageInvoicesRepository repository;
    private MutableLiveData<List<ManageInvoiceResponse>> invoicesLiveData;
    private MutableLiveData<Boolean> isLoadingLiveData;
    private MutableLiveData<String> errorLiveData;

    public ManageInvoicesViewModel(@NonNull Application application) {
        super(application);
        this.repository = new ManageInvoicesRepository();
        this.invoicesLiveData = new MutableLiveData<>();
        this.isLoadingLiveData = new MutableLiveData<>();
        this.errorLiveData = new MutableLiveData<>();
    }

    public LiveData<List<ManageInvoiceResponse>> getInvoices() {
        return invoicesLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoadingLiveData;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }

    public void loadAllInvoices() {
        isLoadingLiveData.setValue(true);

        repository.getAllInvoices(new ManageInvoicesRepository.DataCallback<List<ManageInvoiceResponse>>() {
            @Override
            public void onSuccess(List<ManageInvoiceResponse> data) {
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

