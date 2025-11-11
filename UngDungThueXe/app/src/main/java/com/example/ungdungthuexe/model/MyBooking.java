package com.example.ungdungthuexe.model;

public class MyBooking {
    private int maDatXe;
    private String ngayDat;
    private String ngayDuKienNhan;
    private String trangThai;
    private LoaiXeInfo loaiXe;

    public MyBooking(int maDatXe, String ngayDat, String ngayDuKienNhan, String trangThai, LoaiXeInfo loaiXe) {
        this.maDatXe = maDatXe;
        this.ngayDat = ngayDat;
        this.ngayDuKienNhan = ngayDuKienNhan;
        this.trangThai = trangThai;
        this.loaiXe = loaiXe;
    }

    public int getMaDatXe() {
        return maDatXe;
    }

    public void setMaDatXe(int maDatXe) {
        this.maDatXe = maDatXe;
    }

    public String getNgayDat() {
        return ngayDat;
    }

    public void setNgayDat(String ngayDat) {
        this.ngayDat = ngayDat;
    }

    public String getNgayDuKienNhan() {
        return ngayDuKienNhan;
    }

    public void setNgayDuKienNhan(String ngayDuKienNhan) {
        this.ngayDuKienNhan = ngayDuKienNhan;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public LoaiXeInfo getLoaiXe() {
        return loaiXe;
    }

    public void setLoaiXe(LoaiXeInfo loaiXe) {
        this.loaiXe = loaiXe;
    }

    // Nested class for LoaiXe information
    public static class LoaiXeInfo {
        private int maLoaiXe;
        private String tenLoaiXe;

        public LoaiXeInfo(int maLoaiXe, String tenLoaiXe) {
            this.maLoaiXe = maLoaiXe;
            this.tenLoaiXe = tenLoaiXe;
        }

        public int getMaLoaiXe() {
            return maLoaiXe;
        }

        public void setMaLoaiXe(int maLoaiXe) {
            this.maLoaiXe = maLoaiXe;
        }

        public String getTenLoaiXe() {
            return tenLoaiXe;
        }

        public void setTenLoaiXe(String tenLoaiXe) {
            this.tenLoaiXe = tenLoaiXe;
        }
    }
}

