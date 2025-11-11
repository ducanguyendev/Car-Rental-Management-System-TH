package com.example.ungdungthuexe.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ungdungthuexe.model.ManageContractResponse;
import com.example.ungdungthuexe.repository.ManageContractsRepository;

import java.util.List;

public class ManageContractsViewModel extends AndroidViewModel {
    private ManageContractsRepository repository;
    private MutableLiveData<List<ManageContractResponse>> contractsLiveData;
    private MutableLiveData<Boolean> isLoadingLiveData;
    private MutableLiveData<String> errorLiveData;
    private MutableLiveData<String> successMessageLiveData;

    public ManageContractsViewModel(@NonNull Application application) {
        super(application);
        this.repository = new ManageContractsRepository();
        this.contractsLiveData = new MutableLiveData<>();
        this.isLoadingLiveData = new MutableLiveData<>();
        this.errorLiveData = new MutableLiveData<>();
        this.successMessageLiveData = new MutableLiveData<>();
    }

    public LiveData<List<ManageContractResponse>> getContracts() {
        return contractsLiveData;
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

    public void loadAllContracts() {
        isLoadingLiveData.setValue(true);

        repository.getAllContracts(new ManageContractsRepository.DataCallback<List<ManageContractResponse>>() {
            @Override
            public void onSuccess(List<ManageContractResponse> data) {
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

    public void completeContract(int maHopDong) {
        isLoadingLiveData.setValue(true);

        repository.updateContractStatus(maHopDong, "Đã hoàn thành", new ManageContractsRepository.ActionCallback() {
            @Override
            public void onSuccess() {
                successMessageLiveData.setValue("Hoàn thành hợp đồng thành công");
                loadAllContracts(); // Auto-refresh list
            }

            @Override
            public void onError(String error) {
                isLoadingLiveData.setValue(false);
                errorLiveData.setValue(error);
            }
        });
    }
}

