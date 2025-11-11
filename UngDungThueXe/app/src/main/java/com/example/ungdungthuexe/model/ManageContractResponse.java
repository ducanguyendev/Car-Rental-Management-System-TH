package com.example.ungdungthuexe.model;

import java.util.List;

public class ManageContractResponse {
    private int maHopDong;
    private String trangThai;
    private double tongTien;
    private KhachHangInfo khachHang;
    private List<ChiTietHopDongInfo> chiTietHopDongs;

    public ManageContractResponse(int maHopDong, String trangThai, double tongTien,
                                   KhachHangInfo khachHang, List<ChiTietHopDongInfo> chiTietHopDongs) {
        this.maHopDong = maHopDong;
        this.trangThai = trangThai;
        this.tongTien = tongTien;
        this.khachHang = khachHang;
        this.chiTietHopDongs = chiTietHopDongs;
    }

    public int getMaHopDong() {
        return maHopDong;
    }

    public void setMaHopDong(int maHopDong) {
        this.maHopDong = maHopDong;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public KhachHangInfo getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHangInfo khachHang) {
        this.khachHang = khachHang;
    }

    public List<ChiTietHopDongInfo> getChiTietHopDongs() {
        return chiTietHopDongs;
    }

    public void setChiTietHopDongs(List<ChiTietHopDongInfo> chiTietHopDongs) {
        this.chiTietHopDongs = chiTietHopDongs;
    }

    // Nested class for KhachHang information
    public static class KhachHangInfo {
        private String hoTen;

        public KhachHangInfo(String hoTen) {
            this.hoTen = hoTen;
        }

        public String getHoTen() {
            return hoTen;
        }

        public void setHoTen(String hoTen) {
            this.hoTen = hoTen;
        }
    }

    // Nested class for ChiTietHopDong information
    public static class ChiTietHopDongInfo {
        private String tenXe;

        public ChiTietHopDongInfo(String tenXe) {
            this.tenXe = tenXe;
        }

        public String getTenXe() {
            return tenXe;
        }

        public void setTenXe(String tenXe) {
            this.tenXe = tenXe;
        }
    }
}

