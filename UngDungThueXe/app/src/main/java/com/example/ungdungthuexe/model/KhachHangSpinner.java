package com.example.ungdungthuexe.model;

public class KhachHangSpinner {
    private int maKH;
    private String hoTen;
    private String soCMND;

    public KhachHangSpinner(int maKH, String hoTen, String soCMND) {
        this.maKH = maKH;
        this.hoTen = hoTen;
        this.soCMND = soCMND;
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

    public String getSoCMND() {
        return soCMND;
    }

    public void setSoCMND(String soCMND) {
        this.soCMND = soCMND;
    }

    @Override
    public String toString() {
        return hoTen;
    }
}

