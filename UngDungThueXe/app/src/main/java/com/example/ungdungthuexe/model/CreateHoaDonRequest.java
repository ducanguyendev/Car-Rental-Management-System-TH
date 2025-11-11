package com.example.ungdungthuexe.model;

public class CreateHoaDonRequest {
    private int maHopDong;
    private int maNhanVien;
    private double soTienThanhToan;
    private String loaiThanhToan;

    public CreateHoaDonRequest(int maHopDong, int maNhanVien, double soTienThanhToan, String loaiThanhToan) {
        this.maHopDong = maHopDong;
        this.maNhanVien = maNhanVien;
        this.soTienThanhToan = soTienThanhToan;
        this.loaiThanhToan = loaiThanhToan;
    }

    public int getMaHopDong() {
        return maHopDong;
    }

    public void setMaHopDong(int maHopDong) {
        this.maHopDong = maHopDong;
    }

    public int getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(int maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public double getSoTienThanhToan() {
        return soTienThanhToan;
    }

    public void setSoTienThanhToan(double soTienThanhToan) {
        this.soTienThanhToan = soTienThanhToan;
    }

    public String getLoaiThanhToan() {
        return loaiThanhToan;
    }

    public void setLoaiThanhToan(String loaiThanhToan) {
        this.loaiThanhToan = loaiThanhToan;
    }
}
