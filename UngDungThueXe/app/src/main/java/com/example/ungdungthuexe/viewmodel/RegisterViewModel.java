package com.example.ungdungthuexe.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ungdungthuexe.model.RegisterRequest;
import com.example.ungdungthuexe.model.RegisterResponse;
import com.example.ungdungthuexe.repository.RegisterRepository;

public class RegisterViewModel extends AndroidViewModel {
    private RegisterRepository registerRepository;
    private MutableLiveData<RegisterResponse> registerResponseLiveData;
    private MutableLiveData<String> errorLiveData;
    private MutableLiveData<Boolean> isLoadingLiveData;

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        this.registerRepository = new RegisterRepository();
        this.registerResponseLiveData = new MutableLiveData<>();
        this.errorLiveData = new MutableLiveData<>();
        this.isLoadingLiveData = new MutableLiveData<>();
    }

    public LiveData<RegisterResponse> getRegisterResponse() {
        return registerResponseLiveData;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoadingLiveData;
    }

    public void register(String tenDangNhap, String matKhau, String hoTen, 
                        String soCMND, String soHoKhau, String soDienThoai, String diaChiCoQuan) {
        isLoadingLiveData.setValue(true);
        
        RegisterRequest request = new RegisterRequest(tenDangNhap, matKhau, hoTen, 
                                                      soCMND, soHoKhau, soDienThoai, diaChiCoQuan);
        registerRepository.register(request, new RegisterRepository.RegisterCallback() {
            @Override
            public void onSuccess(RegisterResponse response) {
                isLoadingLiveData.setValue(false);
                registerResponseLiveData.setValue(response);
            }

            @Override
            public void onError(String error) {
                isLoadingLiveData.setValue(false);
                errorLiveData.setValue(error);
            }
        });
    }
}

