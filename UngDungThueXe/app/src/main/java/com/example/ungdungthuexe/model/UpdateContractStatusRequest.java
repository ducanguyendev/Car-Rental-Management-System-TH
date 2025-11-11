package com.example.ungdungthuexe.model;

public class UpdateContractStatusRequest {
    private String trangThai;

    public UpdateContractStatusRequest(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}

