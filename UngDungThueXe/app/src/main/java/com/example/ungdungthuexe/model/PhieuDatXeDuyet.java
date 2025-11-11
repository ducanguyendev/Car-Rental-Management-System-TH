package com.example.ungdungthuexe.model;

public class PhieuDatXeDuyet {
    private int maDatXe;
    private KhachHangInfo khachHang;
    private LoaiXeInfo loaiXe;

    public PhieuDatXeDuyet(int maDatXe, KhachHangInfo khachHang, LoaiXeInfo loaiXe) {
        this.maDatXe = maDatXe;
        this.khachHang = khachHang;
        this.loaiXe = loaiXe;
    }

    public int getMaDatXe() {
        return maDatXe;
    }

    public void setMaDatXe(int maDatXe) {
        this.maDatXe = maDatXe;
    }

    public KhachHangInfo getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHangInfo khachHang) {
        this.khachHang = khachHang;
    }

    public LoaiXeInfo getLoaiXe() {
        return loaiXe;
    }

    public void setLoaiXe(LoaiXeInfo loaiXe) {
        this.loaiXe = loaiXe;
    }

    // Nested class for KhachHang information
    public static class KhachHangInfo {
        private int maKH;
        private String hoTen;

        public KhachHangInfo(int maKH, String hoTen) {
            this.maKH = maKH;
            this.hoTen = hoTen;
        }

        public int getMaKH() {
            return maKH;
        }

        public void setMaKH(int maKH) {
            this.maKH = maKH;
        }

        public String getHoTen() {
            return hoTen;
        }

        public void setHoTen(String hoTen) {
            this.hoTen = hoTen;
        }
    }

    // Nested class for LoaiXe information
    public static class LoaiXeInfo {
        private int maLoaiXe;

        public LoaiXeInfo(int maLoaiXe) {
            this.maLoaiXe = maLoaiXe;
        }

        public int getMaLoaiXe() {
            return maLoaiXe;
        }

        public void setMaLoaiXe(int maLoaiXe) {
            this.maLoaiXe = maLoaiXe;
        }
    }

    @Override
    public String toString() {
        return "Phiếu #" + maDatXe + " - " + khachHang.getHoTen();
    }
}

