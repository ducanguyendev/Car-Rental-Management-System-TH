package com.example.ungdungthuexe.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ungdungthuexe.model.CreateHoaDonRequest;
import com.example.ungdungthuexe.model.HopDongSpinnerItem;
import com.example.ungdungthuexe.repository.CreateInvoiceRepository;

import java.util.List;

public class CreateInvoiceViewModel extends AndroidViewModel {
    private CreateInvoiceRepository repository;
    private MutableLiveData<List<HopDongSpinnerItem>> contractListLiveData;
    private MutableLiveData<Boolean> isLoadingLiveData;
    private MutableLiveData<String> errorLiveData;
    private MutableLiveData<Boolean> createSuccessLiveData;

    public CreateInvoiceViewModel(@NonNull Application application) {
        super(application);
        this.repository = new CreateInvoiceRepository();
        this.contractListLiveData = new MutableLiveData<>();
        this.isLoadingLiveData = new MutableLiveData<>();
        this.errorLiveData = new MutableLiveData<>();
        this.createSuccessLiveData = new MutableLiveData<>();
    }

    public LiveData<List<HopDongSpinnerItem>> getContractList() {
        return contractListLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoadingLiveData;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }

    public LiveData<Boolean> getCreateSuccess() {
        return createSuccessLiveData;
    }

    public void loadContracts() {
        isLoadingLiveData.setValue(true);

        repository.getContractsForSpinner(new CreateInvoiceRepository.DataCallback<List<HopDongSpinnerItem>>() {
            @Override
            public void onSuccess(List<HopDongSpinnerItem> data) {
                isLoadingLiveData.setValue(false);
                contractListLiveData.setValue(data);
            }

            @Override
            public void onError(String error) {
                isLoadingLiveData.setValue(false);
                errorLiveData.setValue(error);
            }
        });
    }

    public void submitHoaDon(CreateHoaDonRequest request) {
        isLoadingLiveData.setValue(true);

        repository.createHoaDon(request, new CreateInvoiceRepository.ActionCallback() {
            @Override
            public void onSuccess() {
                isLoadingLiveData.setValue(false);
                createSuccessLiveData.setValue(true);
            }

            @Override
            public void onError(String error) {
                isLoadingLiveData.setValue(false);
                errorLiveData.setValue(error);
            }
        });
    }
}
