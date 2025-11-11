package com.example.ungdungthuexe.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ungdungthuexe.model.GiaThue;
import com.example.ungdungthuexe.model.PhieuDatXeRequest;
import com.example.ungdungthuexe.model.PhieuDatXeResponse;
import com.example.ungdungthuexe.model.XeDetail;
import com.example.ungdungthuexe.repository.XeDetailRepository;

public class XeDetailViewModel extends AndroidViewModel {
    private XeDetailRepository repository;
    private MutableLiveData<XeDetail> xeDetailLiveData;
    private MutableLiveData<GiaThue> giaThueLiveData;
    private MutableLiveData<Boolean> isLoadingLiveData;
    private MutableLiveData<String> errorLiveData;
    private MutableLiveData<PhieuDatXeResponse> datXeResultLiveData;

    public XeDetailViewModel(@NonNull Application application) {
        super(application);
        this.repository = new XeDetailRepository();
        this.xeDetailLiveData = new MutableLiveData<>();
        this.giaThueLiveData = new MutableLiveData<>();
        this.isLoadingLiveData = new MutableLiveData<>();
        this.errorLiveData = new MutableLiveData<>();
        this.datXeResultLiveData = new MutableLiveData<>();
    }

    public LiveData<XeDetail> getXeDetail() {
        return xeDetailLiveData;
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

    public LiveData<PhieuDatXeResponse> getDatXeResult() {
        return datXeResultLiveData;
    }

    public void loadXeDetail(int maXe) {
        isLoadingLiveData.setValue(true);

        // First call: Get xe detail
        repository.getXeDetail(maXe, new XeDetailRepository.DataCallback<XeDetail>() {
            @Override
            public void onSuccess(XeDetail data) {
                xeDetailLiveData.setValue(data);
                
                // Second call: Get gia thue based on maLoaiXe (chained call)
                repository.getGiaThue(data.getMaLoaiXe(), new XeDetailRepository.DataCallback<GiaThue>() {
                    @Override
                    public void onSuccess(GiaThue data) {
                        giaThueLiveData.setValue(data);
                        isLoadingLiveData.setValue(false);
                    }

                    @Override
                    public void onError(String error) {
                        isLoadingLiveData.setValue(false);
                        errorLiveData.setValue(error);
                    }
                });
            }

            @Override
            public void onError(String error) {
                isLoadingLiveData.setValue(false);
                errorLiveData.setValue(error);
            }
        });
    }

    public void datXe(PhieuDatXeRequest request) {
        isLoadingLiveData.setValue(true);

        repository.createPhieuDatXe(request, new XeDetailRepository.DataCallback<PhieuDatXeResponse>() {
            @Override
            public void onSuccess(PhieuDatXeResponse response) {
                isLoadingLiveData.setValue(false);
                datXeResultLiveData.setValue(response);
            }

            @Override
            public void onError(String error) {
                isLoadingLiveData.setValue(false);
                errorLiveData.setValue(error);
            }
        });
    }
}

