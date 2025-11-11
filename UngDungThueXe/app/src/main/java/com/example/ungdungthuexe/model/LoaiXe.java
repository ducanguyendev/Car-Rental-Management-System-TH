package com.example.ungdungthuexe.model;

public class LoaiXe {
    private int maLoaiXe;
    private String tenLoaiXe;
    private String moTa;
    private int soLuongXe;

    public LoaiXe(int maLoaiXe, String tenLoaiXe, String moTa, int soLuongXe) {
        this.maLoaiXe = maLoaiXe;
        this.tenLoaiXe = tenLoaiXe;
        this.moTa = moTa;
        this.soLuongXe = soLuongXe;
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

    public int getSoLuongXe() {
        return soLuongXe;
    }

    public void setSoLuongXe(int soLuongXe) {
        this.soLuongXe = soLuongXe;
    }
}

