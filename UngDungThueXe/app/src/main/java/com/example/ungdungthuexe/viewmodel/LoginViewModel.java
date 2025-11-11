package com.example.ungdungthuexe.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ungdungthuexe.model.LoginRequest;
import com.example.ungdungthuexe.model.LoginResponse;
import com.example.ungdungthuexe.repository.LoginRepository;

public class LoginViewModel extends AndroidViewModel {
    private LoginRepository loginRepository;
    private MutableLiveData<LoginResponse> loginResponseLiveData;
    private MutableLiveData<String> errorLiveData;
    private MutableLiveData<Boolean> isLoadingLiveData;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        this.loginRepository = new LoginRepository();
        this.loginResponseLiveData = new MutableLiveData<>();
        this.errorLiveData = new MutableLiveData<>();
        this.isLoadingLiveData = new MutableLiveData<>();
    }

    public LiveData<LoginResponse> getLoginResponse() {
        return loginResponseLiveData;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoadingLiveData;
    }

    public void login(String username, String password) {
        isLoadingLiveData.setValue(true);
        
        LoginRequest request = new LoginRequest(username, password);
        loginRepository.login(request, new LoginRepository.LoginCallback() {
            @Override
            public void onSuccess(LoginResponse response) {
                isLoadingLiveData.setValue(false);
                loginResponseLiveData.setValue(response);
            }

            @Override
            public void onError(String error) {
                isLoadingLiveData.setValue(false);
                errorLiveData.setValue(error);
            }
        });
    }
}

