package com.example.ungdungthuexe.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ungdungthuexe.model.ManageCustomerResponse;
import com.example.ungdungthuexe.repository.ManageCustomersRepository;

import java.util.List;

public class ManageCustomersViewModel extends AndroidViewModel {
    private ManageCustomersRepository repository;
    private MutableLiveData<List<ManageCustomerResponse>> customerListLiveData;
    private MutableLiveData<Boolean> isLoadingLiveData;
    private MutableLiveData<String> errorLiveData;

    public ManageCustomersViewModel(@NonNull Application application) {
        super(application);
        this.repository = new ManageCustomersRepository();
        this.customerListLiveData = new MutableLiveData<>();
        this.isLoadingLiveData = new MutableLiveData<>();
        this.errorLiveData = new MutableLiveData<>();
    }

    public LiveData<List<ManageCustomerResponse>> getCustomerList() {
        return customerListLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoadingLiveData;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }

    public void loadAllCustomers() {
        isLoadingLiveData.setValue(true);

        repository.getAllCustomers(new ManageCustomersRepository.DataCallback<List<ManageCustomerResponse>>() {
            @Override
            public void onSuccess(List<ManageCustomerResponse> data) {
                isLoadingLiveData.setValue(false);
                customerListLiveData.setValue(data);
            }

            @Override
            public void onError(String error) {
                isLoadingLiveData.setValue(false);
                errorLiveData.setValue(error);
            }
        });
    }

    public void searchCustomers(String keyword) {
        isLoadingLiveData.setValue(true);

        repository.searchCustomers(keyword, new ManageCustomersRepository.DataCallback<List<ManageCustomerResponse>>() {
            @Override
            public void onSuccess(List<ManageCustomerResponse> data) {
                isLoadingLiveData.setValue(false);
                customerListLiveData.setValue(data);
            }

            @Override
            public void onError(String error) {
                isLoadingLiveData.setValue(false);
                errorLiveData.setValue(error);
            }
        });
    }
}
