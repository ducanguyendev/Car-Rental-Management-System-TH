package com.example.ungdungthuexe.model;

public class GiaThue {
    private int maBangGia;
    private double donGiaTheoNgay;
    private String ngayApDung;
    private LoaiXeInfo loaiXe;

    public GiaThue(int maBangGia, double donGiaTheoNgay, String ngayApDung, LoaiXeInfo loaiXe) {
        this.maBangGia = maBangGia;
        this.donGiaTheoNgay = donGiaTheoNgay;
        this.ngayApDung = ngayApDung;
        this.loaiXe = loaiXe;
    }

    public int getMaBangGia() {
        return maBangGia;
    }

    public void setMaBangGia(int maBangGia) {
        this.maBangGia = maBangGia;
    }

    public double getDonGiaTheoNgay() {
        return donGiaTheoNgay;
    }

    public void setDonGiaTheoNgay(double donGiaTheoNgay) {
        this.donGiaTheoNgay = donGiaTheoNgay;
    }

    public String getNgayApDung() {
        return ngayApDung;
    }

    public void setNgayApDung(String ngayApDung) {
        this.ngayApDung = ngayApDung;
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
        private String moTa;

        public LoaiXeInfo(int maLoaiXe, String tenLoaiXe, String moTa) {
            this.maLoaiXe = maLoaiXe;
            this.tenLoaiXe = tenLoaiXe;
            this.moTa = moTa;
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

        public String getMoTa() {
            return moTa;
        }

        public void setMoTa(String moTa) {
            this.moTa = moTa;
        }
    }
}

