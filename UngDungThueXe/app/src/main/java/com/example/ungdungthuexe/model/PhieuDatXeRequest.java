package com.example.ungdungthuexe.model;

public class PhieuDatXeRequest {
    private int maKH;
    private int maLoaiXe;
    private String ngayDuKienNhan;

    public PhieuDatXeRequest(int maKH, int maLoaiXe, String ngayDuKienNhan) {
        this.maKH = maKH;
        this.maLoaiXe = maLoaiXe;
        this.ngayDuKienNhan = ngayDuKienNhan;
    }

    public int getMaKH() {
        return maKH;
    }

    public void setMaKH(int maKH) {
        this.maKH = maKH;
    }

    public int getMaLoaiXe() {
        return maLoaiXe;
    }

    public void setMaLoaiXe(int maLoaiXe) {
        this.maLoaiXe = maLoaiXe;
    }

    public String getNgayDuKienNhan() {
        return ngayDuKienNhan;
    }

    public void setNgayDuKienNhan(String ngayDuKienNhan) {
        this.ngayDuKienNhan = ngayDuKienNhan;
    }
}

