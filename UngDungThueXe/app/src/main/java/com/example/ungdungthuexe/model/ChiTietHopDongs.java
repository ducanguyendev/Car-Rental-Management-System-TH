package com.example.ungdungthuexe.model;

public class ChiTietHopDongs {
    private int maXe;
    private double donGiaTaiThoiDiemThue;
    private String ghiChu;

    public ChiTietHopDongs(int maXe, double donGiaTaiThoiDiemThue, String ghiChu) {
        this.maXe = maXe;
        this.donGiaTaiThoiDiemThue = donGiaTaiThoiDiemThue;
        this.ghiChu = ghiChu;
    }

    public int getMaXe() {
        return maXe;
    }

    public void setMaXe(int maXe) {
        this.maXe = maXe;
    }

    public double getDonGiaTaiThoiDiemThue() {
        return donGiaTaiThoiDiemThue;
    }

    public void setDonGiaTaiThoiDiemThue(double donGiaTaiThoiDiemThue) {
        this.donGiaTaiThoiDiemThue = donGiaTaiThoiDiemThue;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}

