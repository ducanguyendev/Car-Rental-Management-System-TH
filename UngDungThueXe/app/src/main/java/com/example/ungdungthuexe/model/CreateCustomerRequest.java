package com.example.ungdungthuexe.model;

public class CreateCustomerRequest {
    private String hoTen;
    private String soCMND;
    private String soHoKhau;
    private String soDienThoai;
    private String diaChiCoQuan;

    public CreateCustomerRequest(String hoTen, String soCMND, String soHoKhau,
                                 String soDienThoai, String diaChiCoQuan) {
        this.hoTen = hoTen;
        this.soCMND = soCMND;
        this.soHoKhau = soHoKhau;
        this.soDienThoai = soDienThoai;
        this.diaChiCoQuan = diaChiCoQuan;
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
}
