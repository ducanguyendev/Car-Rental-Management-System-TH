package com.example.ungdungthuexe.model;

public class UpdateBookingStatusRequest {
    private String trangThai;

    public UpdateBookingStatusRequest(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}

