package com.example.ungdungthuexe.model;

public class RegisterRequest {
    private String tenDangNhap;
    private String matKhau;
    private String hoTen;
    private String soCMND;
    private String soHoKhau;
    private String soDienThoai;
    private String diaChiCoQuan;

    public RegisterRequest(String tenDangNhap, String matKhau, String hoTen, 
                          String soCMND, String soHoKhau, String soDienThoai, String diaChiCoQuan) {
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.hoTen = hoTen;
        this.soCMND = soCMND;
        this.soHoKhau = soHoKhau;
        this.soDienThoai = soDienThoai;
        this.diaChiCoQuan = diaChiCoQuan;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
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

