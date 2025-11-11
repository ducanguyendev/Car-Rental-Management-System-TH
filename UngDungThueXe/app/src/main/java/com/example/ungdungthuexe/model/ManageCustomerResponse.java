package com.example.ungdungthuexe.model;

public class ManageCustomerResponse {
    private int maKH;
    private String hoTen;
    private String soCMND;
    private String soHoKhau;
    private String soDienThoai;
    private String diaChiCoQuan;
    private int soHopDong;

    public ManageCustomerResponse(int maKH, String hoTen, String soCMND, String soHoKhau,
                                  String soDienThoai, String diaChiCoQuan, int soHopDong) {
        this.maKH = maKH;
        this.hoTen = hoTen;
        this.soCMND = soCMND;
        this.soHoKhau = soHoKhau;
        this.soDienThoai = soDienThoai;
        this.diaChiCoQuan = diaChiCoQuan;
        this.soHopDong = soHopDong;
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

    public String getSoHoKhau() {
        return soHoKhau;
    }

    public void setSoHoKhau(String soHoKhau) {
        this.soHoKhau = soHoKhau;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getDiaChiCoQuan() {
        return diaChiCoQuan;
    }

    public void setDiaChiCoQuan(String diaChiCoQuan) {
        this.diaChiCoQuan = diaChiCoQuan;
    }

    public int getSoHopDong() {
        return soHopDong;
    }

    public void setSoHopDong(int soHopDong) {
        this.soHopDong = soHopDong;
    }
}
