package com.example.ungdungthuexe.model;

public class HopDongSpinnerItem {
    private int maHopDong;
    private KhachHangInfo khachHang;

    public HopDongSpinnerItem(int maHopDong, KhachHangInfo khachHang) {
        this.maHopDong = maHopDong;
        this.khachHang = khachHang;
    }

    public int getMaHopDong() {
        return maHopDong;
    }

    public void setMaHopDong(int maHopDong) {
        this.maHopDong = maHopDong;
    }

    public KhachHangInfo getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHangInfo khachHang) {
        this.khachHang = khachHang;
    }

    // For displaying in Spinner
    @Override
    public String toString() {
        return "HĐ #" + maHopDong + " - " + (khachHang != null ? khachHang.getHoTen() : "");
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
}
