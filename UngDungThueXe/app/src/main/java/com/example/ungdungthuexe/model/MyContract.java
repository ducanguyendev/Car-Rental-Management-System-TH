package com.example.ungdungthuexe.model;

public class MyContract {
    private int maHopDong;
    private String ngayLap;
    private String ngayNhanXe;
    private String ngayTraXe;
    private double tongTien;
    private String trangThai;

    public MyContract(int maHopDong, String ngayLap, String ngayNhanXe, 
                      String ngayTraXe, double tongTien, String trangThai) {
        this.maHopDong = maHopDong;
        this.ngayLap = ngayLap;
        this.ngayNhanXe = ngayNhanXe;
        this.ngayTraXe = ngayTraXe;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
    }

    public int getMaHopDong() {
        return maHopDong;
    }

    public void setMaHopDong(int maHopDong) {
        this.maHopDong = maHopDong;
    }

    public String getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(String ngayLap) {
        this.ngayLap = ngayLap;
    }

    public String getNgayNhanXe() {
        return ngayNhanXe;
    }

    public void setNgayNhanXe(String ngayNhanXe) {
        this.ngayNhanXe = ngayNhanXe;
    }

    public String getNgayTraXe() {
        return ngayTraXe;
    }

    public void setNgayTraXe(String ngayTraXe) {
        this.ngayTraXe = ngayTraXe;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}

