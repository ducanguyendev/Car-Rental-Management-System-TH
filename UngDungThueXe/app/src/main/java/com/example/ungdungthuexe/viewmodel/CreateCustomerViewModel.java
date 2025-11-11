package com.example.ungdungthuexe.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ungdungthuexe.model.CreateCustomerRequest;
import com.example.ungdungthuexe.model.CreateCustomerResponse;
import com.example.ungdungthuexe.repository.CreateCustomerRepository;

public class CreateCustomerViewModel extends AndroidViewModel {
    private CreateCustomerRepository repository;
    private MutableLiveData<CreateCustomerResponse> createResultLiveData;
    private MutableLiveData<Boolean> isLoadingLiveData;
    private MutableLiveData<String> errorLiveData;

    public CreateCustomerViewModel(@NonNull Application application) {
        super(application);
        this.repository = new CreateCustomerRepository();
        this.createResultLiveData = new MutableLiveData<>();
        this.isLoadingLiveData = new MutableLiveData<>();
        this.errorLiveData = new MutableLiveData<>();
    }

    public LiveData<CreateCustomerResponse> getCreateResult() {
        return createResultLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoadingLiveData;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }

    public void submitCustomer(CreateCustomerRequest request) {
        isLoadingLiveData.setValue(true);

        repository.createCustomer(request, new CreateCustomerRepository.DataCallback<CreateCustomerResponse>() {
            @Override
            public void onSuccess(CreateCustomerResponse data) {
                isLoadingLiveData.setValue(false);
                createResultLiveData.setValue(data);
            }

            @Override
            public void onError(String error) {
                isLoadingLiveData.setValue(false);
                errorLiveData.setValue(error);
            }
        });
    }
}
