package com.example.ungdungthuexe.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ungdungthuexe.model.ChiTietHopDongs;
import com.example.ungdungthuexe.model.CreateHopDongRequest;
import com.example.ungdungthuexe.model.GiaThue;
import com.example.ungdungthuexe.model.KhachHangSpinner;
import com.example.ungdungthuexe.model.PhieuDatXeDuyet;
import com.example.ungdungthuexe.model.Xe;
import com.example.ungdungthuexe.repository.CreateContractRepository;

import java.util.ArrayList;
import java.util.List;

public class CreateContractViewModel extends AndroidViewModel {
    private CreateContractRepository repository;
    private MutableLiveData<List<KhachHangSpinner>> khachHangsLiveData;
    private MutableLiveData<List<PhieuDatXeDuyet>> bookingsLiveData;
    private MutableLiveData<List<Xe>> availableXeLiveData;
    private MutableLiveData<GiaThue> giaThueLiveData;
    private MutableLiveData<Boolean> isLoadingLiveData;
    private MutableLiveData<String> errorLiveData;
    private MutableLiveData<String> successMessageLiveData;

    public CreateContractViewModel(@NonNull Application application) {
        super(application);
        this.repository = new CreateContractRepository();
        this.khachHangsLiveData = new MutableLiveData<>();
        this.bookingsLiveData = new MutableLiveData<>();
        this.availableXeLiveData = new MutableLiveData<>();
        this.giaThueLiveData = new MutableLiveData<>();
        this.isLoadingLiveData = new MutableLiveData<>();
        this.errorLiveData = new MutableLiveData<>();
        this.successMessageLiveData = new MutableLiveData<>();
    }

    public LiveData<List<KhachHangSpinner>> getKhachHangs() {
        return khachHangsLiveData;
    }

    public LiveData<List<PhieuDatXeDuyet>> getBookings() {
        return bookingsLiveData;
    }

    public LiveData<List<Xe>> getAvailableXe() {
        return availableXeLiveData;
    }

    public LiveData<GiaThue> getGiaThue() {
        return giaThueLiveData;
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

    public void loadInitialData() {
        isLoadingLiveData.setValue(true);

        // Load KhachHangs
        repository.getKhachHangs(new CreateContractRepository.DataCallback<List<KhachHangSpinner>>() {
            @Override
            public void onSuccess(List<KhachHangSpinner> data) {
                khachHangsLiveData.setValue(data);
                checkLoadingComplete();
            }

            @Override
            public void onError(String error) {
                isLoadingLiveData.setValue(false);
                errorLiveData.setValue(error);
            }
        });

        // Load Approved Bookings
        repository.getApprovedBookings(new CreateContractRepository.DataCallback<List<PhieuDatXeDuyet>>() {
            @Override
            public void onSuccess(List<PhieuDatXeDuyet> data) {
                bookingsLiveData.setValue(data);
                checkLoadingComplete();
            }

            @Override
            public void onError(String error) {
                isLoadingLiveData.setValue(false);
                errorLiveData.setValue(error);
            }
        });

        // Load Available Xe
        repository.getAvailableXe(new CreateContractRepository.DataCallback<List<Xe>>() {
            @Override
            public void onSuccess(List<Xe> data) {
                availableXeLiveData.setValue(data);
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
        if (khachHangsLiveData.getValue() != null && bookingsLiveData.getValue() != null && availableXeLiveData.getValue() != null) {
            isLoadingLiveData.setValue(false);
        }
    }

    public void getGiaChoXe(int maLoaiXe) {
        repository.getGiaThue(maLoaiXe, new CreateContractRepository.DataCallback<GiaThue>() {
            @Override
            public void onSuccess(GiaThue data) {
                giaThueLiveData.setValue(data);
            }

            @Override
            public void onError(String error) {
                errorLiveData.setValue(error);
            }
        });
    }

    public void submitHopDong(CreateHopDongRequest request) {
        isLoadingLiveData.setValue(true);

        repository.createHopDong(request, new CreateContractRepository.ActionCallback() {
            @Override
            public void onSuccess() {
                isLoadingLiveData.setValue(false);
                successMessageLiveData.setValue("Tạo hợp đồng thành công!");
            }

            @Override
            public void onError(String error) {
                isLoadingLiveData.setValue(false);
                errorLiveData.setValue(error);
            }
        });
    }
}

